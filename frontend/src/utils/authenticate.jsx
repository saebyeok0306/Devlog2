"use client";
import { AUTH_DEFAULT } from "@/recoil/authAtom";
import { toast } from "react-toastify";
import { clearAllCacheStore } from "@/api/cache";
import { user_logout_api, user_profile_api } from "@/api/user";

export const signIn = async (setAuthDto, message = "로그인 성공!") => {
  try {
    let res = await user_profile_api();
    const payload = res.data;
    setAuthDto((prev) => ({
      ...prev,
      username: payload.username,
      email: payload.email,
      about: payload.about,
      isLogin: true,
      role: payload.role,
      profileUrl: payload.profileUrl,
      provider: payload.provider,
      certificate: payload.certificate,
    }));

    clearAllCacheStore(); // 캐시 초기화
    if (message !== false) toast.success(`${message}`, {});
    return true;
  } catch (error) {
    console.error("Failed to sign in:", error);
    return false;
  }
};

export const signOutProcess = async (setAuthDto) => {
  try {
    await user_logout_api();
    setAuthDto({ ...AUTH_DEFAULT });
    clearAllCacheStore(); // 캐시 초기화
    return true;
  } catch (error) {
    console.error("Failed to sign out:", error);
  }
  return false;
};

export const signOut = async (setAuthDto, message = "로그아웃 했습니다.") => {
  if (await signOutProcess(setAuthDto)) {
    toast.success(`${message}`, {});
  }
};

export const warnSignOut = async (
  setAuthDto,
  message = "로그인이 만료되었습니다."
) => {
  if (await signOutProcess(setAuthDto)) {
    toast.warning(`${message}`, {});
  }
};
