"use client";

import "./Info.scss";
import { Alert, Button, TextInput } from "flowbite-react";
import React, { useState } from "react";
import { useRecoilState } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import { upload_user_profile_api } from "@/api/user";
import { toast } from "react-toastify";

function Info(message) {
  const [isEdit, setIsEdit] = useState(false);
  const [authDto, setAuthDto] = useRecoilState(authAtom);

  const [info, setInfo] = useState({
    username: "",
    about: "",
  });

  const onEditHandler = () => {
    setIsEdit(true);
    setInfo({
      username: authDto.username,
      about: authDto.about || "",
    });
  };

  const onUploadHandler = async () => {
    if (authDto.username === info.username && authDto.about === info.about) {
      setIsEdit(false);
      return;
    }
    try {
      await upload_user_profile_api(info.username, info.about);
      setAuthDto({ ...authDto, username: info.username, about: info.about });
      toast.info("프로필이 수정되었습니다.");
    } catch (error) {
      console.error("Failed to upload user profile:", error);
      toast.error("프로필 수정 중 오류가 발생했습니다.");
    } finally {
      setIsEdit(false);
    }
  };

  if (isEdit) {
    return (
      <form className="profile-info-flex">
        <div className="info-area">
          <div className="username">
            <input
              type="text"
              value={info.username}
              onChange={(e) => setInfo({ ...info, username: e.target.value })}
              maxLength={6}
              required
            />
          </div>
          <div className="desc">
            <input
              type="text"
              value={info.about}
              onChange={(e) => setInfo({ ...info, about: e.target.value })}
              maxLength={50}
            />
          </div>
          <Alert className="alert" color="success">
            <span className="font-medium">닉네임</span>은 최대 6글자,{" "}
            <span className="font-medium">자기소개</span>는 50자까지 작성할 수
            있습니다.
          </Alert>
        </div>
        <div>
          <Button size="xs" onClick={onUploadHandler}>
            수정하기
          </Button>
        </div>
      </form>
    );
  }

  return (
    <div className="profile-info-flex">
      <div className="info-area">
        <div className="username">{authDto.username}</div>
        <div className="desc">{authDto.about}</div>
      </div>
      <div>
        <Button size="xs" onClick={onEditHandler}>
          수정하기
        </Button>
      </div>
    </div>
  );
}

export default Info;
