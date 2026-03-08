"use client";
import React from "react";

import "./UserMenu.scss";
import { authAtom } from "@/recoil/authAtom";
import { Dropdown } from "flowbite-react";
import { signOut } from "@/utils/authenticate";
import Link from "next/link";
import { useRecoilState } from "recoil";
import { useRouter } from "next/navigation";

const onProfileHandler = (navigate) => {
  navigate.push("/profile");
};

const onLogoutHandler = (setAuthDto) => {
  signOut(setAuthDto);
};

function UserMenu() {
  const navigate = useRouter();
  // const location = useLocation();
  const [authDto, setAuthDto] = useRecoilState(authAtom);

  if (authDto?.isLogin === false) {
    return (
      <nav className="usermenu button">
        <Link href="/login">
          {" "}
          {/*state={{ backpath: location.pathname }} */}
          로그인
        </Link>
      </nav>
    );
  }

  return (
    <nav className="usermenu">
      <Dropdown label={`${authDto.username}님`} inline>
        <Dropdown.Item onClick={() => onProfileHandler(navigate)}>
          프로필
        </Dropdown.Item>
        {authDto.role === "ADMIN" ? (
          <Dropdown.Item>블로그</Dropdown.Item>
        ) : null}
        <Dropdown.Item onClick={() => onLogoutHandler(setAuthDto)}>
          로그아웃
        </Dropdown.Item>
      </Dropdown>
    </nav>
  );
}

export default UserMenu;
