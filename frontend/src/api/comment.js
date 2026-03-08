import { API } from "./axios";

export const edit_comment_api = async (commentId, content, files, hidden) => {
  const requestBody = {
    content: content,
    files: files,
    hidden: hidden,
  };

  return await API.post(`/comments/${commentId}`, requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const upload_comment_api = async (
  post,
  parent,
  content,
  files,
  hidden
) => {
  const requestBody = {
    postUrl: post.url,
    parent: parent,
    content: content,
    files: files,
    hidden: hidden,
  };

  return await API.post(`/comments`, requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const delete_comment_api = async (commentId) => {
  return await API.delete(`/comments/${commentId}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_comments_by_post_api = async (postId) => {
  return await API.get(`/comments/post/${postId}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_comment_files_api = async (commentId) => {
  return await API.get(`/files/comment/${commentId}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const upload_guest_comment_api = async (
  post,
  parent,
  content,
  hidden,
  username,
  password
) => {
  const requestBody = {
    postUrl: post.url,
    parent: parent,
    content: content,
    hidden: hidden,
    username: username,
    password: password,
  };

  return await API.post(`/comments/guest`, requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const edit_guest_comment_api = async (
  commentId,
  content,
  hidden,
  password
) => {
  const requestBody = {
    content: content,
    hidden: hidden,
    password: password,
  };

  return await API.post(`/comments/guest/${commentId}`, requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const delete_guest_comment_api = async (commentId, password) => {
  return await API.delete(`/comments/guest/${commentId}?password=${password}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
