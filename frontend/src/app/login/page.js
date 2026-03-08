import PageTemplate from "@/components/common/pageTemplate";
import LoginContainer from "@/containers/login/LoginContainer";
import { BLOG_DESCRIPTION, SHORT_BLOG_NAME } from "@/constants/base/main";

export const metadata = {
  title: `Login | ${SHORT_BLOG_NAME}`,
  description: BLOG_DESCRIPTION,
};
export default function ServerPage() {
  return (
    <PageTemplate>
      <LoginContainer />
    </PageTemplate>
  );
}
