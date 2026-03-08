"use client";
import React from "react";

import "./Post.scss";
import { getDatetime } from "@/utils/getDatetime";

import {
  HiCalendar,
  HiHeart,
  HiOutlineHeart,
  HiOutlineChatAlt2,
} from "react-icons/hi";
import { useRecoilValue } from "recoil";
import { postAtom } from "@/recoil/postAtom";
import { List, Tooltip } from "flowbite-react";
import { authAtom } from "@/recoil/authAtom";
import { post_like_api, post_unlike_api } from "@/api/like";
import { toast } from "react-toastify";
import { commentsAtom } from "@/recoil/commentAtom";
import PostInitializer from "./PostInitializer";
import { useTheme } from "next-themes";
import Tailcard from "@/components/tailcard";

const onLikeHandler = async (postUrl, likes, setLikes, authDto) => {
  try {
    await post_like_api(postUrl);
    setLikes({
      ...likes,
      liked: true,
      likeCount: likes.likeCount + 1,
      users: [...likes.users, authDto],
    });
  } catch (error) {
    console.error("Failed to like post:", error);
    toast.info(error.response.data.error);
  }
};

const onLikeCancelHandler = async (postUrl, likes, setLikes, authDto) => {
  try {
    await post_unlike_api(postUrl);
    setLikes({
      ...likes,
      liked: false,
      likeCount: likes.likeCount - 1,
      users: likes.users.filter((user) => user.email !== authDto.email),
    });
  } catch (error) {
    console.error("Failed to cancel like post:", error);
  }
};

function Post({ ...props }) {
  const { likes, setLikes } = props;
  const { resolvedTheme } = useTheme();
  const isDark = resolvedTheme === "dark";
  const authDto = useRecoilValue(authAtom);
  const postContent = useRecoilValue(postAtom);
  const CommentsData = useRecoilValue(commentsAtom);

  PostInitializer(postContent);

  if (postContent === null) {
    // return <Navigate replace to="/" />;
    return <></>;
  }

  const UploadedFiles = () => {
    if (!postContent?.files) return <></>;
    const files = postContent.files.filter((file) => file.fileType !== "IMAGE");
    if (files.length === 0) {
      return <></>;
    }

    const bytesToMB = (bytes) => {
      return Math.round((bytes / (1024 * 1024)) * 100) / 100;
    };
    return (
      <>
        <hr />
        <div>
          <div>첨부파일</div>
          <List className="file-upload-files">
            {files.map((file, idx) => (
              <List.Item key={idx} className="file-upload-file">
                <button
                  className="inline-flex items-center rounded-lg bg-transparent p-1.5 text-sm text-gray-700 dark:text-gray-400 hover:bg-gray-200 hover:text-gray-900 dark:hover:bg-gray-600 dark:hover:text-white"
                  onClick={null}
                >
                  <a
                    href={`${process.env.NEXT_PUBLIC_API_ENDPOINT}/main/download/${file.fileUrl}`}
                    download={file.fileName}
                  >
                    <p className="file-upload-file-name">
                      {file.fileName} ({bytesToMB(file.fileSize)}MB)
                    </p>
                    {/* <HiOutlineX /> */}
                  </a>
                </button>
              </List.Item>
            ))}
          </List>
        </div>
      </>
    );
  };

  const Like = () => {
    const likers =
      likes?.users.length > 0
        ? likes?.users.map((user) => `${user?.username}님`).join(", ")
        : "가장 먼저 좋아요를 눌러보세요.";

    return (
      <div className="post-like post-footer-item">
        {likes?.liked ? (
          <button
            onClick={() =>
              onLikeCancelHandler(postContent.url, likes, setLikes, authDto)
            }
          >
            <HiHeart />
          </button>
        ) : (
          <button
            onClick={() =>
              onLikeHandler(postContent.url, likes, setLikes, authDto)
            }
          >
            <HiOutlineHeart />
          </button>
        )}
        <Tooltip content={likers}>
          <span>{`좋아요 ${likes?.likeCount || 0}개`}</span>
        </Tooltip>
      </div>
    );
  };

  const createdAtFormat = getDatetime(postContent.createdAt);
  return (
    <div className="post-container" data-color-mode={isDark ? "dark" : "light"}>
      <header>
        <h1 className="post-title">{postContent.title || "Title"}</h1>
        <div className="post-author">
          By.{postContent.user?.username || "Author"}
        </div>
        <div className="post-category">
          {postContent.category?.name || "Category"}
        </div>
        <div className="post-datetime text-gray-700 dark:text-gray-400">
          <HiCalendar />
          <span>{createdAtFormat}</span>
        </div>
      </header>
      <hr />
      <article>
        <div
          className="post-context ck-content"
          dangerouslySetInnerHTML={{ __html: postContent.content }}
        ></div>
        <div className="post-image-modal">
          <img src={null} alt="fullscreen" />
          <p>클릭하면 이미지가 축소됩니다.</p>
        </div>
      </article>
      <UploadedFiles />
      <Tailcard profile={postContent.user} />
      <hr />
      <footer>
        <Like />
        <div className="post-comment-count post-footer-item">
          <HiOutlineChatAlt2 />
          <span>{`댓글 ${CommentsData.commentCount || 0}개`}</span>
        </div>
      </footer>
    </div>
  );
}

export default Post;
