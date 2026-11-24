# Well Enough (충분히 강녕한가요?)

WHO 2020 신체활동 가이드라인으로 지난 7일 운동량을 진단하고, 부족분을 채우는 주간 플랜과 주간 리포트를 주는 웹서비스입니다.

> 현재 상태: 기획과 디자인을 마치고 개발을 준비하고 있습니다.

## 주요 기능

- 로그인 없이 약 1분 진단 (국제 신체활동 설문 IPAQ 단축형 7문항 + 근력운동 1문항)
- 맞춤 주간 플랜
- 익명 비교 통계 (표본 수와 오차 범위 함께 표시)
- 주간 리포트 (화면 + 이메일)
- 한국어·영어, 다크·라이트 모드, WCAG 2.2 AA 기준

## 기술 스택

| 영역 | 선택 |
| --- | --- |
| 프론트엔드 | Next.js (TypeScript), Vercel |
| 백엔드 | Spring Boot, Railway |
| DB | PostgreSQL, Railway |
| 테스트·리뷰 | JUnit, Vitest, Playwright, GitHub Actions, CodeRabbit |

## 레포 구조

| 폴더 | 내용 |
| --- | --- |
| `frontend/` | Next.js (예정) |
| `backend/` | Spring Boot (예정) |
| `design/` | 디자인 토큰, 와이어프레임 |
| `docs/` | 기획서 |

## 문서

- [PRD](docs/PRD.md)
- [디자인 토큰](design/tokens.css)

## 출처와 라이선스

이 레포의 코드는 [MIT 라이선스](LICENSE)를 따릅니다. 설문 문항은 제외됩니다.

- 설문 문항: [국제 신체활동 설문(IPAQ)](https://sites.google.com/view/ipaq), CC BY 4.0
- 판정 기준: [WHO 2020 신체활동 가이드라인](https://www.ncbi.nlm.nih.gov/books/NBK566048/)

이 서비스의 결과는 의료 조언이 아닙니다.
