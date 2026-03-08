"use client";
import React from "react";

import "./EditCategoryModal.scss";
import { Button, Dropdown, Modal, TextInput } from "flowbite-react";
import { ROLE_TYPE } from "@/utils/RoleType";

const authNameList = [
  ["writePostAuth", "글쓰기 권한"],
  ["readCategoryAuth", "글읽기 권한"],
  ["writeCommentAuth", "댓글쓰기 권한"],
];

function DropdownAuthority({
  item,
  authName,
  labelName,
  setModalCategoryItem,
}) {
  const setAuthHandler = (idx) => {
    const newItem = { ...item.data };
    newItem[authName] = Object.keys(ROLE_TYPE)[idx];
    setModalCategoryItem({ ...item, data: newItem });
  };

  return (
    <div className="dropdown-auth">
      <label>{labelName}</label>
      <Dropdown label={`${item.data[authName]}`} inline>
        {Object.keys(ROLE_TYPE).map((role, idx) => (
          <Dropdown.Item key={idx} onClick={() => setAuthHandler(idx)}>
            {role}
          </Dropdown.Item>
        ))}
      </Dropdown>
    </div>
  );
}

function saveCategoryItemHandler(
  item,
  categoryName,
  list,
  setList,
  setOpenModal
) {
  const newData = { ...item.data, name: categoryName };
  const newList = [...list];
  newList[item.index] = newData;
  setList(newList);
  closeHandler(setOpenModal);
}

function categoryNameHandler(e, setCategoryName) {
  setCategoryName(e.target.value);
}

function closeHandler(setOpenModal) {
  setOpenModal(false);
}

function EditCategoryModal({
  openModal,
  setOpenModal,
  modalCategoryItem,
  setModalCategoryItem,
  modalCategoryName,
  setModalCategoryName,
  list,
  setList,
}) {
  if (modalCategoryItem === null) {
    return <></>;
  }
  return (
    <>
      <Modal
        show={openModal}
        onClose={() => closeHandler(setOpenModal)}
        size="md"
        className="edit-category-modal"
        // popup
      >
        <Modal.Header>{modalCategoryItem.data.name}</Modal.Header>
        <Modal.Body>
          <div className="edit-category-modal-body">
            <TextInput
              className="edit-category-modal-body-title"
              value={modalCategoryName || ""}
              onChange={(e) => categoryNameHandler(e, setModalCategoryName)}
            />
            {authNameList.map((authName, idx) => (
              <DropdownAuthority
                key={idx}
                item={modalCategoryItem}
                authName={authName[0]}
                labelName={authName[1]}
                setModalCategoryItem={setModalCategoryItem}
              />
            ))}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            onClick={() =>
              saveCategoryItemHandler(
                modalCategoryItem,
                modalCategoryName,
                list,
                setList,
                setOpenModal
              )
            }
          >
            확인
          </Button>
          <Button color="gray" onClick={() => closeHandler(setOpenModal)}>
            취소
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default EditCategoryModal;
