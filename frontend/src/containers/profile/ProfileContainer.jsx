import React from "react";
import Main from "@/components/common/main";
import LeftSideContainer from "@/containers/side/LeftSideContainer";
import Profile from "@/components/profile/Profile";

function ProfileContainer() {
  return <Main LeftSide={LeftSideContainer} MainContent={Profile} />;
}

export default ProfileContainer;
