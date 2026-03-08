import { atom } from "recoil";

export const AUTH_DEFAULT = {
  username: null,
  about: "자기소개를 작성해주세요.",
  email: null,
  isLogin: false,
  role: "GUEST",
  profileUrl: null,
  provider: null,
  certificate: false,
};

export const authAtom = atom({
  key: "author",
  default: { ...AUTH_DEFAULT },
});
