import { API } from "./axios";

export const get_categories_api = async () => {
  return await API.get("/categories")
    .then((response) => response?.data)
    .catch((error) => {
      throw error;
    });
};

export const get_categories_readwrite_api = async () => {
  return await API.get("/categories/readwrite")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_categories_detail_api = async () => {
  return await API.get("/categories/details")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const set_categories_api = async (categories) => {
  return await API.post("/categories", categories)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
