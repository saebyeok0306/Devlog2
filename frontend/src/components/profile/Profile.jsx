"use client";
import React, { useEffect, useRef, useState } from "react";

import "./Profile.scss";
import { useRecoilState } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import { Avatar, Button } from "flowbite-react";

import ImageCrop from "./imageCrop";
import {
  ProfileEmail,
  ProfilePassword,
  ProfileUnregister,
} from "./profileItem";
import { toast } from "react-toastify";
import { delete_profile_url_api } from "@/api/user";
import Info from "@/components/profile/profileItem/info/Info";

function Profile() {
  const fileInputRef = useRef(null);
  const [authDto, setAuthDto] = useRecoilState(authAtom);
  const [userProfile, setUserProfile] = useState({
    username: "",
    about: "",
    email: "",
    profileUrl: "",
    role: "",
    provider: null,
    certificate: false,
  });

  const [imageCrop, setImageCrop] = useState({
    openModal: false,
    targetImage: null,
    targetUrl: null,
    targetSrc: null,
  });

  useEffect(() => {
    setUserProfile({ ...userProfile, ...authDto });
    // eslint-disable-next-line
  }, [authDto]);

  const uploadImageHandler = () => {
    fileInputRef.current.click();
  };

  const removeImageHandler = async () => {
    try {
      await delete_profile_url_api();
      setAuthDto({ ...authDto, profileUrl: null });
      setUserProfile({ ...userProfile, profileUrl: null });
    } catch (error) {
      toast.error("요청을 처리하는데 실패했습니다.");
    }
  };

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.addEventListener("load", () =>
        setImageCrop({
          ...imageCrop,
          openModal: true,
          targetSrc: reader.result?.toString() || "",
        })
      );
      reader.readAsDataURL(file);
      event.target.value = null;
    }
  };

  return (
    <div className="profile-container">
      <ImageCrop
        imageCrop={imageCrop}
        setImageCrop={setImageCrop}
        setUserProfile={setUserProfile}
        setAuthDto={setAuthDto}
      />
      <header className="profile-header">
        <div className="profile-info-box">
          <Info />
        </div>
        <div className="profile-info-hr border-gray-300 dark:border-gray-500" />
        <div className="profile-image-box">
          <div className="profile-image">
            <Avatar img={userProfile.profileUrl} alt="user-profile" rounded />
            <input
              type="file"
              accept="image/*"
              ref={fileInputRef}
              style={{ display: "none" }}
              onChange={handleFileChange}
            />
          </div>
          <div className="profile-buttons">
            <Button onClick={uploadImageHandler}>이미지 업로드</Button>
            <Button color="gray" onClick={removeImageHandler}>
              이미지 삭제
            </Button>
          </div>
        </div>
      </header>
      <hr className="border-gray-300 dark:border-gray-500" />
      <div className="profile-body">
        <ProfileEmail
          userProfile={userProfile}
          setUserProfile={setUserProfile}
        />
        <hr className="border-gray-300 dark:border-gray-500" />
        <ProfilePassword userProfile={userProfile} />
        <hr className="border-gray-300 dark:border-gray-500" />
        <ProfileUnregister userProfile={userProfile} />
      </div>
    </div>
  );
}

export default Profile;
