import PageTemplate from "@/components/common/pageTemplate";
import HeaderContainer from "@/containers/base/HeaderContainer";
import PostEditorContainer from "@/containers/editor/PostEditorContainer";
import { BLOG_DESCRIPTION, SHORT_BLOG_NAME } from "@/constants/base/main";

export const metadata = {
  title: `Editor | ${SHORT_BLOG_NAME}`,
  description: BLOG_DESCRIPTION,
};
export default function ServerPage() {
  return (
    <PageTemplate>
      <HeaderContainer />
      <PostEditorContainer />
    </PageTemplate>
  );
}
