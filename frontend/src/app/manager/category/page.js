import PageTemplate from "@/components/common/pageTemplate";
import HeaderContainer from "@/containers/base/HeaderContainer";
import CategoryManagerContainer from "@/containers/main/CategoryManagerContainer";
import { BLOG_DESCRIPTION, SHORT_BLOG_NAME } from "@/constants/base/main";

export const metadata = {
  title: `CategoryManager | ${SHORT_BLOG_NAME}`,
  description: BLOG_DESCRIPTION,
};
export default function ServerPage() {
  return (
    <PageTemplate>
      <HeaderContainer />
      <CategoryManagerContainer />
    </PageTemplate>
  );
}
