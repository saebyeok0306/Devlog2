import { atom } from "recoil";

export const windowAtom = atom({
  key: "window",
  default: {
    width: 0,
  },
});
