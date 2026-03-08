"use client";
import React, { useRef, useState } from "react";

import "./ImageCrop.scss";
import { Button, Modal } from "flowbite-react";
import ReactCrop, { centerCrop, makeAspectCrop } from "react-image-crop";

import "react-image-crop/dist/ReactCrop.css";
import { canvasPreview } from "./canvasPreview";
import { toast } from "react-toastify";
import { resizeBlob } from "@/utils/ImageResizer";
import { upload_file_api } from "@/api/file";
import { upload_profile_url_api } from "@/api/user";

// 레퍼런스
// https://codesandbox.io/p/sandbox/react-image-crop-demo-with-react-hooks-y831o?file=%2Fsrc%2FApp.tsx%3A131%2C18

function centerAspectCrop(mediaWidth, mediaHeight, aspect) {
  return centerCrop(
    makeAspectCrop(
      {
        unit: "%",
        width: 90,
        height: 90,
      },
      aspect,
      mediaWidth,
      mediaHeight
    ),
    mediaWidth,
    mediaHeight
  );
}

function ImageCrop({ imageCrop, setImageCrop, setUserProfile, setAuthDto }) {
  const blobUrlRef = useRef(null);
  const imageRef = useRef(null);
  const previewCanvasRef = useRef(null);
  const [crop, setCrop] = useState();
  const cropProp = {
    aspect: 1,
  };
  const [completedCrop, setCompletedCrop] = useState(null);

  const closeHandler = () => {
    setImageCrop({ ...imageCrop, openModal: false, targetSrc: null });
    setCrop(undefined);
  };

  const onImageLoadHandler = (image) => {
    if (cropProp.aspect) {
      const { width, height } = image.currentTarget;
      setCrop(centerAspectCrop(width, height, cropProp.aspect));
    }
  };

  async function onDownloadCropClickHandler() {
    const image = imageRef.current;
    const previewCanvas = previewCanvasRef.current;

    if (!image || !completedCrop) {
      throw new Error("Crop canvas does not exist");
    }

    if (!completedCrop.width || !completedCrop.height) {
      toast.error("영역을 선택해주세요.");
      return;
    }

    canvasPreview(image, previewCanvas, completedCrop);

    const scaleX = image.naturalWidth / image.width;
    const scaleY = image.naturalHeight / image.height;

    const offscreen = new OffscreenCanvas(
      completedCrop.width * scaleX,
      completedCrop.height * scaleY
    );

    const ctx = offscreen.getContext("2d");
    if (!ctx) {
      throw new Error("No 2d context");
    }

    ctx.drawImage(
      previewCanvas,
      0,
      0,
      previewCanvas.width,
      previewCanvas.height,
      0,
      0,
      offscreen.width,
      offscreen.height
    );

    // You might want { type: "image/jpeg", quality: <0 to 1> } to
    // reduce image size
    const blob = await offscreen.convertToBlob({
      type: "image/png",
    });

    if (blobUrlRef.current) {
      URL.revokeObjectURL(blobUrlRef.current);
    }

    await resizeBlob(blob)
      .then(async (output_blob) => {
        try {
          const output_file = new File([output_blob], "profile.png", {
            type: "image/png",
            lastModified: new Date(),
          });
          const payload = await upload_file_api(output_file);
          const profileUrl = `${process.env.NEXT_PUBLIC_API_FILE_URL}/${payload.filePath}/${payload.fileUrl}`;
          await upload_profile_url_api(payload, profileUrl);
          // const new_url = URL.createObjectURL(output_blob);
          blobUrlRef.current = profileUrl;
          setUserProfile((prev) => ({
            ...prev,
            profileUrl: profileUrl,
          }));
          setAuthDto((prev) => ({ ...prev, profileUrl: profileUrl }));
        } catch (error) {
          toast.error("이미지 업로드에 실패했습니다.");
        }
      })
      .catch((err) => {
        console.error(err);
      });

    // blobUrlRef.current = URL.createObjectURL(blob);
    // await setUserProfile(blobUrlRef.current);

    closeHandler();
  }

  return (
    <Modal show={imageCrop.openModal} onClose={closeHandler} size="xl" popup>
      <Modal.Header>프로필 이미지 설정</Modal.Header>
      <Modal.Body>
        <div className="imageCrop-container">
          {!!imageCrop.targetSrc && (
            <ReactCrop
              className="imageCrop-cropper"
              crop={crop}
              onChange={(_, newCrop) => setCrop(newCrop)}
              onComplete={(c) => setCompletedCrop(c)}
              aspect={cropProp.aspect}
              minHeight={128}
            >
              <img
                ref={imageRef}
                alt="Crop me"
                src={imageCrop.targetSrc}
                onLoad={onImageLoadHandler}
              />
            </ReactCrop>
          )}
          {!!completedCrop && (
            <canvas
              ref={previewCanvasRef}
              style={{
                display: "none",
                border: "1px solid black",
                objectFit: "contain",
                width: completedCrop.width,
                height: completedCrop.height,
              }}
            />
          )}
        </div>
      </Modal.Body>
      <Modal.Footer>
        <Button onClick={onDownloadCropClickHandler}>확인</Button>
        <Button color="gray" onClick={closeHandler}>
          취소
        </Button>
      </Modal.Footer>
    </Modal>
  );
}

export default ImageCrop;
