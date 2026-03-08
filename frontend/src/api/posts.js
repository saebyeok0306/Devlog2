import { API } from "./axios";
import { MAIN_URL } from "@/constants/api/url";

export const upload_post_api = async ({ postContext, postUrl, previewUrl }) => {
  const requestBody = {
    url: postUrl,
    title: postContext.title,
    content: postContext.body,
    previewUrl: previewUrl,
    categoryId: postContext.category.id,
    files: postContext.files,
    hidden: postContext.hidden,
  };
  return await API.post("/posts", requestBody, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const edit_post_api = async ({
  postContext,
  postUrl,
  previewUrl,
  modifiedAt,
}) => {
  const requestBody = {
    id: postContext.id,
    url: postUrl,
    title: postContext.title,
    content: postContext.body,
    previewUrl: previewUrl,
    categoryId: postContext.category.id,
    files: postContext.files,
    views: postContext.views,
    hidden: postContext.hidden,
    createdAt: postContext.createdAt,
    modifiedAt: modifiedAt,
  };

  return await API.post("/posts/edit", requestBody, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_posts_api = async (categoryId = null, page = 0, size = 10) => {
  if (categoryId === null || categoryId === 0) {
    return await API.get(`/posts?page=${page}&size=${size}`, {})
      .then((response) => response.data)
      .catch((error) => {
        throw error;
      });
  } else {
    return await API.get(
      `/posts/category/v2/${categoryId}?page=${page}&size=${size}`,
      {}
    )
      .then((response) => response.data)
      .catch((error) => {
        throw error;
      });
  }
};

export const get_inf_posts_api = async (lastId = 0, size = 10) => {
  return await API.get(`/posts/inf?lastId=${lastId}&size=${size}`, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_post_url_api = async (postUrl) => {
  return await API.get(`/posts/${postUrl}`, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_post_files_api = async (postId) => {
  return await API.get(`/files/post/${postId}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const delete_post_api = async (postUrl) => {
  return await API.delete(`/posts/${postUrl}`, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_post_daily_statistics_api = async (postUrl, start, end) => {
  return await API.get(`/views/post/${postUrl}/daily?start=${start}&end=${end}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_post_edit_permission_api = async (postUrl) => {
  return await API.get(`/posts/${postUrl}/permissions`, {})
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const get_post_metadata_api = async (postUrl) => {
  return await fetch(`${MAIN_URL}/posts/${postUrl}/metadata`, {
    next: { revalidate: 60 },
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(res.statusText);
      }
      return res.json();
    })
    .catch((error) => {
      throw error;
    });
};

export const get_post_exists_api = async (postUrl) => {
  return await fetch(`${MAIN_URL}/posts/${postUrl}/exists`, {
    next: { revalidate: 60 },
  })
    .then((res) => {
      if (!res.ok) {
        return false;
      }
      return res.json();
    })
    .catch((error) => false);
};
