import mem from "mem";
import { API } from "./axios";
import { REFRESH_STORE } from "./cache";

export const user_join_api = async (username, password, email) => {
  const requestBody = {
    username: username,
    password: password,
    email: email,
  };
  return await API.post("/join", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const user_login_api = async (email, password) => {
  const requestBody = {
    email: email,
    password: password,
  };
  return await API.post("/login", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const user_check_api = async () => {
  return await API.get("/check")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const user_profile_api = async () => {
  return await API.get("/profile")
    .then((response) => response)
    .catch((error) => {
      throw error;
    });
};

export const user_logout_api = async () => {
  return await API.get("/signout")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const jwt_refresh_api = mem(
  async (email = null) => {
    return await API.get("/reissue")
      .then((response) => response.data)
      .catch((error) => {
        throw error;
      });
  },
  // 유저별로 캐시를 따로 관리하기 위해 email을 cacheKey로 사용
  { maxAge: 1000, cacheKey: (args) => args[0], cache: REFRESH_STORE }
);

export const renew_password_api = async ({ currentPassword, newPassword }) => {
  const requestBody = {
    currentPassword: currentPassword,
    newPassword: newPassword,
  };
  return await API.put("/profile/password", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const request_verify_email_api = async () => {
  return await API.get("/profile/verify-email")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const send_verify_email_api = async (code) => {
  return await API.post(`/profile/verify-email/${code}`)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const upload_profile_url_api = async (file, profileUrl) => {
  const requestBody = {
    file: file,
    profileUrl: profileUrl,
  };

  return await API.post("/profile/picture", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const delete_profile_url_api = async () => {
  return await API.delete("/profile/picture")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const verify_captcha_api = async (token) => {
  const requestBody = {
    token: token,
  };
  return await API.post("/captcha/verify", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const has_jwt_cookie_api = async () => {
  return await API.get("/jwt")
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};

export const upload_user_profile_api = async (username, about) => {
  const requestBody = {
    username: username,
    about: about,
  };
  return await API.post("/profile", requestBody)
    .then((response) => response.data)
    .catch((error) => {
      throw error;
    });
};
