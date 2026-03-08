import { API } from "./axios";

export const post_like_api = async (postUrl) => {
  return await API.put(`/like/post/${postUrl}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const post_unlike_api = async (postUrl) => {
  return await API.delete(`/like/post/${postUrl}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
