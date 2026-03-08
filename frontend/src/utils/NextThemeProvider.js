"use client";
import { ThemeProvider } from "next-themes";
import { useRecoilValue } from "recoil";
import { renderAtom } from "@/recoil/renderAtom";
import Initializer from "@/utils/Initializer";

export default function NextThemeProvider({ children, ...props }) {
  const render = useRecoilValue(renderAtom);

  // 블로그 최초 접속시 초기화 함수
  Initializer();

  // TODO: 나중에는 google bot인 경우를 따로 판단해서 화면 표시하기.
  if (!render) {
    return <>{children}</>;
    // return <></>;
  } // for persistent theme page.

  return (
    <ThemeProvider storageKey={"theme"} attribute="class" {...props}>
      {children}
    </ThemeProvider>
  );
}
