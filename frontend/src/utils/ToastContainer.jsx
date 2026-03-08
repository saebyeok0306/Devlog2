"use client";
import { ToastContainer } from "react-toastify";
import { useTheme } from "next-themes";

function ClientToastContainer() {
  const { resolvedTheme } = useTheme();
  return (
    <ToastContainer
      position="top-center"
      autoClose={2000}
      hideProgressBar={true}
      newestOnTop={false}
      closeOnClick
      rtl={false}
      pauseOnFocusLoss={false}
      draggable
      theme={`${resolvedTheme == "dark" ? "dark" : "light"}`}
    />
  );
}

export default ClientToastContainer;
