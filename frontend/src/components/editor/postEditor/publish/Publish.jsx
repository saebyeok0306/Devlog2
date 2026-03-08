"use client";
import { Button, Carousel, Dropdown, Modal, TextInput } from "flowbite-react";

import "./Publish.scss";
import { useEffect, useState } from "react";
import { onErrorImg } from "@/utils/defaultImg";
import { edit_post_api, upload_post_api } from "@/api/posts";
import { toast } from "react-toastify";
import { POST_STORE } from "@/api/cache";
import { useRouter } from "next/navigation";

const safeUrlValidator = (url) => {
  return url
    .replace(/\s+/g, "-") // 1개 이상의 공백을 "-"로 변경
    .replace(/[^a-zA-Z0-9ㄱ-ㅎ가-힣-_]/g, "") // 영문자, 한글, 숫자를 제외한 문자는 공백으로 변경
    .replace(/-$/, ""); // 마지막 문자가 "-"인 경우 공백으로 변경
};

function Publish({
  openModal,
  setOpenModal,
  authDto,
  categories,
  postContext,
  setPostContext,
}) {
  const navigate = useRouter();
  const [postUrl, setPostUrl] = useState();
  const [notEditUrl, setNotEditUrl] = useState(false);

  useEffect(() => {
    if (openModal && !postUrl && postContext.title) {
      if (postContext.url === "") {
        setPostUrl(safeUrlValidator(postContext.title));
      } else {
        setPostUrl(postContext.url);
        setNotEditUrl(true);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [openModal]);

  const closeHandler = () => {
    setOpenModal(false);
  };

  const publicHandler = (e) => {
    e.preventDefault();
    setPostContext({ ...postContext, hidden: false });
    toast.info("전체공개로 설정되었습니다.");
  };

  const privateHandler = (e) => {
    e.preventDefault();
    setPostContext({ ...postContext, hidden: true });
    toast.info("비공개로 설정되었습니다.");
  };

  const publishHandler = async () => {
    const previewUrl = postContext.preview
      ? `${process.env.NEXT_PUBLIC_API_FILE_URL}/${postContext.preview?.filePath}/${postContext.preview?.fileUrl}`
      : null;
    if (postContext.id === null) {
      try {
        await upload_post_api({
          postContext: postContext,
          postUrl: postUrl,
          previewUrl: previewUrl,
        });
        toast.info("게시글을 업로드했습니다!");
        POST_STORE.clear();
      } catch (err) {
        toast.error("게시글 업로드에 실패했습니다!");
        return;
      }
    } else {
      const modifiedAt = new Date().toISOString();
      try {
        await edit_post_api({
          postContext: postContext,
          postUrl: postUrl,
          previewUrl: previewUrl,
          modifiedAt: modifiedAt,
        });
        toast.info("게시글을 수정했습니다!");
        POST_STORE.clear();
      } catch (err) {
        toast.error("게시글 수정에 실패했습니다!");
        return;
      }
    }
    closeHandler();
    navigate.push(`/post/${postUrl}`);
  };

  const changePostUrlHandler = async (e) => {
    if (notEditUrl) return;
    await setPostUrl(safeUrlValidator(e.target.value));
  };

  const PreviewCarousel = () => {
    if (postContext.files.length === 0) {
      return (
        <div className="publish-preview-list">
          <h3 className="text-xl font-medium">미리보기 선택</h3>
          <p>미리보기 이미지가 없습니다.</p>
        </div>
      );
    }

    return (
      <div className="publish-preview-list">
        <h3 className="text-xl font-medium">미리보기 선택</h3>
        <Carousel slide={false}>
          {postContext.files
            .filter((file) => file.fileType === "IMAGE")
            .map((file, index) => (
              <img
                key={index}
                className="publish-preview-list-image"
                src={`${process.env.NEXT_PUBLIC_API_FILE_URL}/${file.filePath}/${file.fileUrl}`}
                alt={`preview${index}`}
                onClick={() =>
                  setPostContext({ ...postContext, preview: file })
                }
              />
            ))}
        </Carousel>
      </div>
    );
  };

  return (
    <>
      <Modal show={openModal} onClose={closeHandler} size="4xl" popup>
        <Modal.Header />
        <Modal.Body>
          <div className="publish-modal">
            <div className="publish-left">
              <div className="publish-preview">
                <h3 className="text-xl font-medium">포스트 미리보기</h3>
                <div className="publish-preview-img">
                  {postContext.preview ? (
                    <img
                      src={`${process.env.NEXT_PUBLIC_API_FILE_URL}/${postContext.preview?.filePath}/${postContext.preview?.fileUrl}`}
                      alt="preview"
                      onError={onErrorImg}
                    />
                  ) : (
                    <img src="" alt="preview" onError={onErrorImg} />
                  )}
                </div>
              </div>
              <PreviewCarousel />
            </div>
            <div className="publish-right">
              <div className="publish-public">
                <h3 className="text-xl font-medium">포스트 공개범위</h3>
                <div>
                  <Button
                    color={postContext.hidden ? "gray" : "blue"}
                    onClick={publicHandler}
                  >
                    전체공개
                  </Button>
                  <Button
                    color={postContext.hidden ? "warning" : "gray"}
                    onClick={privateHandler}
                  >
                    비공개
                  </Button>
                </div>
              </div>
              <div className="publish-category">
                <h3 className="text-xl font-medium">카테고리</h3>
                <div className="border border-gray-300 bg-gray-50 text-gray-900 focus:border-cyan-500 focus:ring-cyan-500 dark:border-gray-600 dark:bg-gray-700 dark:text-white dark:placeholder-gray-400 dark:focus:border-cyan-500 dark:focus:ring-cyan-500 p-2.5 text-sm rounded-lg">
                  <Dropdown label={postContext.category?.name} inline>
                    {categories.map((category, idx) => (
                      <Dropdown.Item
                        key={idx}
                        onClick={() =>
                          setPostContext({ ...postContext, category: category })
                        }
                      >
                        {category?.name}
                      </Dropdown.Item>
                    ))}
                  </Dropdown>
                </div>
              </div>
              <div className="publish-url">
                <h3 className="text-xl font-medium">포스트 URL</h3>
                <TextInput
                  placeholder="포스트 URL"
                  required
                  color="gray"
                  value={postUrl || ""}
                  onChange={(e) => changePostUrlHandler(e)}
                />
              </div>
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer className="publish-footer">
          <Button onClick={publishHandler}>발행하기</Button>
          <Button color="gray" onClick={closeHandler}>
            취소
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
export default Publish;
