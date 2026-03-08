import { LLM_API } from "./axios";

export const upgrade_sentence_api = async (message) => {
  const requestBody = {
    message: message,
  };
  const config = {
    withCredentials: true,
    headers: {
      "Content-Type": "application/json",
    },
    responseType: "stream",
    adapter: "fetch",
  };

  return await LLM_API.post("/upgrade-sentence", requestBody, config)
    .then(async (response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const title_suggestion_api = async (message) => {
  const requestBody = {
    message: message,
  };
  const config = {
    withCredentials: true,
    headers: {
      "Content-Type": "application/json",
    },
    responseType: "stream",
    adapter: "fetch",
  };

  return await LLM_API.post("/title-suggestion", requestBody, config)
    .then(async (response) => response.data)
    .catch((error) => {
      throw error;
    });
};
