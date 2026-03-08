"use client";
import React, { useEffect, useState } from "react";

import "./EditCategory.scss";
import FolderIcon from "@/assets/icons/Folder";
import { useRecoilState } from "recoil";
import { get_categories_detail_api, set_categories_api } from "@/api/category";
import { toast } from "react-toastify";
import { Button, Checkbox, Table } from "flowbite-react";
import { CategoryDetail } from "@/model/CategoryDetail";
import EditCategoryModal from "./modal/EditCategoryModal";
import { categoryUpdaterAtom } from "@/recoil/categoryAtom";
import { useRouter } from "next/navigation";
import { useTheme } from "next-themes";

function allCheckCategoryHandler(allChecked, setAllChecked, setCheckedList) {
  if (!allChecked) {
    // checkedList를 모두 true로 바꿈
    setCheckedList((prev) => {
      return prev.map(() => true);
    });
    setAllChecked(true);
  } else {
    // checkedList를 모두 false로 바꿈
    setCheckedList((prev) => {
      return prev.map(() => false);
    });
    setAllChecked(false);
  }
}

function checkCategoryHandler(idx, setAllChecked, checkedList, setCheckedList) {
  const newList = [...checkedList];
  newList[idx] = !newList[idx]; // 해당 인덱스의 값을 토글
  setCheckedList(newList);
  if (newList.every((v) => v === true)) {
    setAllChecked(true);
  } else if (newList.every((v) => v === false)) {
    setAllChecked(false);
  }
}

function addCategoryHandler(list, setList, setCheckedList) {
  const newCategory = new CategoryDetail({ layer: list.length + 1 });
  setList([...list, newCategory.toObject()]);
  setCheckedList((prev) => [...prev, false]);
}

function removeCategoryHandler(list, setList, checkedList, setCheckedList) {
  if (checkedList.every((v) => v === false)) {
    toast.error("삭제할 카테고리를 선택해주세요.");
  }
  const newList = list.filter((item, idx) => !checkedList[idx]);
  for (let i = 0; i < newList.length; i++) {
    newList[i].layer = i + 1;
  }
  setList(newList);
  setCheckedList((prev) => prev.filter((item) => !item));
}

async function saveCategoryHandler(list, setCategoryUpdater, navigate) {
  try {
    await set_categories_api(list);
    toast.info("카테고리가 저장되었습니다.");
    setCategoryUpdater((prev) => prev + 1);
  } catch (err) {
    toast.error(`${err.response?.data ? err.response.data.error : err}`);
  }
}

function openEditCategoryModalHandler(
  index,
  categoryItem,
  setOpenModal,
  setModalCategoryItem,
  setModalCategoryName
) {
  setOpenModal(true);
  setModalCategoryItem({ index: index, data: categoryItem });
  setModalCategoryName(categoryItem.name);
}

function layerSort(list) {
  for (let i = 0; i < list.length; i++) {
    list[i].layer = i + 1;
  }
  return list;
}

function arrowUpHandler(checkedList, setCheckedList, list, setList) {
  const newList = [...list];
  const newCheckedList = [...checkedList];

  for (let i = 1; i < newList.length; i++) {
    if (newCheckedList[i] && !newCheckedList[i - 1]) {
      // Swap the items in list
      [newList[i - 1], newList[i]] = [newList[i], newList[i - 1]];
      // Swap the items in checkedList
      [newCheckedList[i - 1], newCheckedList[i]] = [
        newCheckedList[i],
        newCheckedList[i - 1],
      ];
    }
  }

  setList(layerSort(newList));
  setCheckedList(newCheckedList);
}

function arrowDownHandler(checkedList, setCheckedList, list, setList) {
  const newList = [...list];
  const newCheckedList = [...checkedList];

  for (let i = newList.length - 2; i >= 0; i--) {
    if (newCheckedList[i] && !newCheckedList[i + 1]) {
      // Swap the items in list
      [newList[i + 1], newList[i]] = [newList[i], newList[i + 1]];
      // Swap the items in checkedList
      [newCheckedList[i + 1], newCheckedList[i]] = [
        newCheckedList[i],
        newCheckedList[i + 1],
      ];
    }
  }

  setList(layerSort(newList));
  setCheckedList(newCheckedList);
}

function EditCategory() {
  // Reference https://romantech.net/1118?category=954568
  const navigate = useRouter();
  const { resolvedTheme } = useTheme();
  const isDark = resolvedTheme == "dark";
  const [categoryUpdater, setCategoryUpdater] =
    useRecoilState(categoryUpdaterAtom);
  const [list, setList] = useState([]); // 렌더될 요소
  const [allChecked, setAllChecked] = useState(false); // 전체 체크 여부
  const [checkedList, setCheckedList] = useState([]);

  const [openModal, setOpenModal] = useState(false);
  const [modalCategoryItem, setModalCategoryItem] = useState(null);
  const [modalCategoryName, setModalCategoryName] = useState("");

  useEffect(() => {
    get_categories_detail();
  }, [categoryUpdater]);

  const get_categories_detail = () => {
    get_categories_detail_api()
      .then((res) => {
        setList(res);
        const newList = Array(res.length).fill(false);
        setCheckedList(newList);
        setAllChecked(false);
      })
      .catch((err) => {
        toast.error(`${err.response?.data ? err.response.data.error : err}`);
      });
  };

  const CategoryIcon = () => {
    return (
      <div className="icon">
        <FolderIcon
          width="100%"
          height="100%"
          fill={isDark ? "#fff" : "#000"}
        />
      </div>
    );
  };

  return (
    <div className="edit-category">
      <EditCategoryModal
        openModal={openModal}
        setOpenModal={setOpenModal}
        modalCategoryItem={modalCategoryItem}
        setModalCategoryItem={setModalCategoryItem}
        modalCategoryName={modalCategoryName}
        setModalCategoryName={setModalCategoryName}
        list={list}
        setList={setList}
      />
      <p className="title">Category Manager</p>
      <Table hoverable>
        <Table.Head>
          <Table.HeadCell className="p-4">
            <Checkbox
              checked={allChecked || false}
              onChange={(e) =>
                allCheckCategoryHandler(
                  allChecked,
                  setAllChecked,
                  setCheckedList
                )
              }
            />
          </Table.HeadCell>
          {/* <Table.HeadCell className="p-4">Layer</Table.HeadCell> */}
          <Table.HeadCell className="w-full">Category</Table.HeadCell>
        </Table.Head>
        <Table.Body className="divide-y">
          <Table.Row className="edit-category-item-all">
            <Table.Cell className="p-4"></Table.Cell>
            {/* <Table.Cell></Table.Cell> */}
            <Table.Cell className="edit-category-item-name">
              <CategoryIcon />
              <p>전체글보기</p>
            </Table.Cell>
          </Table.Row>
          {list.map((item, idx) => (
            <Table.Row key={idx} data-position={idx}>
              <Table.Cell className="p-4">
                <Checkbox
                  checked={checkedList[idx] || false}
                  onChange={(e) =>
                    checkCategoryHandler(
                      idx,
                      setAllChecked,
                      checkedList,
                      setCheckedList
                    )
                  }
                />
              </Table.Cell>
              {/* <Table.Cell className="edit-category-item-layer p-4">
                <button onClick={() => arrowUpHandler(idx, list, setList)}>
                  ↑
                </button>
                /
                <button onClick={() => arrowDownHandler(idx, list, setList)}>
                  ↓
                </button>
              </Table.Cell> */}
              <Table.Cell>
                <button
                  className="edit-category-item-name font-medium text-cyan-600 hover:underline dark:text-cyan-500"
                  onClick={() =>
                    openEditCategoryModalHandler(
                      idx,
                      item,
                      setOpenModal,
                      setModalCategoryItem,
                      setModalCategoryName
                    )
                  }
                >
                  <CategoryIcon />
                  <p>{`${item?.name}`}</p>
                </button>
              </Table.Cell>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
      <div className="edit-category-bottom">
        <div>
          <Button
            color="gray"
            size="sm"
            onClick={() => addCategoryHandler(list, setList, setCheckedList)}
          >
            추가
          </Button>
          <Button
            color="gray"
            size="sm"
            onClick={() =>
              removeCategoryHandler(list, setList, checkedList, setCheckedList)
            }
          >
            삭제
          </Button>
          <Button
            color="gray"
            size="sm"
            onClick={() =>
              arrowUpHandler(checkedList, setCheckedList, list, setList)
            }
          >
            위로
          </Button>
          <Button
            color="gray"
            size="sm"
            onClick={() =>
              arrowDownHandler(checkedList, setCheckedList, list, setList)
            }
          >
            아래로
          </Button>
        </div>
        <div>
          <Button
            color="success"
            size="sm"
            onClick={() =>
              saveCategoryHandler(list, setCategoryUpdater, navigate)
            }
          >
            저장
          </Button>
        </div>
      </div>
    </div>
  );
}

export default EditCategory;
