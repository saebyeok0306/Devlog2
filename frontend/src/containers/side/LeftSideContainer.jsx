import React from "react";
import Profile from "@/components/side/profile";
import Category from "@/components/category/display";
import Visit from "@/components/side/visit";

function LeftSideContainer() {
  return (
    <aside>
      <Profile />
      <Category />
      <Visit />
    </aside>
  );
}

export default LeftSideContainer;
