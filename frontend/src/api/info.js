import { API } from "./axios";

export const get_info_api = async () => {
  return await API.get("/info")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const set_info_api = async (about, profile_url) => {
  const requestBody = {
    about: about,
    profile_url: profile_url,
  };
  return await API.post("/info", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_blog_visit_daily_api = async (start, end) => {
  return await API.get(`/blog/visit/daily?start=${start}&end=${end}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const put_blog_visit_api = async () => {
  return await API.put(`/blog/visit`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
