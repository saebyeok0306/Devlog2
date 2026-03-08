import React from "react";
import FooterComponent from "@/components/base/footer/FooterComponent";
// import { useLocation } from "react-router-dom";

function FooterContainer() {
  // const { pathname } = useLocation();

  // const pathFilter = () => {
  //   const paths = ["/login", "/signup"];
  //   for (let i = 0; i < paths.length; i++) {
  //     const path = paths[i];
  //     if (pathname.startsWith(path)) {
  //       return true;
  //     }
  //   }
  //   return false;
  // };

  // if (pathFilter()) {
  //   return <></>;
  // }

  return <FooterComponent />;
}

export default FooterContainer;
