import React from "react";
import LeftSideContainer from "@/containers/side/LeftSideContainer";
import Main from "@/components/common/main";
import EditCategory from "@/components/category/edit/EditCategory";

function CategoryManagerContainer() {
  return <Main LeftSide={LeftSideContainer} MainContent={EditCategory} />;
}

export default CategoryManagerContainer;
