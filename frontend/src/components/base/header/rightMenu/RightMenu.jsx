"use client";
import React from "react";

import "./RightMenu.scss";
import { authAtom } from "@/recoil/authAtom";
import { useRecoilValue } from "recoil";
import { Dropdown } from "flowbite-react";
import DarkIcon from "@/assets/icons/Dark";
import LightIcon from "@/assets/icons/Light";
import ComputerIcon from "@/assets/icons/Computer";
import DotIcon from "@/assets/icons/Dot";
import Link from "next/link";
import { useTheme } from "next-themes";

function RightMenu() {
  const { theme, setTheme, resolvedTheme } = useTheme();
  const authDto = useRecoilValue(authAtom);
  const isDark = resolvedTheme == "dark";

  const fill = () => (isDark ? "#fff" : "#000");

  const Light = (prop) => {
    return <LightIcon width="1.4em" height="1.4em" fill={fill()} {...prop} />;
  };

  const Dark = (prop) => {
    return <DarkIcon width="1.4em" height="1.4em" stroke={fill()} {...prop} />;
  };

  const System = (prop) => {
    return (
      <ComputerIcon width="1.4em" height="1.4em" fill={fill()} {...prop} />
    );
  };

  const Dot = () => {
    return <DotIcon width="25px" height="25px" fill={fill()} />;
  };

  const DarkModeIcon = () => {
    return isDark ? <Dark /> : <Light />;
  };

  const SetThemeSetting = (flag) => {
    setTheme(flag);
  };

  const CommonMenu = () => {
    return (
      <>
        <nav>검색메뉴</nav>
        <nav>
          <Dropdown
            className="dropdown"
            label=""
            inline
            renderTrigger={() => (
              <button>
                <DarkModeIcon />
              </button>
            )}
          >
            <Dropdown.Item onClick={() => SetThemeSetting("light")}>
              <div>
                <Light style={{ marginRight: "0.5em" }} />
                Light
              </div>
              {theme === "light" ? <Dot /> : null}
            </Dropdown.Item>
            <Dropdown.Item onClick={() => SetThemeSetting("dark")}>
              <div>
                <Dark style={{ marginRight: "0.5em" }} />
                Dark
              </div>
              {theme === "dark" ? <Dot /> : null}
            </Dropdown.Item>
            <Dropdown.Item onClick={() => SetThemeSetting("system")}>
              <div>
                <System style={{ marginRight: "0.5em" }} />
                System
              </div>
              {theme === "system" ? <Dot /> : null}
            </Dropdown.Item>
          </Dropdown>
        </nav>
      </>
    );
  };

  const NewPost = () => {
    return (
      <nav>
        <Link href="/editor">새글쓰기</Link>
      </nav>
    );
  };

  if (authDto?.isLogin === true) {
    return (
      <div className="rightmenu">
        <CommonMenu />
        <NewPost />
      </div>
    );
  }

  return (
    <div className="rightmenu">
      <CommonMenu />
    </div>
  );
}

export default RightMenu;
