import { atom } from "recoil";

export const categoryAtom = atom({
  key: "category",
  /** @type {number} */
  default: 0,
});

export const categoryUpdaterAtom = atom({
  key: "category_updater",
  /** @type {number} */
  default: 0,
});
