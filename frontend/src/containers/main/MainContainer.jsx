import React from "react";
import Main from "@/components/common/main";
import LeftSideContainer from "@/containers/side/LeftSideContainer";
import Posts from "@/components/posts/Posts";

function MainContainer() {
  return <Main LeftSide={LeftSideContainer} MainContent={Posts} />;
}

export default MainContainer;
