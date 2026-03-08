"use client";
import React, { useEffect } from "react";
import { toast } from "react-toastify";

const initTocHeader = () => {
  let toc_counter = 0;
  document.querySelectorAll("h1, h2, h3, h4, h5").forEach((el) => {
    el.setAttribute("id", el.tagName + "_" + toc_counter);
    toc_counter += 1;
  });
};

const onClickImageHandler = (img, modal, modalImg) => {
  modalImg.src = img.src; // 클릭한 이미지의 src를 모달에 설정
  modal.classList.add("active"); // 모달 보이기
};

const onClickModalHandler = (modal) => {
  modal.classList.remove("active");
};

async function copyCode(block) {
  let code = block.querySelector("code");
  let text = code.innerText;

  await navigator.clipboard.writeText(text);
  toast.info("코드가 복사되었습니다.", { position: "bottom-center" });
}

function PostInitializer(postContent) {
  useEffect(() => {
    window.scrollTo({
      top: 0,
      behavior: "instant",
    });

    initTocHeader();

    const images = document.querySelectorAll(".post-context img");
    const modal = document.querySelector(".post-image-modal");
    const modalImg = document.querySelector(".post-image-modal img");

    const imagesListeners = new Array(images.length);

    images.forEach((img, idx) => {
      const listener = () => onClickImageHandler(img, modal, modalImg);
      imagesListeners[idx] = listener;
      img.addEventListener("click", listener);
    });
    const modalListener = () => onClickModalHandler(modal);
    modal.addEventListener("click", modalListener);

    /** Code Block Copy Button */
    const Label = "Copy";
    const blocks = document.querySelectorAll("pre");
    const copyListeners = new Map();

    // Only add button if browser supports Clipboard API
    if (navigator.clipboard) {
      blocks.forEach((block) => {
        let button = document.createElement("button");

        button.innerText = Label;
        block.appendChild(button);

        const copyListener = () => copyCode(block);
        copyListeners.set(button, copyListener);
        button.addEventListener("click", copyListener);
      });
    }

    return () => {
      images.forEach((img, idx) => {
        img.removeEventListener("click", imagesListeners[idx]);
      });
      modal.removeEventListener("click", modalListener);
      copyListeners.forEach((val, key) => {
        key.removeEventListener("click", val);
      });
    };
  }, [postContent]);
}

export default PostInitializer;
