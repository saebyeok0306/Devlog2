import "./DeleteModal.scss";
import { Button, Label, Modal, TextInput } from "flowbite-react";
import React, { useState } from "react";
import { RiLockPasswordLine } from "react-icons/ri";
import {
  deleteCommentHandler,
  deleteGuestCommentHandler,
} from "@/components/base/comment/comments/CommentsHandler";

function closeHandler(setDeletePopup, setPassword) {
  setPassword("");
  setDeletePopup((prev) => ({ ...prev, openModal: false, target: null }));
}

async function onDeleteCommentHandler({
  comment,
  password,
  setDeletePopup,
  setPassword,
  setUpdater,
}) {
  let result = false;
  if (comment.user.email != null) {
    result = await deleteCommentHandler({
      comment: comment,
      setUpdater: setUpdater,
    });
  } else {
    result = await deleteGuestCommentHandler({
      comment: comment,
      password: password,
      setUpdater: setUpdater,
    });
  }
  if (result) {
    closeHandler(setDeletePopup, setPassword);
  }
}

function DeleteModal({ deletePopup, setDeletePopup, setUpdater }) {
  const [password, setPassword] = useState("");
  return (
    <>
      <Modal
        show={deletePopup.openModal}
        onClose={() => closeHandler(setDeletePopup, setPassword)}
        size="md"
        className="comment-delete-modal"
      >
        <Modal.Header>해당 댓글을 삭제하시겠습니까?</Modal.Header>
        <Modal.Body>
          <div className="delete-modal-body h-[100px]">
            {deletePopup.target ? (
              <div
                className="ck-content delete-modal-content"
                dangerouslySetInnerHTML={{ __html: deletePopup.target.content }}
              ></div>
            ) : null}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            color="red"
            onClick={() =>
              onDeleteCommentHandler({
                comment: deletePopup.target,
                password: password,
                setDeletePopup: setDeletePopup,
                setPassword: setPassword,
                setUpdater: setUpdater,
              })
            }
          >
            삭제
          </Button>
          <Button
            color="gray"
            onClick={() => closeHandler(setDeletePopup, setPassword)}
          >
            취소
          </Button>
          {deletePopup.target?.user.email == null ? (
            <TextInput
              id="password"
              className=""
              placeholder="비밀번호를 입력해주세요."
              type="password"
              onChange={(e) => setPassword(e.target.value)}
              color="success"
            />
          ) : null}
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default DeleteModal;
