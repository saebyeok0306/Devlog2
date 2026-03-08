"use client";
import { useEffect } from "react";
import { useRecoilState } from "recoil";
import { postAtom } from "@/recoil/postAtom";

import "./TOC.scss";
import { Button, Dropdown } from "flowbite-react";
import {
  HiOutlineChevronDoubleUp,
  HiChatAlt,
  HiLink,
  HiDotsVertical,
} from "react-icons/hi";
import { toast } from "react-toastify";
import { PostContext, postContextAtom } from "@/recoil/editorAtom";
import { delete_post_api } from "@/api/posts";
import { POST_STORE } from "@/api/cache";
import tocbot from "tocbot";
import { useParams, useRouter } from "next/navigation";

const scrollToTopHandler = () => {
  window.scrollTo({
    top: 0,
    behavior: "smooth",
  });
};

const scrollToCommentHandler = (commentRef) => {
  if (commentRef.current === null) return;
  commentRef.current.scrollIntoView({
    behavior: "smooth",
  });
};

const exportUrlHandler = (postContent) => {
  const url = `${window.location.origin}/post/${postContent?.url}`;
  navigator.clipboard.writeText(url);
  toast.info("URL이 복사되었습니다.", { position: "bottom-center" });
};

// TODO: 게시글 수정은 /editor/{url} 형태로 새로고침해도 유지되도록 변경
const postEditHandler = async (navigate, postUrl) => {
  navigate.push("/editor/" + postUrl);
};

const postDeleteHandler = async (navigate, postContent) => {
  try {
    await delete_post_api(postContent.url);
    POST_STORE.clear();
    navigate.back();
    toast.info("게시글이 삭제되었습니다.");
  } catch (error) {
    toast.error("게시글 삭제 중 오류가 발생했습니다.");
    console.error("Failed to delete post:", error);
  }
};

const postStatisticsHandler = async (navigate, postContent) => {
  navigate.push(`/post/${postContent.url}/statistics`);
};

function TOC({ ...props }) {
  const { commentRef } = props;
  const { postUrl } = useParams();
  const navigate = useRouter();
  const [postContent] = useRecoilState(postAtom);

  useEffect(() => {
    tocbot.init({
      // Where to render the table of contents.
      tocSelector: ".toc",
      // Where to grab the headings to build the table of contents.
      contentSelector: ".post-context",
      // Which headings to grab inside of the contentSelector element.
      headingSelector: "h1, h2, h3",
      // For headings inside relative or absolute positioned containers within content.
      hasInnerContainers: false,
      linkClass: "toc-link",
      isCollapsedClass: ".collapsed",
      collapseDepth: 0,

      disableTocScrollSync: true,
      enableUrlHashUpdateOnScroll: false,
    });

    return () => {
      tocbot.destroy();
    };
  });

  return (
    <aside className="toc-box">
      <div className="toc-container">
        <span className="toc-title">목차</span>
        <div className="toc"></div>
        {/* <Toc
          markdownText={postContent?.content}
          type="raw"
          className="toc text-gray-800 dark:text-gray-300"
        /> */}
        <div className="toc-buttons">
          <Button
            className="toc-button"
            size="xs"
            color="gray"
            onClick={scrollToTopHandler}
          >
            <HiOutlineChevronDoubleUp />
          </Button>
          <Button
            className="toc-button"
            size="xs"
            color="gray"
            onClick={() => scrollToCommentHandler(commentRef)}
          >
            <HiChatAlt />
          </Button>
          <Button
            className="toc-button"
            size="xs"
            color="gray"
            onClick={() => exportUrlHandler(postContent)}
          >
            <HiLink />
          </Button>
          {postContent?.ownership ? (
            <Dropdown
              className="dropdown"
              label=""
              // inline
              renderTrigger={() => (
                <Button className="toc-button" size="xs" color="gray">
                  <HiDotsVertical />
                </Button>
              )}
            >
              <Dropdown.Item
                onClick={() => postStatisticsHandler(navigate, postContent)}
              >
                통계
              </Dropdown.Item>
              <Dropdown.Item onClick={() => postEditHandler(navigate, postUrl)}>
                수정
              </Dropdown.Item>
              <Dropdown.Item
                onClick={() => postDeleteHandler(navigate, postContent)}
              >
                삭제
              </Dropdown.Item>
            </Dropdown>
          ) : null}
        </div>
      </div>
    </aside>
  );
}

export default TOC;
