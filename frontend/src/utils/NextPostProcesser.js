"use client";
import { useWindow } from "./hooks/useWindow";
import { Initizalize } from "@/utils/reactGA4";

export default function NextPostProcesser({ children }) {
  useWindow();
  Initizalize();

  return <>{children}</>;
}
