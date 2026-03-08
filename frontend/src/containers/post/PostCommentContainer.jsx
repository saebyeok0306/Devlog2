"use client";
import { get_post_files_api, get_post_url_api } from "@/api/posts";
import Comment from "@/components/base/comment";
import Post from "@/components/post";
import React, { useEffect, useState } from "react";
import { useRecoilState, useRecoilValue } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import {
  commentAtom,
  COMMENTS_DATA_DEFAULT,
  commentsAtom,
} from "@/recoil/commentAtom";
import { ga4Atom } from "@/recoil/ga4Atom";
import { POST_DEFAULT, postAtom } from "@/recoil/postAtom";
import { sendPageView } from "@/utils/reactGA4";
import { sortComments } from "@/utils/sortComments";
import { useParams, usePathname, useRouter } from "next/navigation";

const resetPostData = async (setPostContent, setComments) => {
  await setPostContent({ ...POST_DEFAULT });
  await setComments({ ...COMMENTS_DATA_DEFAULT });
};

function PostCommentContainer({ ...props }) {
  const navigate = useRouter();
  const location = usePathname();
  const { postUrl } = useParams();
  const [authDto] = useRecoilState(authAtom);
  const initialized = useRecoilValue(ga4Atom);
  const [, setPostContent] = useRecoilState(postAtom);
  const [, setCommentState] = useRecoilState(commentAtom);
  const [, setComments] = useRecoilState(commentsAtom);
  const [likes, setLikes] = useState();

  useEffect(() => {
    const getPost = async () => {
      await resetPostData(setPostContent, setComments);

      try {
        const post = await get_post_url_api(postUrl);
        const postData = post?.post;
        sendPageView(location, postData.title, initialized);

        const files = await get_post_files_api(postData.id);
        postData["files"] = files || [];
        setPostContent(postData);

        setComments((prev) => ({
          ...prev,
          comments: sortComments(post?.comments),
          commentCount: post?.comments.length,
        }));
        setCommentState((prev) => ({
          ...prev,
          commentFlag: post?.commentFlag,
        }));
        setLikes(post?.likes);
      } catch (err) {
        console.log("Failed to get post:", err);
        navigate.push("/");
      }
    };
    getPost();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [postUrl, authDto]);

  return (
    <>
      <Post {...props} likes={likes} setLikes={setLikes} />
      <Comment {...props} />
    </>
  );
}

export default PostCommentContainer;
