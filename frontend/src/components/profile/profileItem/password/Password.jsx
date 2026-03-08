"use client";
import React, { useState } from "react";

import "./Password.scss";

import { Button } from "flowbite-react";
import { toast } from "react-toastify";
import { renew_password_api } from "@/api/user";

const passwordInit = {
  currentPassword: "",
  newPassword: "",
  newPasswordConfirm: "",
};

export function ProfilePassword({ userProfile }) {
  const [edit, setEdit] = useState(false);
  const [password, setPassword] = useState(passwordInit);
  const [match, setMatch] = useState(true);
  const [prvMatch, setPrvMatch] = useState(true);

  const renewPasswordHandler = async () => {
    if (password.currentPassword === "" && password.newPassword === "") {
      setPassword(passwordInit);
      setEdit(false);
      return;
    }
    if (!match) {
      toast.error("비밀번호가 일치하지 않습니다.");
      return;
    }

    if (password.newPassword === password.currentPassword) {
    }

    try {
      await renew_password_api({ ...password });
      toast.success("비밀번호가 변경되었습니다.");
      setPassword(passwordInit);
      setEdit(false);
      return;
    } catch (error) {
      toast.error(
        error?.response
          ? error.response.data.error
          : "서버에 오류가 발생했습니다."
      );
      setPassword({ ...password, currentPassword: "" });
      return;
    }
  };

  const currentPasswordChangeHandler = (e) => {
    setPassword({
      ...password,
      currentPassword: e.target.value,
    });
    setPrvMatch(e.target.value !== password.newPassword);
  };

  const passwordChangeHandler = (e) => {
    setPassword({
      ...password,
      newPassword: e.target.value,
    });
    setMatch(e.target.value === password.newPasswordConfirm);
    setPrvMatch(e.target.value !== password.currentPassword);
  };

  const passwordCheckHandler = (e) => {
    setPassword({
      ...password,
      newPasswordConfirm: e.target.value,
    });
    setMatch(e.target.value === password.newPassword);
  };

  return (
    <div className="profile-body-password-box">
      <div className="content-box">
        <div className="content-label">비밀번호</div>
        <div className="content-data">
          {/* <div className="content-data-password">
            <HiOutlineMail style={{ margin: "auto 0" }} />
            <span>{authDto.email}</span>
          </div> */}
          {edit ? (
            <>
              <div className="content-data-password">
                <div>
                  <input
                    type="password"
                    placeholder="현재 비밀번호"
                    value={password.currentPassword}
                    onChange={(e) => currentPasswordChangeHandler(e)}
                  />
                </div>
                <div>
                  <input
                    type="password"
                    placeholder="신규 비밀번호"
                    value={password.newPassword}
                    onChange={(e) => passwordChangeHandler(e)}
                  />
                  <input
                    type="password"
                    placeholder="신규 비밀번호 확인"
                    value={password.newPasswordConfirm}
                    onChange={(e) => passwordCheckHandler(e)}
                    style={{ borderColor: match ? null : "#ff2626" }}
                  />
                  <Button
                    color="dark"
                    size="xs"
                    onClick={() => renewPasswordHandler()}
                  >
                    확인
                  </Button>
                </div>
              </div>
              <p
                className="text-gray-700 dark:text-gray-400"
                style={{ display: match ? "none" : "block" }}
              >
                !비밀번호가 일치하지 않습니다.
              </p>
              <p
                className="text-gray-700 dark:text-gray-400"
                style={{ display: prvMatch ? "none" : "block" }}
              >
                !이전에 쓰던 비밀번호와 동일합니다.
              </p>
            </>
          ) : (
            <Button
              color={userProfile.provider !== null ? "gray" : "green"}
              size="xs"
              onClick={() => setEdit(true)}
              disabled={userProfile.provider !== null}
            >
              비밀번호 변경하기
            </Button>
          )}
        </div>
      </div>
      <p>
        로그인 비밀번호를 변경합니다.
        <span style={{ fontWeight: "bold" }}> 소셜로그인</span>의 경우에는
        비활성화됩니다.
      </p>
    </div>
  );
}
