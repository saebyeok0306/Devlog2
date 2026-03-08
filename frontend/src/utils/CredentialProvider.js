import { useRecoilState } from "recoil";
import { authAtom } from "@/recoil/authAtom";
import { useEffect } from "react";
import {
  has_jwt_cookie_api,
  jwt_refresh_api,
  user_profile_api,
} from "@/api/user";
import { signOutProcess } from "@/utils/authenticate";

export default function CredentialProvider({ children }) {
  const [authDto, setAuthDto] = useRecoilState(authAtom);

  useEffect(() => {
    const checkUserdata = async () => {
      if (authDto?.isLogin) return;
      try {
        const jwt = await has_jwt_cookie_api();
        if (!jwt) return;
        await jwt_refresh_api();
        const result = await user_profile_api();
        const payload = result.data;

        if (result.status === 200) {
          setAuthDto((prev) => ({
            ...prev,
            username: payload.username,
            email: payload.email,
            about: payload.about,
            isLogin: true,
            role: payload.role,
            profileUrl: payload.profileUrl,
            provider: payload.provider,
            certificate: payload.certificate,
          }));
        } else if (result.status === 204) {
          return;
        }
      } catch (error) {
        if (error?.message === "Network Error") {
          console.error("server maintenance:", error);
          // setMaintenance(true);
          return;
        }
        await signOutProcess(setAuthDto);
        // await warnSignOut(setAuthDto, "로그인이 만료되었습니다.");
        return;
      }
    };

    checkUserdata();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return <>{children}</>;
}
