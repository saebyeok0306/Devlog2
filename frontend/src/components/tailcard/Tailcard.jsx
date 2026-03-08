"use client";
import "./Tailcard.scss";
import { useRecoilValue } from "recoil";
import { SHORT_BLOG_NAME } from "@/constants/base/main";
import { Avatar } from "flowbite-react";
import { renderAtom } from "@/recoil/renderAtom";

function Tailcard({ ...props }) {
  const { profile } = props;
  const isRender = useRecoilValue(renderAtom);

  if (!isRender) {
    return <></>;
  }

  return (
    <div className="tailcard-container">
      <div className="profile">
        <Avatar
          className="avatar"
          img={profile?.profileUrl}
          size="lg"
          rounded
        />
      </div>
      <div className="info">
        <p className="name">
          {profile?.username}｜{SHORT_BLOG_NAME}
        </p>
        <p className="desc">{profile?.about || "자기소개를 작성해주세요."}</p>
        {/* TODO: github link icon */}
        <ul>
          <li></li>
        </ul>
      </div>
    </div>
  );
}

export default Tailcard;
