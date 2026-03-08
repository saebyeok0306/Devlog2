"use client";
import React, { useEffect, useState } from "react";

import "./Category.scss";
import FolderIcon from "@/assets/icons/Folder";
import { useRecoilState, useRecoilValue } from "recoil";
import { get_categories_api } from "@/api/category";
import EditIcon from "@/assets/icons/Edit";
import { Tooltip } from "flowbite-react";
import { categoryAtom, categoryUpdaterAtom } from "@/recoil/categoryAtom";
import { authAtom } from "@/recoil/authAtom";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useTheme } from "next-themes";
import { renderAtom } from "@/recoil/renderAtom";
import { postPagingAtom } from "@/recoil/postPagingAtom";

import { BiSolidCommentError } from "react-icons/bi";

function Category() {
  const navigate = useRouter();
  const pathname = usePathname();
  const authDto = useRecoilValue(authAtom);
  const isRender = useRecoilValue(renderAtom);
  const categoryUpdater = useRecoilValue(categoryUpdaterAtom);
  const { resolvedTheme } = useTheme();
  const isDark = resolvedTheme === "dark";
  const [, setSelectCategory] = useRecoilState(categoryAtom);
  const [page, setPage] = useRecoilState(postPagingAtom);
  const [list, setList] = useState([]); // 렌더될 요소

  useEffect(() => {
    if (isRender) {
      get_categories_api(categoryUpdater)
        .then((res) => {
          setList(res);
        })
        .catch((err) => {
          console.error(err);
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [authDto, categoryUpdater]);

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

  const CategoryEditIcon = () => {
    return (
      <div className="icon">
        <EditIcon
          width="100%"
          height="100%"
          stroke={isDark ? "#fff" : "#000"}
        />
      </div>
    );
  };

  const clickCategoryHandler = (id) => {
    setSelectCategory(id);
    setPage({ ...page, currentPage: 0 });
    if (pathname !== "/") {
      navigate.push("/");
    }
  };

  return (
    <div className="category">
      <p className="title">Category</p>
      <ul>
        <li className="category-all">
          <button
            className="category-item"
            onClick={() => clickCategoryHandler(0)}
          >
            <CategoryIcon />
            <p>전체글보기</p>
          </button>
          {authDto?.role === "ADMIN" ? (
            <Link href="/manager/category">
              <Tooltip content="Edit" style={isDark ? "dark" : "light"}>
                <CategoryEditIcon />
              </Tooltip>
            </Link>
          ) : null}
        </li>
        {list.map((item, idx) => (
          <li key={idx} draggable={false} data-position={idx}>
            <button
              className="category-item"
              onClick={() => clickCategoryHandler(item?.id ? item.id : 0)}
            >
              <CategoryIcon />
              <p>{`${item?.name}`}</p>
              {item?.newPostIcon ? (
                <BiSolidCommentError className="category-new-icon" />
              ) : null}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Category;
