import HeaderContainer from "@/containers/base/HeaderContainer";
import PageTemplate from "@/components/common/pageTemplate";

export default function NotFound() {
  return (
    <PageTemplate>
      <HeaderContainer />
      <div
        style={{
          textAlign: "center",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          flex: 1,
        }}
      >
        <div>
          <h1
            className="next-error-h1 border-r border-gray-500 dark:border-gray-400"
            style={{
              display: "inline-block",
              margin: "0px 20px 0px 0px",
              padding: "0px 23px 0px 0px",
              fontSize: "24px",
              fontWeight: 500,
              verticalAlign: "top",
              lineHeight: "49px",
            }}
          >
            404
          </h1>
          <div style={{ display: "inline-block" }}>
            <h2
              style={{
                fontSize: "14px",
                fontWeight: 400,
                lineHeight: "49px",
                margin: 0,
              }}
            >
              This page could not be found.
            </h2>
          </div>
        </div>
      </div>
    </PageTemplate>
  );
}
