"use client";
import { authAtom } from "@/recoil/authAtom";
import axios from "axios";
import mem from "mem";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { useRecoilState } from "recoil";
import { ETC_STORE } from "./cache";
import { warnSignOut } from "@/utils/authenticate";
import { jwt_refresh_api } from "@/api/user";
import { LLM_URL, MAIN_URL } from "@/constants/api/url";

const REFRESH_URL = "/reissue";

export const API = axios.create({
  baseURL: MAIN_URL,
  withCredentials: true,
});

export const LLM_API = axios.create({
  baseURL: LLM_URL,
  withCredentials: true,
});

const requestHandler = async (request) => {
  return request;
};

const responseSuccessHandler = (response) => {
  return response;
};

const responseRejectHandler = async (err, navigate, authDto, setAuthDto) => {
  // Network Error
  if (!err.response?.status) return Promise.reject(err);

  const {
    config,
    response: { status, data },
  } = err;

  const signOutToast = mem(
    async (message, path) => {
      await warnSignOut(setAuthDto, message);
      navigate.push(path);
    },
    { maxAge: 5, cache: ETC_STORE }
  );

  if (config.url === REFRESH_URL) {
    if (900 <= status && status <= 999)
      await signOutToast("다시 로그인을 해주세요.", "/login");
    return Promise.reject(err);
  }

  if (900 <= status && status <= 999) {
    if (status !== 900) {
      await signOutToast("다시 로그인을 해주세요.", "/");
      return Promise.reject(err);
    }
    try {
      await jwt_refresh_api(authDto.email);
    } catch (error) {
      return Promise.reject(err);
    }
    return API(config);
  }

  return Promise.reject(err);
};

export const AuthTokenInterceptor = () => {
  const navigate = useRouter();
  const [authDto, setAuthDto] = useRecoilState(authAtom);

  const mainRequestHandler = API.interceptors.request.use(requestHandler);
  const mainRejectHandler = API.interceptors.response.use(
    (response) => responseSuccessHandler(response),
    (error) => responseRejectHandler(error, navigate, authDto, setAuthDto)
  );

  const llmRequestHandler = LLM_API.interceptors.request.use(requestHandler);
  const llmRejectHandler = LLM_API.interceptors.response.use(
    (response) => responseSuccessHandler(response),
    (error) => responseRejectHandler(error, navigate, authDto, setAuthDto)
  );

  useEffect(() => {
    return () => {
      API.interceptors.request.eject(mainRequestHandler);
      API.interceptors.response.eject(mainRejectHandler);
      LLM_API.interceptors.request.eject(llmRequestHandler);
      LLM_API.interceptors.response.eject(llmRejectHandler);
    };
  }, [
    mainRequestHandler,
    mainRejectHandler,
    llmRequestHandler,
    llmRejectHandler,
  ]);
};

export const AxiosProvider = ({ children }) => {
  const navigate = useRouter();
  const [authDto, setAuthDto] = useRecoilState(authAtom);

  const mainRequestHandler = API.interceptors.request.use(requestHandler);
  const mainRejectHandler = API.interceptors.response.use(
    (response) => responseSuccessHandler(response),
    (error) => responseRejectHandler(error, navigate, authDto, setAuthDto)
  );

  const llmRequestHandler = LLM_API.interceptors.request.use(requestHandler);
  const llmRejectHandler = LLM_API.interceptors.response.use(
    (response) => responseSuccessHandler(response),
    (error) => responseRejectHandler(error, navigate, authDto, setAuthDto)
  );

  useEffect(() => {
    return () => {
      API.interceptors.request.eject(mainRequestHandler);
      API.interceptors.response.eject(mainRejectHandler);
      LLM_API.interceptors.request.eject(llmRequestHandler);
      LLM_API.interceptors.response.eject(llmRejectHandler);
    };
  }, [
    mainRequestHandler,
    mainRejectHandler,
    llmRequestHandler,
    llmRejectHandler,
  ]);

  return <>{children}</>;
};
