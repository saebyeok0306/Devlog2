import { atom } from "recoil";

export const postPagingAtom = atom({
  key: "postPaging",
  default: {
    totalPages: 0,
    currentPage: 0,
    totalElements: 0,
  },
});
