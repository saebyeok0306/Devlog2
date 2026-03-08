"use client";
import React, { useState } from "react";

import "./Email.scss";

import { HiOutlineMail } from "react-icons/hi";
import { Button } from "flowbite-react";
import VerifyModal from "./VerifyModal";
import { request_verify_email_api } from "@/api/user";

export function ProfileEmail({ userProfile, setUserProfile }) {
  const [verifyTimer, setVerifyTimer] = useState(300);
  const [verifyModal, setVerifyModal] = useState(false);

  const requestVerifyEmailHandler = async () => {
    try {
      await request_verify_email_api();
      setVerifyModal(true);
      setVerifyTimer(300);
    } catch (error) {}
  };

  return (
    <div className="profile-body-email-box">
      <VerifyModal
        verifyModal={verifyModal}
        setVerifyModal={setVerifyModal}
        verifyTimer={verifyTimer}
        setVerifyTimer={setVerifyTimer}
        setUserProfile={setUserProfile}
        requestVerifyEmailHandler={requestVerifyEmailHandler}
      />
      <div className="content-box">
        <div className="content-label">이메일</div>
        <div className="content-data">
          <div className="content-data-email">
            <HiOutlineMail style={{ margin: "auto 0" }} />
            <span>{userProfile.email}</span>
          </div>
          {userProfile.certificate ? (
            <Button color="dark" size="xs" disabled>
              이메일 인증완료
            </Button>
          ) : (
            <Button
              color="green"
              size="xs"
              onClick={() => requestVerifyEmailHandler()}
              disabled={verifyModal}
            >
              이메일 인증하기
            </Button>
          )}
        </div>
      </div>
      <p>회원 인증 또는 시스템에서 발송하는 이메일을 수신하는 주소입니다.</p>
    </div>
  );
}
