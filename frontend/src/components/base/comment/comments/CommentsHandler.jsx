"use client";
import {
  delete_comment_api,
  delete_guest_comment_api,
  edit_comment_api,
  edit_guest_comment_api,
  get_comment_files_api,
  upload_comment_api,
  upload_guest_comment_api,
} from "@/api/comment";
import { toast } from "react-toastify";

const uploadReplyHandler = async ({
  postContent,
  comments,
  reply,
  setReply,
  content,
  files,
}) => {
  try {
    await upload_comment_api(
      postContent,
      reply.target.id,
      content, // reply.content,
      files,
      reply.target.hidden ? reply.target.hidden : reply.hidden
    );
    await cancelEditHandler({ reply: reply, setReply: setReply });

    toast.info("댓글이 등록되었습니다.");
  } catch (err) {
    toast.error("댓글 등록 중 오류가 발생했습니다.");
    console.error(err);
  }
  return false;
};

const uploadCommentHandler = async ({
  postContent,
  comments,
  editorComment,
  setEditorComment,
  content,
  files,
}) => {
  try {
    await upload_comment_api(
      postContent,
      0,
      content, // editorComment.content,
      files, // editorComment.files,
      editorComment.hidden
    );
    // const comment = result.data;
    // comments.push(comment);
    setEditorComment({
      ...editorComment,
      content: "",
      hidden: false,
    });

    toast.info("댓글이 등록되었습니다.");
    return true;
  } catch (err) {
    return false;
  }
};

const onReplyHandler = async ({
  targetComment,
  reply,
  setReply,
  setCommentFiles,
}) => {
  let root = targetComment?.root;
  if (!root) root = targetComment.id;
  if (reply.editId !== root) {
    await setReply({
      ...reply,
      flag: true,
      editId: root,
      target: targetComment,
      content: "",
    });
  } else {
    await setReply({ ...reply, flag: true, target: targetComment });
  }
  await setCommentFiles([]);
};

const onEditHandler = async ({ comment, reply, setReply, setCommentFiles }) => {
  await setReply({
    ...reply,
    flag: false,
    editId: comment.id,
    content: comment.content,
    hidden: comment.hidden,
  });
  try {
    const payload = await get_comment_files_api(comment.id);
    await setCommentFiles(payload);
  } catch (err) {
    await setCommentFiles([]);
    toast.error("댓글 파일을 불러오는데 실패했습니다.");
    console.error("Failed to get comment files:", err);
  }
};

const cancelEditHandler = async ({ reply, setReply }) => {
  await setReply({
    ...reply,
    flag: false,
    editId: null,
    target: null,
    hidden: false,
  });
};

const updateEditHandler = async ({
  comment,
  comments,
  reply,
  setReply,
  content,
  files,
}) => {
  const res = await edit_comment_api(comment.id, content, files, reply.hidden)
    .then(() => {
      return true;
    })
    .catch((err) => {
      console.error(err);
      return false;
    });

  if (res) {
    toast.info("댓글이 수정되었습니다.");
    await cancelEditHandler({ reply: reply, setReply: setReply });
  }
  // CommentEditor의 onSave에서 true이면, 에디터 내용을 초기화하는 코드가 작동함.
  // 그러면, 에디터의 상태도 변화하면서 cancel을 위한 코드가 제대로 작동하지 않음.
  return false;
};

const deleteCommentHandler = async ({ comment, setUpdater }) => {
  try {
    await delete_comment_api(comment.id);
    await setUpdater((prev) => prev + 1);
    return true;
  } catch (err) {
    return false;
  }
};

const uploadGuestCommentHandler = async ({
  postContent,
  comments,
  editorComment,
  setEditorComment,
  content,
  files,
  username,
  password,
  verify,
}) => {
  if (!username || !password) {
    toast.error("닉네임과 비밀번호를 입력해주세요.");
    return false;
  }
  if (verify === false) {
    toast.error("캡차를 인증해주세요.");
    return false;
  }
  try {
    await upload_guest_comment_api(
      postContent,
      0,
      content, // editorComment.content,
      false,
      username,
      password
    );
    setEditorComment({
      ...editorComment,
      content: "",
      hidden: false,
    });
    toast.info("댓글이 등록되었습니다.");
    return true;
  } catch (err) {
    return false;
  }
};

const updateGuestEditHandler = async ({
  comment,
  comments,
  reply,
  setReply,
  content,
  password,
  verify,
}) => {
  if (!password) {
    toast.error("비밀번호를 입력해주세요.");
    return false;
  }
  if (verify === false) {
    toast.error("캡차를 인증해주세요.");
    return false;
  }
  const res = await edit_guest_comment_api(comment.id, content, false, password)
    .then(() => {
      return true;
    })
    .catch(() => {
      return false;
    });
  if (res) {
    await cancelEditHandler({ reply: reply, setReply: setReply });
    toast.info("댓글이 수정되었습니다.");
  } else {
    toast.error("비밀번호가 일치하지 않습니다.");
  }
  // CommentEditor의 onSave에서 true이면, 에디터 내용을 초기화하는 코드가 작동함.
  // 그러면, 에디터의 상태도 변화하면서 cancel을 위한 코드가 제대로 작동하지 않음.
  return false;
};

const uploadGuestReplyHandler = async ({
  postContent,
  comments,
  reply,
  setReply,
  content,
  files,
  username,
  password,
  verify,
}) => {
  try {
    if (!username || !password) {
      toast.error("닉네임과 비밀번호를 입력해주세요.");
      return false;
    }
    if (verify === false) {
      toast.error("캡차를 인증해주세요.");
      return false;
    }
    await upload_guest_comment_api(
      postContent,
      reply.target.id,
      content, // reply.content,
      false, // reply.target.hidden ? reply.target.hidden : reply.hidden
      username,
      password
    );
    await cancelEditHandler({ reply: reply, setReply: setReply });

    toast.info("댓글이 등록되었습니다.");
  } catch (err) {
    toast.error("댓글 등록 중 오류가 발생했습니다.");
    console.error(err);
  }
  return false;
};

const deleteGuestCommentHandler = async ({ comment, password, setUpdater }) => {
  if (!password) {
    toast.error("비밀번호를 입력해주세요.");
    return false;
  }
  try {
    await delete_guest_comment_api(comment.id, password);
    await setUpdater((prev) => prev + 1);
    return true;
  } catch (err) {
    return false;
  }
};

export {
  uploadReplyHandler,
  uploadCommentHandler,
  onReplyHandler,
  onEditHandler,
  cancelEditHandler,
  updateEditHandler,
  deleteCommentHandler,
  uploadGuestCommentHandler,
  updateGuestEditHandler,
  uploadGuestReplyHandler,
  deleteGuestCommentHandler,
};
