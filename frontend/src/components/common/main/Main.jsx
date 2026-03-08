import React from "react";
import Responsive from "@/components/common/Responsive";

import "./Main.scss";

function Main({ LeftSide, MainContent, RightSide, ...props }) {
  return (
    <Responsive className="main">
      {LeftSide == null ? (
        <aside style={{ height: "0px", padding: "0px" }} />
      ) : (
        <LeftSide {...props} />
      )}
      <section>
        <MainContent {...props} />
      </section>
      {RightSide == null ? (
        <aside style={{ height: "0px", padding: "0px" }} />
      ) : (
        <RightSide {...props} />
      )}
    </Responsive>
  );
}

export default Main;
