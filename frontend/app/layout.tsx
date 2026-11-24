import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "충분히 강녕한가요? | Well Enough",
  description: "지난 7일 운동량이 WHO 권장 기준을 채웠는지 1분 만에 확인해요.",
};

export default function RootLayout({ children }: LayoutProps<"/">) {
  return (
    <html lang="ko">
      <body>{children}</body>
    </html>
  );
}
