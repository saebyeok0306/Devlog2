"use client";
import { Dropdown, Timeline } from "flowbite-react";
import { useRecoilState, useRecoilValue } from "recoil";
import { replyTimelineTheme } from "./replyTimelineTheme";
import { getDatetime } from "@/utils/getDatetime";

import { HiAnnotation } from "react-icons/hi";
import {
  cancelEditHandler,
  onEditHandler,
  onReplyHandler,
  updateEditHandler,
  updateGuestEditHandler,
  uploadGuestReplyHandler,
  uploadReplyHandler,
} from "./CommentsHandler";
import { authAtom } from "@/recoil/authAtom";
import { commentAtom, commentFilesAtom } from "@/recoil/commentAtom";
import { postAtom } from "@/recoil/postAtom";
import dynamic from "next/dynamic";
import { useRef, useState } from "react";
import GuestCommentEditor from "@/components/editor/commentEditor/GuestCommentEditor";

const CommentEditor = dynamic(
  () => import("@/components/editor/commentEditor"),
  {
    ssr: false, // 서버사이드 렌더링 비활성화
  }
);

const CommentReply = ({
  rootComment,
  comments,
  reply,
  setReply,
  setUpdater,
}) => {
  const authDto = useRecoilValue(authAtom);
  const postContent = useRecoilValue(postAtom);

  const captchaRef = useRef(null);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [verify, setVerify] = useState(false);

  return (
    <Timeline.Item className={"comment create-reply"}>
      <Timeline.Point icon={HiAnnotation} />
      <Timeline.Content>
        <Timeline.Time className="comment-user">
          <div>{`${authDto.username || username}님`}</div>
        </Timeline.Time>
        <Timeline.Body className="comment-content">
          <div className="comment-content-body">
            <p className="comment-reply-name">@{reply.target.user.username}</p>
          </div>
          {authDto.isLogin ? (
            <CommentEditor
              allowHidden={true}
              comment={reply}
              setComment={setReply}
              onCancel={() =>
                cancelEditHandler({
                  reply: reply,
                  setReply: setReply,
                })
              }
              onSave={(content, files) =>
                uploadReplyHandler({
                  postContent: postContent,
                  comments: comments,
                  reply: reply,
                  setReply: setReply,
                  content: content,
                  files: files,
                })
              }
              setUpdater={setUpdater}
            />
          ) : (
            <GuestCommentEditor
              comment={reply}
              setComment={setReply}
              onCancel={() =>
                cancelEditHandler({
                  reply: reply,
                  setReply: setReply,
                })
              }
              onSave={(content, files) =>
                uploadGuestReplyHandler({
                  postContent: postContent,
                  comments: comments,
                  reply: reply,
                  setReply: setReply,
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
          )}
        </Timeline.Body>
      </Timeline.Content>
    </Timeline.Item>
  );
};

const CommentBox = ({
  comment,
  comments,
  reply,
  setReply,
  setUpdater,
  setDeletePopup,
}) => {
  const isReply = comment.parent ? true : false;

  return (
    <Timeline.Item className={isReply ? "comment reply" : "comment"}>
      <Timeline.Point
        icon={HiAnnotation}
        theme={isReply ? replyTimelineTheme : null}
      >
        {isReply ? (
          <div className="comment-reply-line">
            <div className="border-t border-gray-200 dark:border-gray-700 top-3 -left-6" />
          </div>
        ) : null}
      </Timeline.Point>
      <Timeline.Content>
        {reply.flag === false && reply.editId === comment.id ? (
          <CommentEdit
            comment={comment}
            comments={comments}
            reply={reply}
            setReply={setReply}
            setUpdater={setUpdater}
          />
        ) : (
          <CommentView
            comment={comment}
            comments={comments}
            reply={reply}
            setReply={setReply}
            setUpdater={setUpdater}
            setDeletePopup={setDeletePopup}
          />
        )}
      </Timeline.Content>
    </Timeline.Item>
  );
};

const CommentView = ({
  comment,
  comments,
  reply,
  setReply,
  setUpdater,
  setDeletePopup,
}) => {
  const authDto = useRecoilValue(authAtom);
  const commentState = useRecoilValue(commentAtom);
  const [, setCommentFiles] = useRecoilState(commentFilesAtom);

  const onDeleteHandler = () => {
    setDeletePopup((prev) => ({ ...prev, openModal: true, target: comment }));
  };

  const CommentToolbar = () => {
    if (
      commentState.commentFlag === true &&
      comment.ownership &&
      !comment.deleted
    ) {
      return (
        <>
          <Dropdown label="" inline>
            <Dropdown.Item
              onClick={() =>
                onEditHandler({
                  comment: comment,
                  reply: reply,
                  setReply: setReply,
                  setCommentFiles: setCommentFiles,
                })
              }
            >
              수정
            </Dropdown.Item>
            <Dropdown.Item onClick={onDeleteHandler}>삭제</Dropdown.Item>
          </Dropdown>
        </>
      );
    }
    return null;
  };
  return (
    <>
      <Timeline.Time className="comment-user">
        {!comment.deleted ? <div>{`${comment.user.username}님`}</div> : null}
        <div className="text-gray-900 dark:text-gray-400 comment-menu">
          <CommentToolbar />
        </div>
      </Timeline.Time>
      <Timeline.Body className="comment-content">
        <div className="comment-content-body">
          {comment?.parent ? (
            <p className="comment-reply-name">
              @
              {comment.parentData.deleted
                ? "삭제됨"
                : comment.parentData.user.username}
            </p>
          ) : null}
          {/* {comment.content} */}
          {!comment.deleted ? (
            <div className="comment-md-content">
              {comment.hidden && !comment.hiddenFlag ? (
                <span className="text-[0.8rem] text-gray-600 dark:text-gray-500 select-none">
                  [비밀 댓글입니다]
                </span>
              ) : null}
              <div
                className="ck-content"
                dangerouslySetInnerHTML={{ __html: comment.content }}
              ></div>
            </div>
          ) : (
            <div className="text-gray-700 dark:text-gray-400">
              삭제된 댓글입니다.
            </div>
          )}
        </div>
        <div className="comment-content-footer">
          <time dateTime={comment.createdAt}>
            {getDatetime(comment.createdAt)}
          </time>
          {commentState.commentFlag === true && !comment.deleted ? (
            <button
              onClick={() =>
                onReplyHandler({
                  targetComment: comment,
                  reply: reply,
                  setReply: setReply,
                  setCommentFiles: setCommentFiles,
                })
              }
            >
              답글
            </button>
          ) : null}
        </div>
      </Timeline.Body>
    </>
  );
};

const CommentEdit = ({ comment, comments, reply, setReply, setUpdater }) => {
  const captchaRef = useRef(null);
  const [password, setPassword] = useState("");
  const [verify, setVerify] = useState(false);

  if (comment.user.email != null) {
    return (
      <CommentEditor
        allowHidden={true}
        comment={reply}
        setComment={setReply}
        onCancel={() =>
          cancelEditHandler({
            reply: reply,
            setReply: setReply,
          })
        }
        onSave={(content, files) =>
          updateEditHandler({
            comment: comment,
            comments: comments,
            reply: reply,
            setReply: setReply,
            content: content,
            files: files,
            verify: verify,
          })
        }
        setUpdater={setUpdater}
      />
    );
  }
  return (
    <GuestCommentEditor
      comment={reply}
      setComment={setReply}
      onCancel={() =>
        cancelEditHandler({
          reply: reply,
          setReply: setReply,
        })
      }
      onSave={(content, files) =>
        updateGuestEditHandler({
          comment: comment,
          comments: comments,
          reply: reply,
          setReply: setReply,
          content: content,
          password: password,
        })
      }
      setUpdater={setUpdater}
      setPassword={setPassword}
      captchaRef={captchaRef}
      setVerify={setVerify}
    />
  );
};

const Comments = ({
  comments,
  reply,
  setReply,
  setUpdater,
  setDeletePopup,
}) => {
  if (!comments) {
    return null;
  }
  return (
    <>
      {comments.map((comment, idx) => (
        <div key={idx}>
          <CommentBox
            comment={comment}
            comments={comments}
            reply={reply}
            setReply={setReply}
            setUpdater={setUpdater}
            setDeletePopup={setDeletePopup}
          />
          {comment.children &&
            comment.children.map((child, idx2) => (
              <CommentBox
                key={idx2 + 10000}
                comment={child}
                comments={comments}
                reply={reply}
                setReply={setReply}
                setUpdater={setUpdater}
                setDeletePopup={setDeletePopup}
              />
            ))}
          {reply.flag === true && reply.editId === comment.id ? (
            <CommentReply
              rootComment={comment}
              comments={comments}
              reply={reply}
              setReply={setReply}
              setUpdater={setUpdater}
            />
          ) : null}
        </div>
      ))}
    </>
  );
};

export default Comments;
