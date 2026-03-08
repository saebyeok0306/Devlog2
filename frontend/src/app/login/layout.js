"use client";

import { useRecoilValue } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import { useRouter } from "next/navigation";

export default function Layout({ children }) {
  const navigate = useRouter();
  const authDto = useRecoilValue(authAtom);

  if (authDto.isLogin) {
    return navigate.push("/");
  }
  return children;
}
