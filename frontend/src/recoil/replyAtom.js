import { atom } from "recoil";

export const replyAtom = atom({
  key: "reply",
  /** @type {string} */
  default: "",
});
