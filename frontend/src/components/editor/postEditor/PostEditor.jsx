"use client";
import React, { useEffect, useRef, useState } from "react";

import { authAtom } from "@/recoil/authAtom";
import { useRecoilState, useRecoilValue } from "recoil";
import { CKEditor } from "@ckeditor/ckeditor5-react";
import { CustomUploadAdapter, editorConfig } from "./ClassicEditor";
import { ClassicEditor } from "ckeditor5";

import "./PostEditor.scss";
import { Button } from "flowbite-react";
import { POST_CONTEXT_DEFAULT, postContextAtom } from "@/recoil/editorAtom";
import { toast } from "react-toastify";
import { get_categories_readwrite_api } from "@/api/category";
import Publish from "./publish";
import hljs from "highlight.js";
import FileUploader from "./fileUploader";
import { useRouter } from "next/navigation";

const divideTitleAndBody = (content) => {
  const parser = new DOMParser();
  const doc = parser.parseFromString(content, "text/html");
  const header1 = doc.querySelector("h1");
  if (!header1) {
    return false;
  }
  const title = header1.textContent;
  header1.remove();
  const detail = doc.body.innerHTML;
  return {
    title: title.trim(),
    body: detail,
  };
};

function PostEditor() {
  const editorRef = useRef(null);
  const navigate = useRouter();
  const authDto = useRecoilValue(authAtom);

  const [editorInstance, setEditorInstance] = useState(null);
  const [isLayoutReady, setIsLayoutReady] = useState(false);
  const [openModal, setOpenModal] = useState(false);

  const [categories, setCategories] = useState([]);
  const [postContext, setPostContext] = useRecoilState(postContextAtom);

  const [openLoader, setOpenLoader] = useState(false);
  const [uploaderFiles, setUploaderFiles] = useState([]); // 업로드된 파일 + 임시파일

  useEffect(() => {
    const fileUploadListener = (e) => {
      if (window.sessionStorage.getItem("fileUploader") === "true") {
        setOpenLoader(true);
      }
    };
    window.addEventListener("fileUploader", fileUploadListener);

    return () => {
      window.removeEventListener("fileUploader", fileUploadListener);
    };
  }, []);

  useEffect(() => {
    if (openLoader) {
      setUploaderFiles([
        ...postContext.files.filter((file) => file.fileType !== "IMAGE"),
      ]);
    }
    // eslint-disable-next-line
  }, [openLoader]);

  useEffect(() => {
    setIsLayoutReady(true);

    get_categories_readwrite_api()
      .then((res) => {
        const res_categories = res;
        if (res_categories.length === 0) {
          toast.warning("글을 작성할 수 있는 카테고리가 없습니다!");
          return navigate.back();
        }
        setCategories(res_categories);
        if (postContext.category == null) {
          setPostContext((prev) => ({ ...prev, category: res_categories[0] }));
        }
      })
      .catch((err) => {
        console.error(err);
      });
    return () => {
      setOpenModal(false);
      setOpenLoader(false);
      setPostContext({ ...POST_CONTEXT_DEFAULT });
      setIsLayoutReady(false);
    };
    // eslint-disable-next-line
  }, []);

  useEffect(() => {
    if (editorInstance && postContext.title && postContext.content) {
      const header = `<h1>${postContext.title}</h1>`;
      const content = header + postContext.content;
      editorInstance.setData(content);
    }
    // eslint-disable-next-line
  }, [editorInstance]);

  useEffect(() => {
    if (!postContext.preview) {
      const images = postContext.files.filter(
        (file) => file.fileType === "IMAGE"
      );
      if (images.length < 1) return;
      setPostContext((prev) => ({ ...prev, preview: images[0] }));
    } else {
      // files 중 content에 없는 이미지가 preview인 경우 다시 files에서 첫번째 항목으로 선택.
      if (
        postContext.content.indexOf(
          `(${process.env.NEXT_PUBLIC_API_FILE_URL}/${postContext.preview.filePath}/${postContext.preview.fileUrl})`
        ) === -1
      ) {
        setPostContext((prev) => ({ ...prev, preview: prev.files[0] }));
      }
    }
    // eslint-disable-next-line
  }, [openModal]);

  const publishHandler = (e) => {
    e.preventDefault();
    const res = divideTitleAndBody(postContext.content);
    if (!res) {
      toast.error("제목을 입력해주세요.");
      return;
    }
    const { title, body } = res;
    if (!title) {
      toast.error("제목을 입력해주세요.");
      return;
    }
    if (!body) {
      toast.error("내용을 입력해주세요.");
      return;
    }

    // files 중 content에 없는 파일은 files에서 제거
    const fileFilter = (file) => {
      return (
        postContext.content.indexOf(
          `${process.env.NEXT_PUBLIC_API_FILE_URL}/${file.filePath}/${file.fileUrl}`
        ) !== -1 || file.fileType !== "IMAGE"
      );
    };
    const newFiles = postContext.files.filter((file) => fileFilter(file));

    const tempDiv = document.createElement("div");
    tempDiv.innerHTML = body;

    // <pre><code> 태그에 하이라이트 적용
    tempDiv.querySelectorAll("pre code").forEach((block) => {
      hljs.highlightElement(block);
    });

    setPostContext((prev) => ({
      ...prev,
      files: newFiles,
      title: title,
      body: tempDiv.innerHTML,
    }));

    setOpenModal(true);
  };

  if (categories.length === 0) {
    return <></>;
  }

  return (
    <div className="editor-container post-editor">
      <FileUploader
        openLoader={openLoader}
        setOpenLoader={setOpenLoader}
        postContext={postContext}
        setPostContext={setPostContext}
        uploaderFiles={uploaderFiles}
        setUploaderFiles={setUploaderFiles}
      />
      <div className="editor-container__editor">
        {isLayoutReady && (
          <CKEditor
            ref={editorRef}
            className="ck-editor-container"
            editor={ClassicEditor}
            config={editorConfig}
            onReady={(editor) => {
              setEditorInstance(editor);
              editor.plugins.get("FileRepository").createUploadAdapter = (
                loader
              ) => {
                return new CustomUploadAdapter(loader, setPostContext);
              };
            }}
            onChange={(event, editor) => {
              setPostContext((prev) => ({
                ...prev,
                content: editor.getData(),
              }));
            }}
          />
        )}
        <Button
          className="post-editor-publish-btn outline outline-[#d0d7de] dark:outline-[#30363d]"
          color="gray"
          onClick={(e) => publishHandler(e)}
        >
          발행하기
        </Button>
      </div>
      <Publish
        openModal={openModal}
        setOpenModal={setOpenModal}
        authDto={authDto}
        categories={categories}
        postContext={postContext}
        setPostContext={setPostContext}
      />
    </div>
  );
}

export default PostEditor;
