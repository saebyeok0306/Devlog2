import { atom } from "recoil";

const BLOG_DEFAULT = {
  username: "",
  about: "",
  profileUrl: "",
};

export const blogAtom = atom({
  key: "blog",
  default: BLOG_DEFAULT,
});
