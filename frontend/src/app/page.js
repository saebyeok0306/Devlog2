import PageTemplate from "@/components/common/pageTemplate";
import HeaderContainer from "@/containers/base/HeaderContainer";
import MainContainer from "@/containers/main/MainContainer";

export default function ServerHome() {
  return (
    <PageTemplate>
      <HeaderContainer />
      <MainContainer />
    </PageTemplate>
  );
}
