"use client";

import NextThemeProvider from "@/utils/NextThemeProvider";
import { CookiesProvider } from "react-cookie";
import { RecoilRoot } from "recoil";
import NextPostProcesser from "./NextPostProcesser";
import ClientToastContainer from "./ToastContainer";
import { AxiosProvider } from "@/api/axios";
import CredentialProvider from "@/utils/CredentialProvider";

export default function NextProvider({ children }) {
  return (
    <RecoilRoot>
      <CookiesProvider>
        <NextThemeProvider>
          <AxiosProvider>
            <CredentialProvider>
              <ClientToastContainer />
              <NextPostProcesser>{children}</NextPostProcesser>
            </CredentialProvider>
          </AxiosProvider>
        </NextThemeProvider>
      </CookiesProvider>
    </RecoilRoot>
  );
}
