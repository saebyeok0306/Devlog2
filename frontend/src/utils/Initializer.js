"use client";

import { useRecoilState } from "recoil";
import { useEffect } from "react";
import { get_info_api, put_blog_visit_api } from "@/api/info";
import { blogAtom } from "@/recoil/blogAtom";
import { renderAtom } from "@/recoil/renderAtom";

export default function Initializer() {
  const [, setRender] = useRecoilState(renderAtom);
  const [, setBlogProfile] = useRecoilState(blogAtom);

  useEffect(() => {
    setRender(true);
    put_blog_visit_api().then();
    get_info_api()
      .then((res) => {
        setBlogProfile(res);
      })
      .catch((err) => {
        console.error(err);
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
}
