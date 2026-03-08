"use client";
import { useState } from "react";
import { Button, FileInput, Label, List, Modal } from "flowbite-react";
import { upload_file_api } from "@/api/file";
import { toast } from "react-toastify";
import { HashLoader } from "react-spinners";

import { HiOutlineX } from "react-icons/hi";

import "./FileUploader.scss";
import { useTheme } from "next-themes";

function FileUploader({
  openLoader,
  setOpenLoader,
  postContext,
  setPostContext,
  uploaderFiles,
  setUploaderFiles,
}) {
  const { resolvedTheme } = useTheme();
  const isDark = resolvedTheme === "dark";
  const [files, setFiles] = useState([]); // 임시파일
  const [isLoading, setIsLoading] = useState(false);

  const closeHandler = () => {
    if (isLoading) {
      return;
    }
    setFiles([]);
    setOpenLoader(false);
    window.sessionStorage.setItem("fileUploader", "false");
  };

  const fileHandler = async (e) => {
    const temp_files = Array.from(e.target.files);
    const uploaded_files = [];

    setIsLoading(true);

    for (let i = 0; i < temp_files.length; i++) {
      const file = temp_files[i];
      if (file.type.startsWith("image")) {
        toast.info("이미지 파일은 업로드할 수 없습니다.");
        continue;
      }
      try {
        const upload_result = await upload_file_api(file);
        uploaded_files.push(upload_result);
      } catch (error) {
        toast.error(`파일 업로드에 실패했습니다.\n${error}`);
      }
    }
    setUploaderFiles([...uploaderFiles, ...uploaded_files]);
    setFiles([...files, ...uploaded_files]);
    setIsLoading(false);

    // file.type = "image/png" mimetype
  };

  const uploadHandler = async () => {
    await setPostContext({
      ...postContext,
      files: [...postContext.files, ...files],
    });
    closeHandler();
  };

  const removeFileHandler = (file, idx) => {
    for (let i = 0; i < postContext.files.length; i++) {
      const target = postContext.files[i];
      if (target.fileUrl === file.fileUrl) {
        setPostContext({
          ...postContext,
          files: postContext.files.filter((_, index) => index !== i),
        });
        setUploaderFiles(uploaderFiles.filter((_, index) => index !== idx));
        return;
      }
    }

    for (let i = 0; i < files.length; i++) {
      const target = files[i];
      if (target.fileUrl === file.fileUrl) {
        setFiles(files.filter((_, index) => index !== i));
        setUploaderFiles(uploaderFiles.filter((_, index) => index !== idx));
        return;
      }
    }
  };

  const UploadedFiles = () => {
    if (uploaderFiles.length === 0) {
      return <></>;
    }
    return (
      <List className="file-upload-files">
        {uploaderFiles.map((file, idx) => (
          <List.Item key={idx} className="file-upload-file">
            <button
              className="inline-flex items-center rounded-lg bg-transparent p-1.5 text-sm text-gray-400 hover:bg-gray-200 hover:text-gray-900 dark:hover:bg-gray-600 dark:hover:text-white"
              onClick={() => removeFileHandler(file, idx)}
            >
              <p className="file-upload-file-name">{file.fileName}</p>
              <HiOutlineX />
            </button>
          </List.Item>
        ))}
      </List>
    );
  };

  return (
    <>
      <Modal show={openLoader} onClose={closeHandler} size="xl" popup>
        <Modal.Header />
        <Modal.Body>
          <div className="relative">
            {isLoading && (
              <div className="file-upload-loader">
                <HashLoader
                  color={isDark ? "#fff" : "#000"}
                  loading={isLoading}
                  size={50}
                />
              </div>
            )}
            <div className="mb-2 block">
              <Label htmlFor="file-upload" value="Upload file" />
            </div>
            <FileInput
              id="file-upload"
              onChange={(e) => fileHandler(e)}
              multiple
            />
          </div>
          <UploadedFiles />
        </Modal.Body>
        <Modal.Footer className="publish-footer">
          <Button
            onClick={() => uploadHandler()}
            disabled={isLoading ? true : false}
          >
            첨부하기
          </Button>
          <Button
            color="gray"
            onClick={() => closeHandler()}
            disabled={isLoading ? true : false}
          >
            취소
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default FileUploader;
