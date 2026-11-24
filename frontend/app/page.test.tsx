import { render, screen } from "@testing-library/react";
import { expect, test } from "vitest";
import Home from "./page";

test("랜딩 화면에 서비스 이름이 제목으로 보인다", () => {
  render(<Home />);

  expect(
    screen.getByRole("heading", { level: 1, name: "충분히 강녕한가요?" }),
  ).toBeDefined();
});
