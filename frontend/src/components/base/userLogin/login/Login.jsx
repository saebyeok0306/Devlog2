"use client";
import React, { useRef, useState } from "react";
import Responsive from "@/components/common/Responsive";

import "./Login.scss";
import EmailIcon from "@/assets/icons/Email";
import PasswordIcon from "@/assets/icons/Password";
import {
  user_login_api,
  user_logout_api,
  verify_captcha_api,
} from "@/api/user";
import { authAtom } from "@/recoil/authAtom";
import { useRecoilState } from "recoil";
import { OAUTH2_URI } from "@/constants/api/oauth";
import { signIn } from "@/utils/authenticate";
import { toast } from "react-toastify";
import ReCAPTCHA from "react-google-recaptcha";
import { useRouter } from "next/navigation";
import { useTheme } from "next-themes";
import Link from "next/link";

function Login() {
  const navigate = useRouter();
  const [, setAuthDto] = useRecoilState(authAtom);
  const { resolvedTheme } = useTheme();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const captchaRef = useRef(null);
  const [verify, setVerify] = useState({
    value: "",
    step: 0,
  });

  const handleKeyDown = (event) => {
    if (event.key === "Enter") {
      nextVerifyStep(event);
    }
  };

  const nextVerifyStep = (e) => {
    e.preventDefault();

    user_login_api(email, password)
      .then(async (res) => {
        toast.info(res);
        user_logout_api();
        setVerify({ ...verify, step: 1 });
        setTimeout(() => {
          setVerify({ ...verify, step: 2 });
        }, 500);
        setTimeout(() => {
          setVerify({ ...verify, step: 3 });
        }, 1000);
      })
      .catch((err) => {
        console.log(err);
        toast.error(`${err.response?.data ? err.response.data.error : err}`);
      });
  };

  const oauthLoginAction = (e) => {
    e.preventDefault();
    const provider = "google";
    const link = `${process.env.NEXT_PUBLIC_API_ENDPOINT}/main/${OAUTH2_URI}/${provider}`;
    window.location.href = link;
  };

  const onCaptchaHandler = async (value) => {
    try {
      const payload = await verify_captcha_api(value);
      if (payload.success) {
        toast.info("인증되었습니다.");
        setVerify({ ...verify, value: value });
        return;
      }
    } catch (err) {
      console.log(err);
    }
    toast.error("인증에 실패했습니다.");
    captchaRef.current.props.grecaptcha.reset();
    setVerify({ ...verify, value: "" });
  };

  const onCaptchaLoginHandler = (e) => {
    if (verify.value === "") {
      toast.error("인증을 먼저 진행해야 합니다.");
      return;
    }
    e.preventDefault();
    user_login_api(email, password)
      .then(async (res) => {
        await signIn(setAuthDto);
        navigate.push("/");
      })
      .catch((err) => {
        toast.error(`${err.response?.data ? err.response.data.error : err}`);
      });
  };

  const onCaptchaBackHandler = (e) => {
    e.preventDefault();
    setVerify({ ...verify, step: 0 });
  };

  if (verify.step > 1) {
    return (
      <Responsive className="login">
        <div className="login-box">
          <div className="title">
            로그인
            <div className="liner" />
          </div>
          <div
            className={`verify-align ${verify.step > 2 ? "show-captcha" : "hidden-captcha"}`}
          >
            <div className={`captcha-box m-auto`}>
              <ReCAPTCHA
                ref={captchaRef}
                sitekey={`${process.env.NEXT_PUBLIC_RECAPTCHA_PUBLIC_KEY}`}
                onChange={onCaptchaHandler}
              />
            </div>
            <div className="buttons col w-1/2 m-auto mt-3">
              <div className="button" onClick={onCaptchaLoginHandler}>
                로그인 하기
              </div>
              <div className="button pick" onClick={onCaptchaBackHandler}>
                뒤로가기
              </div>
            </div>
          </div>
        </div>
      </Responsive>
    );
  }

  return (
    <Responsive className="login">
      <div className="login-box">
        <div className="title">
          로그인
          <div className="liner" />
        </div>
        <div className={`login-align ${verify.step > 0 ? "hidden-login" : ""}`}>
          <div className="login-left">
            <div className="inputs">
              <div className="input">
                <EmailIcon />
                <input
                  type="email"
                  placeholder="이메일"
                  onChange={(e) => setEmail(e.target.value)}
                  onKeyDown={(e) => handleKeyDown(e)}
                  tabIndex={1}
                />
              </div>
              <div className="input">
                <PasswordIcon />
                <input
                  type="password"
                  placeholder="비밀번호"
                  onChange={(e) => setPassword(e.target.value)}
                  onKeyDown={(e) => handleKeyDown(e)}
                  tabIndex={2}
                />
              </div>
            </div>
            <div className="etc">
              <div className="find">비밀번호를 잊어버리셨나요?</div>
              <Link tabIndex={3} href="/">
                비밀번호 찾기
              </Link>
            </div>
          </div>
          <div className="login-right">
            <div className="buttons col">
              <div className="button" onClick={nextVerifyStep} tabIndex={4}>
                로그인 하기
              </div>
              <div
                className="button oauth google"
                onClick={oauthLoginAction}
                tabIndex={5}
              >
                구글 로그인
              </div>
              <Link className="button pick" href="/signup" tabIndex={6}>
                계정 만들기
              </Link>
            </div>
          </div>
        </div>
      </div>
    </Responsive>
  );
}

export default Login;
