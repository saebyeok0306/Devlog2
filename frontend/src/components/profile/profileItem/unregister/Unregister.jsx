"use client";
import React from "react";

import "./Unregister.scss";
import { Button } from "flowbite-react";
import { toast } from "react-toastify";

export function ProfileUnregister({ userProfile }) {
  return (
    <div className="profile-body-unregister-box">
      <div className="content-box">
        <div className="content-label">회원탈퇴</div>
        <div className="content-data">
          <Button
            color="red"
            size="xs"
            onClick={() => toast.info("아직 안만들었다네오")}
          >
            탈퇴하고 싶어요
          </Button>
        </div>
      </div>
      <p>
        회원 탈퇴시 작성한 모든 게시글과 댓글이 삭제되며 복구가 불가능합니다.
      </p>
    </div>
  );
}
