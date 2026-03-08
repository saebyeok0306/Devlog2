import { ThemeModeScript } from "flowbite-react";
import "./globals.css";
import "@/styles/main.scss";
import "@/styles/base/font.css";

import NextProvider from "@/utils/NextProvider";
import FooterContainer from "@/containers/base/FooterContainer";
import { BLOG_DESCRIPTION, SHORT_BLOG_NAME } from "@/constants/base/main";

export const revalidate = 3600; // fetch cache 1 hour
export const metadata = {
  title: `${SHORT_BLOG_NAME}`,
  description: BLOG_DESCRIPTION,
};

export default function RootLayout({ children }) {
  return (
    <html suppressHydrationWarning lang="ko">
      <head>
        <ThemeModeScript />
        <script
          dangerouslySetInnerHTML={{
            __html: `
              const theme = localStorage.getItem("theme");
              const systemTheme = window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
              const resolvedTheme = theme === "system" ? systemTheme : theme;
              document.documentElement.setAttribute("data-theme", resolvedTheme);
              document.documentElement.classList.add(resolvedTheme);
              document.documentElement.style.colorScheme = resolvedTheme;
            `,
          }}
        ></script>
      </head>
      <body>
        <NextProvider>
          <div className={`wrapper`}>
            <div className={`contentWrapper`}>{children}</div>
            <FooterContainer />
          </div>
        </NextProvider>
      </body>
    </html>
  );
}
