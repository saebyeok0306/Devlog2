import { API } from "./axios";

export const upload_file_api = async (file) => {
  const formData = new FormData();
  formData.append("file", file);
  return await API.post("/files", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  })
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
