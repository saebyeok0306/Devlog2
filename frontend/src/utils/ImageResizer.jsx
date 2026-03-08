"use client";
import FileResizer from "react-image-file-resizer";

export const resizeBlob = (blob, width = 256, height = 256) =>
  new Promise((resolve) => {
    FileResizer.imageFileResizer(
      blob, // Blob
      width, // maxWidth.
      height, // maxHeight
      "PNG", // Format.
      100, // Quality 100 is max.
      0,
      (uri) => {
        resolve(uri);
      },
      "blob" // output type = base64 | blob | file
    );
  });
