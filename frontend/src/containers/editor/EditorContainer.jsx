import dynamic from "next/dynamic";
import React from "react";

const PostEditor = dynamic(() => import("@/components/editor/postEditor"), {
  ssr: false, // 서버사이드 렌더링 비활성화
});

function EditorContainer() {
  return (
    <>
      <PostEditor />
    </>
  );
}

export default EditorContainer;
