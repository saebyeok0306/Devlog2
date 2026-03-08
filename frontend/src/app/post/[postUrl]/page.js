import PageTemplate from "@/components/common/pageTemplate";
import HeaderContainer from "@/containers/base/HeaderContainer";
import PostContainer from "@/containers/post/PostContainer";
import { SHORT_BLOG_NAME } from "@/constants/base/main";
import { get_post_exists_api, get_post_metadata_api } from "@/api/posts";
import { notFound } from "next/navigation";

const DEFAULT_METADATA = {
  title: "존재하지 않는 글입니다.",
  description: "존재하지 않는 글입니다.",
  keywords: [],
  author: [], // {name: "이름", url: "주소"}
  openGraph: {
    title: "존재하지 않는 글입니다.",
    description: "존재하지 않는 글입니다.",
    url: "",
    siteName: "devLog",
    images: [],
    locale: "ko_KR",
    type: "website",
  },
};

export async function generateMetadata({ params, searchParams }, parent) {
  const { postUrl } = params;
  const metadata = structuredClone(DEFAULT_METADATA);
  metadata.openGraph.url = `${process.env.NEXT_PUBLIC_BASE_URL}/post/${postUrl}`;
  try {
    const post_metadata = await get_post_metadata_api(postUrl);
    metadata.title = `${post_metadata.title} | ${SHORT_BLOG_NAME}`;
    metadata.openGraph.title = post_metadata.title;
    metadata.description = post_metadata.description;
    metadata.openGraph.description = post_metadata.description;
    metadata.keywords.push(...post_metadata.keywords);
    metadata.author.push({ name: post_metadata.author });
    metadata.openGraph.images.push({ url: post_metadata.previewUrl });
  } catch (err) {
    console.debug(err);
  }
  return metadata;
}

export default async function ServerPage({ params }) {
  const { postUrl } = params;
  try {
    const exists = await get_post_exists_api(postUrl);
    if (exists !== true) {
      return notFound();
    }
  } catch (err) {
    console.debug(err);
    return notFound();
  }
  return (
    <PageTemplate>
      <HeaderContainer />
      <PostContainer />
    </PageTemplate>
  );
}
