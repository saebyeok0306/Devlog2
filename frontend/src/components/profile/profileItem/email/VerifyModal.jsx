"use client";
import React, { useEffect, useState } from "react";
import { Alert, Button, Modal, TextInput, Tooltip } from "flowbite-react";

import "./VerifyModal.scss";
import { HiCheck } from "react-icons/hi";
import { send_verify_email_api } from "@/api/user";
import { toast } from "react-toastify";

function VerfiyTimerObj({ verifyTimer, setVerifyTimer }) {
  useEffect(() => {
    if (verifyTimer > 0) {
      // 1초마다 timeLeft를 1씩 감소
      const timerInterval = setInterval(() => {
        setVerifyTimer((prev) => prev - 1);
      }, 1000);

      // 컴포넌트가 언마운트될 때 setInterval을 정리
      return () => clearInterval(timerInterval);
    }
    // eslint-disable-next-line
  }, [verifyTimer]);

  const minutes = Math.floor(verifyTimer / 60);
  const seconds = verifyTimer % 60;

  if (verifyTimer === 0) {
    return <p>인증번호 유효시간이 만료되었습니다.</p>;
  }

  return (
    <p>
      <span className="text-gray-800 dark:text-gray-300 font-bold">
        {minutes}:{seconds < 10 ? `0${seconds}` : seconds}
      </span>{" "}
      내에 인증번호를 입력해주세요.
    </p>
  );
}

function VerifyModal({
  verifyModal,
  setVerifyModal,
  verifyTimer,
  setVerifyTimer,
  setUserProfile,
  requestVerifyEmailHandler,
}) {
  const [verifyCode, setVerifyCode] = useState("");

  const closeHandler = () => {
    setVerifyModal(false);
  };

  const sendVerifyEmailHandler = async () => {
    if (verifyCode.length !== 8) {
      toast.error("인증번호를 정확히 입력해주세요.");
      return;
    }
    try {
      await send_verify_email_api(verifyCode);
      setVerifyModal(false);
      setVerifyCode("");
      setUserProfile((prev) => ({ ...prev, certificate: true }));
      toast.success("이메일 인증이 완료되었습니다.");
    } catch (error) {
      setVerifyCode("");
      toast.error("인증번호가 일치하지 않습니다.");
    }
  };

  const retryVerifyEmailHandler = async () => {
    await requestVerifyEmailHandler();
    toast.info("인증번호를 재전송했습니다.");
  };

  return (
    <Modal show={verifyModal} onClose={closeHandler} size="sm" popup>
      <Modal.Header>이메일 인증</Modal.Header>
      <Modal.Body>
        <div className="verify-modal-container">
          <Alert color="info" icon={HiCheck} className="p-2 mb-2">
            <p>이메일로 인증번호를 보냈습니다!</p>
          </Alert>
          <TextInput
            value={verifyCode}
            onChange={(e) => setVerifyCode(e.target.value)}
            maxLength={8}
            placeholder="인증번호"
            className="mb-2"
            style={verifyTimer === 0 ? { borderColor: "#ff2626" } : null}
          />
          <VerfiyTimerObj
            verifyTimer={verifyTimer}
            setVerifyTimer={setVerifyTimer}
          />
          <div className="verify-modal-buttons mt-3">
            <Button
              size="xs"
              color="indigo"
              disabled={verifyTimer === 0}
              onClick={() => sendVerifyEmailHandler()}
            >
              확인
            </Button>
            <Tooltip placement="bottom" content="이메일이 도착하지 않아요.">
              <Button
                size="xs"
                color="gray"
                onClick={() => retryVerifyEmailHandler()}
              >
                재전송
              </Button>
            </Tooltip>
          </div>
        </div>
      </Modal.Body>
    </Modal>
  );
}

export default VerifyModal;
