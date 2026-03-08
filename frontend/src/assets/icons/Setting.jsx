import * as React from "react";

const SettingIcon = (props) => (
  <svg
    width={800}
    height={800}
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
    {...props}
  >
    <path
      d="M9 22h6c5 0 7-2 7-7V9c0-5-2-7-7-7H9C4 2 2 4 2 9v6c0 5 2 7 7 7"
      stroke="current"
      strokeWidth={1.5}
      strokeLinecap="round"
      strokeLinejoin="round"
    />
    <path
      d="M15.57 18.5v-3.9m0-7.15V5.5m0 7.15a2.6 2.6 0 1 0 0-5.2 2.6 2.6 0 0 0 0 5.2M8.43 18.5v-1.95m0-7.15V5.5m0 11.05a2.6 2.6 0 1 0 0-5.2 2.6 2.6 0 0 0 0 5.2"
      stroke="current"
      strokeWidth={1.5}
      strokeMiterlimit={10}
      strokeLinecap="round"
      strokeLinejoin="round"
    />
  </svg>
);
export default SettingIcon;
