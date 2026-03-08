"use client";
import React, { useEffect, useRef, useState } from "react";

import "./CommentEditor.scss";
import { toast } from "react-toastify";
import { ClassicEditor } from "ckeditor5";
import { CKEditor } from "@ckeditor/ckeditor5-react";
import {
  commentEditorConfig,
  CustomCommentUploadAdapter,
} from "./ClassicEditor";
import hljs from "highlight.js";
import { useRecoilState, useRecoilValue } from "recoil";
import { commentFilesAtom } from "@/recoil/commentAtom";
import { renderAtom } from "@/recoil/renderAtom";
import { Checkbox, Label } from "flowbite-react";

// import CKEditorInspector from "@ckeditor/ckeditor5-inspector";

function CommentEditor({
  comment,
  setComment,
  onCancel,
  onSave,
  setUpdater,
  allowHidden,
  captchaRef,
}) {
  const MAX_LENGTH = 5000;
  const editorRef = useRef(null);
  const privateRef = useRef(null);

  const [editorInstance, setEditorInstance] = useState(null);
  const [isLayoutReady, setIsLayoutReady] = useState(false);
  const isRender = useRecoilValue(renderAtom);
  const [throttle, setThrottle] = useState(false);
  const [commentFiles, setCommentFiles] = useRecoilState(commentFilesAtom);

  useEffect(() => {
    setIsLayoutReady(true);
    return () => {
      setIsLayoutReady(false);
    };
  }, []);

  const handleChangeContent = async (editor) => {
    const content = editor.getData();
    if (content.length <= MAX_LENGTH) {
      await setComment({ ...comment, content: content });
    } else {
      const newContent = content.slice(0, MAX_LENGTH);
      await setComment({ ...comment, content: newContent });
      editor.setData(newContent);
      if (throttle) return;
      if (!throttle) {
        setThrottle(true);
        setTimeout(async () => {
          toast.info(`최대 ${MAX_LENGTH}자까지 입력 가능합니다.`, {
            position: "bottom-center",
          });
          setThrottle(false);
        }, 1000);
      }
    }
  };

  const onSaveHandler = async () => {
    if (comment.content.length === 0) {
      toast.info("댓글 내용을 입력해주세요.");
      return;
    }

    const tempDiv = document.createElement("div");
    tempDiv.innerHTML = comment.content;

    // <pre><code> 태그에 하이라이트 적용
    tempDiv.querySelectorAll("pre code").forEach((block) => {
      hljs.highlightElement(block);
    });

    const fileFilter = (file) => {
      return (
        comment.content.indexOf(
          `${process.env.NEXT_PUBLIC_API_FILE_URL}/${file.filePath}/${file.fileUrl}`
        ) !== -1 || file.fileType === "VIDEO"
      );
    };
    const newFiles = commentFiles.filter((file) => fileFilter(file));

    await setComment({
      ...comment,
      content: tempDiv.innerHTML,
      hidden: privateRef?.current != null ? privateRef?.current.checked : false,
    });
    const res = await onSave(tempDiv.innerHTML, newFiles);
    if (res) {
      editorInstance.setData("");
      captchaRef?.current.props.grecaptcha.reset();
    }
    if (privateRef.current) privateRef.current.checked = false;
    await setUpdater((prev) => prev + 1);
  };

  const onCancelHandler = async () => {
    editorInstance.setData("");
    await setCommentFiles([]);
    await onCancel();
  };

  if (!isRender) {
    return <></>;
  }

  return (
    <div className="editor-container comment-editor">
      <div className="editor-container__editor">
        {isLayoutReady && (
          <CKEditor
            ref={editorRef}
            className="ck-editor-container"
            editor={ClassicEditor}
            config={commentEditorConfig}
            onReady={(editor) => {
              // CKEditorInspector.attach(editor);

              const model = editor.model;
              setEditorInstance(editor);
              if (comment.content) {
                editor.setData(comment.content);
                if (privateRef.current) {
                  privateRef.current.checked = comment.hidden;
                }
              }
              editor.plugins.get("FileRepository").createUploadAdapter = (
                loader
              ) => {
                return new CustomCommentUploadAdapter(loader, setCommentFiles);
              };
            }}
            onChange={(event, editor) => {
              handleChangeContent(editor).then();
              // const originalExecute = editor.execute;
              //
              // editor.execute = (...args) => {
              //   // 실행된 명령과 인자를 로그로 출력
              //   console.log(`Command executed: ${args[0]}`, args);
              //
              //   // 원래 execute 명령을 호출
              //   return originalExecute.apply(editor, args);
              // };

              editor.plugins
                .get("ImageUploadEditing")
                .on("uploadComplete", (evt, { data, imageElement }) => {
                  if (imageElement.is("element", "imageBlock")) {
                    editor.execute("imageStyle", { value: "alignBlockLeft" });
                    editor.execute("resizeImage", { width: "25%" });
                  }
                });
              // END
            }}
          />
        )}
      </div>
      <div className="comment-editor-bottom">
        <div className="mr-0.5">
          {allowHidden === true ? (
            <>
              <Checkbox
                id="private-comment"
                className="mr-1"
                ref={privateRef}
                color="purple"
                onChange={(e) =>
                  setComment({ ...comment, hidden: e.target.checked })
                }
              />
              <Label htmlFor="private-comment">비밀댓글</Label>
            </>
          ) : null}
        </div>
        <span>
          {comment.content.length}/{MAX_LENGTH}
        </span>
        {onSave ? <button onClick={onSaveHandler}>등록</button> : null}
        {onCancel ? <button onClick={onCancelHandler}>취소</button> : null}
      </div>
    </div>
  );
}

export default CommentEditor;
