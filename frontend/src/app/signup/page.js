import PageTemplate from "@/components/common/pageTemplate";
import SignupContainer from "@/containers/signup/SignupContainer";
import { BLOG_DESCRIPTION, SHORT_BLOG_NAME } from "@/constants/base/main";

export const metadata = {
  title: `Signup | ${SHORT_BLOG_NAME}`,
  description: BLOG_DESCRIPTION,
};

export default function ServerPage() {
  return (
    <PageTemplate>
      <SignupContainer />
    </PageTemplate>
  );
}
