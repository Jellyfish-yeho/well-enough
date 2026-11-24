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
| `frontend/` | Next.js |
| `backend/` | Spring Boot |
| `design/` | 디자인 토큰, 와이어프레임 |
| `docs/` | 기획서 |

## 로컬 실행 방법

필요한 것: Node.js 26, Java 25(`JAVA_HOME`이 JDK 25를 가리켜야 합니다), [Docker Desktop](https://docs.docker.com/desktop/).

### 1. 접속 정보 파일 만들기

레포 루트에서 한 번만 실행합니다. `.env`는 Git에 올라가지 않습니다.

```bash
cp .env.example .env
```

8080 포트를 다른 프로그램이 쓰고 있으면 `.env`의 `BACKEND_PORT`를 바꿉니다.

### 2. DB 켜기

Docker Desktop을 켠 뒤 레포 루트에서 실행합니다.

```bash
docker compose up -d --wait
```

| 명령 | 하는 일 |
| --- | --- |
| `docker compose ps` | 상태 보기 |
| `docker compose stop` | 끄기 (데이터 유지) |
| `docker compose down` | 컨테이너 삭제 (데이터 유지) |
| `docker compose down -v` | 컨테이너와 **데이터까지 삭제** |

`.env`의 DB 이름·사용자·비밀번호는 DB를 처음 만들 때만 적용됩니다. 나중에 바꾸려면 데이터를 삭제하고 다시 켜야 합니다.

DBeaver 같은 도구로 접속할 때는 호스트 `localhost`와 `.env`의 값을 씁니다.

### 3. 백엔드

```bash
cd backend
./gradlew bootRun --args='--spring.profiles.active=local'
```

| 명령 | 하는 일 |
| --- | --- |
| `./gradlew test` | 테스트 (Docker가 켜져 있어야 합니다. 로컬 DB와 별도의 테스트용 DB를 씁니다) |
| `./gradlew build` | 테스트 + 배포용 jar 생성 |

헬스 체크: `GET /actuator/health`

### 4. 프론트엔드

```bash
cd frontend
npm ci
npm run dev
```

| 명령 | 하는 일 |
| --- | --- |
| `npm run lint` | 코드 규칙 검사 |
| `npm run typecheck` | 타입 검사 |
| `npm run test:run` | 단위 테스트 1회 실행 |
| `npm run build` | 배포용 빌드 |

## 문서

- [PRD](docs/PRD.md)
- [디자인 토큰](design/tokens.css)

## 출처와 라이선스

이 레포의 코드는 [MIT 라이선스](LICENSE)를 따릅니다. 설문 문항은 제외됩니다.

- 설문 문항: [국제 신체활동 설문(IPAQ)](https://sites.google.com/view/ipaq), CC BY 4.0
- 판정 기준: [WHO 2020 신체활동 가이드라인](https://www.ncbi.nlm.nih.gov/books/NBK566048/)

이 서비스의 결과는 의료 조언이 아닙니다.
