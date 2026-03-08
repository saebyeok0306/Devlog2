"use client";

import React from "react";
import Responsive from "@/components/common/Responsive";

import { BLOG_NAME } from "@/constants/base/main";
import "./Header.scss";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useRecoilState } from "recoil";
import { categoryAtom } from "@/recoil/categoryAtom";

function Header({ right, menu }) {
  const navigate = useRouter();
  const [, setSelectCategory] = useRecoilState(categoryAtom);

  const refreshHomeHandler = (e) => {
    e.preventDefault();
    setSelectCategory(0);
    navigate.push("/");
  };

  return (
    <div className={`header`}>
      <Responsive>
        <div className="header-menu">
          <Link
            className="brand text-black dark:text-white"
            href="/"
            onClick={(e) => refreshHomeHandler(e)}
          >
            {BLOG_NAME}
          </Link>
          {/* 여기에 넣으면 중간에 컴포넌트 삽입 */}
          <div className="header-right">
            {right}
            {menu}
          </div>
        </div>
      </Responsive>
    </div>
  );
}

export default Header;
