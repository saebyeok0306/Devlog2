"use client";
import React, { useEffect, useRef, useState } from "react";

import "./Comment.scss";
import { Timeline } from "flowbite-react";
import { timelineCustomTheme } from "@/styles/theme/timeline";
import { useRecoilState, useRecoilValue } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import { postAtom } from "@/recoil/postAtom";
import {
  commentAtom,
  COMMENTS_DATA_DEFAULT,
  commentsAtom,
} from "@/recoil/commentAtom";
import Comments from "./comments/Comments";
import {
  uploadCommentHandler,
  uploadGuestCommentHandler,
} from "./comments/CommentsHandler";
import { get_comments_by_post_api } from "@/api/comment";
import { sortComments } from "@/utils/sortComments";
import { toast } from "react-toastify";
import dynamic from "next/dynamic";
import GuestCommentEditor from "@/components/editor/commentEditor/GuestCommentEditor";
import DeleteModal from "@/components/base/comment/modal";

const CommentEditor = dynamic(
  () => import("@/components/editor/commentEditor"),
  {
    ssr: false, // 서버사이드 렌더링 비활성화
  }
);

export const ShowCommentEditor = ({
  authDto,
  editorComment,
  setEditorComment,
  postContent,
  commentsData,
  setUpdater,
}) => {
  const captchaRef = useRef(null);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [verify, setVerify] = useState(false);

  if (authDto.isLogin) {
    return (
      <CommentEditor
        allowHidden={true}
        comment={editorComment}
        setComment={setEditorComment}
        onSave={(content, files) =>
          uploadCommentHandler({
            postContent: postContent,
            comments: commentsData.comments,
            editorComment: editorComment,
            setEditorComment: setEditorComment,
            content: content,
            files: files,
          })
        }
        setUpdater={setUpdater}
      />
    );
  }
  return (
    <GuestCommentEditor
      comment={editorComment}
      setComment={setEditorComment}
      onSave={(content, files) =>
        uploadGuestCommentHandler({
          postContent: postContent,
          comments: commentsData.comments,
          editorComment: editorComment,
          setEditorComment: setEditorComment,
          content: content,
          files: files,
          username: username,
          password: password,
          verify: verify,
        })
      }
      setUpdater={setUpdater}
      setUsername={setUsername}
      setPassword={setPassword}
      captchaRef={captchaRef}
      setVerify={setVerify}
    />
  );
};

function Comment({ ...props }) {
  const { commentRef } = props;
  const authDto = useRecoilValue(authAtom);
  const [updater, setUpdater] = useState(0);
  const [reply, setReply] = useState({
    flag: false, // true: 답글 작성 false: 편집
    editId: null, // 편집 중인 댓글 id
    target: null, // 답글 대상
    content: "",
    hidden: false,
  });
  const postContent = useRecoilValue(postAtom);
  const commentState = useRecoilValue(commentAtom);
  const [commentsData, setCommentsData] = useRecoilState(commentsAtom);
  const [editorComment, setEditorComment] = useState({
    content: "",
    hidden: false,
  });
  const [deletePopup, setDeletePopup] = useState({
    openModal: false,
    target: null,
  });

  useEffect(() => {
    if (updater < 1) return;
    get_comments_by_post_api(postContent.id)
      .then((res) => {
        const sortedComments = sortComments(res);
        setCommentsData((prev) => ({
          ...prev,
          comments: sortedComments,
          commentCount: res?.length,
        }));
      })
      .catch((error) => {
        toast.error("댓글을 불러오는데 실패했습니다.");
        console.error("Failed to get comments:", error);
        setCommentsData({ ...COMMENTS_DATA_DEFAULT });
      });
    // eslint-disable-next-line
  }, [updater]);

  return (
    <div ref={commentRef} className="comment-container">
      <DeleteModal
        deletePopup={deletePopup}
        setDeletePopup={setDeletePopup}
        setUpdater={setUpdater}
      />
      <Timeline className="comments" theme={timelineCustomTheme}>
        <Comments
          comments={commentsData.comments}
          reply={reply}
          setReply={setReply}
          setUpdater={setUpdater}
          setDeletePopup={setDeletePopup}
        />
      </Timeline>
      {/* Post Comment Editor */}
      {reply.editId || !commentState.commentFlag ? null : (
        <ShowCommentEditor
          reply={reply}
          setEditorComment={setEditorComment}
          editorComment={editorComment}
          commentsData={commentsData}
          commentState={commentState}
          authDto={authDto}
          postContent={postContent}
          setUpdater={setUpdater}
        />
      )}
    </div>
  );
}

export default Comment;
