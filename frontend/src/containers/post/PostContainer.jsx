"use client";
import Main from "@/components/common/main";
import React, { useRef } from "react";
import PostCommentContainer from "./PostCommentContainer";
import TOC from "@/components/side/toc/TOC";

function PostContainer() {
  const commentRef = useRef(null);
  return (
    <Main
      MainContent={PostCommentContainer}
      RightSide={TOC}
      commentRef={commentRef}
    />
  );
}

export default PostContainer;
