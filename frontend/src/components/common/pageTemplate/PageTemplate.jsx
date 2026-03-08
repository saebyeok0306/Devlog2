import React from "react";

import "./PageTemplate.scss";

function PageTemplate({ header, children }) {
  return (
    <div className="pageTemplate">
      {header}
      <main>{children}</main>
    </div>
  );
}

export default PageTemplate;
