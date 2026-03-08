import { atom } from "recoil";

export const POST_CONTEXT_DEFAULT = {
  id: null,
  title: "",
  body: "",
  content: "",
  category: null,
  files: [],
  preview: null,
  hidden: false,
  createdAt: null,
  modifiedAt: null,
  url: "",
  views: 0,
};

export const postContextAtom = atom({
  key: "postContext",
  default: { ...POST_CONTEXT_DEFAULT },
});
