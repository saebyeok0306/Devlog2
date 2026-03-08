"use client";

import React from "react";
import ReCAPTCHA from "react-google-recaptcha";
import dynamic from "next/dynamic";
import { Label, TextInput } from "flowbite-react";

import { CgProfile } from "react-icons/cg";
import { RiLockPasswordLine } from "react-icons/ri";
import { verify_captcha_api } from "@/api/user";
import { toast } from "react-toastify";

const CommentEditor = dynamic(
  () => import("@/components/editor/commentEditor"),
  {
    ssr: false, // 서버사이드 렌더링 비활성화
  }
);

function GuestCommentEditor({
  comment,
  setComment,
  onCancel,
  onSave,
  setUpdater,
  captchaRef,
  setUsername,
  setPassword,
  setVerify,
}) {
  const onCaptchaHandler = async (value) => {
    if (value) {
      try {
        const payload = await verify_captcha_api(value);
        if (payload.success) {
          toast.info("인증되었습니다.");
          setVerify(true);
          return;
        }
      } catch (err) {
        toast.error(
          "캡챠 인증 중 서버에서 오류가 발생했습니다. 다시 시도해주세요."
        );
        console.log(err);
      }
      toast.error("인증에 실패했습니다.");
      captchaRef.current.props.grecaptcha.reset();
      setVerify(false);
    } else {
      toast.error("인증이 만료되었습니다.");
      setVerify(false);
    }
  };

  return (
    <div>
      <div className="anonymous-comment flex flex-col gap-2 mb-2">
        <div className="flex flex-col gap-2">
          {setUsername != null ? (
            <div className="flex">
              <Label
                htmlFor="username"
                className="mt-auto mb-auto w-[5.5rem] flex gap-1 mr-2"
              >
                <CgProfile className="mt-auto mb-auto" />
                Username
              </Label>
              <TextInput
                id="username"
                className="max-w-[15rem] w-full"
                placeholder="이름을 입력해주세요."
                type="text"
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
          ) : null}
          {setPassword != null ? (
            <div className="flex">
              <Label
                htmlFor="password"
                className="mt-auto mb-auto w-[5.5rem] flex gap-1 mr-2"
              >
                <RiLockPasswordLine className="mt-auto mb-auto" />
                Password
              </Label>
              <TextInput
                id="password"
                className="max-w-[15rem] w-full"
                placeholder="비밀번호를 입력해주세요."
                type="password"
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
          ) : null}
        </div>
        {captchaRef != null ? (
          <ReCAPTCHA
            className="g-recaptcha mb-[-20px]"
            ref={captchaRef}
            sitekey={`${process.env.NEXT_PUBLIC_RECAPTCHA_PUBLIC_KEY}`}
            onChange={onCaptchaHandler}
          />
        ) : null}
      </div>
      <CommentEditor
        comment={comment}
        setComment={setComment}
        onCancel={onCancel}
        onSave={onSave}
        setUpdater={setUpdater}
        captchaRef={captchaRef}
      />
    </div>
  );
}

export default GuestCommentEditor;
