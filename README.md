# 팀번호 - 이름(FE), 이름(BE)

**3 - 최나현(FE), 남연서(BE)**

---

## 🛠 무엇을 어떻게 구현했나요?

### 필수 요구사항

| 기능 | 엔드포인트 | 설명 |
|------|-----------|------|
| 회원가입 | `POST /api/auth/signup` | 이메일 중복 검사 후 BCrypt로 비밀번호 암호화하여 저장 |
| 로그인 | `POST /api/auth/login` | 이메일/비밀번호 검증 후 Access Token + Refresh Token 발급 |
| 토큰 재발급 | `POST /api/auth/reissue` | Refresh Token 검증 후 두 토큰 모두 재발급 (Refresh Token Rotation) |
| 내 정보 조회 | `GET /api/members/me` | JWT 인증 통과 시 로그인된 회원 정보 반환 |

---

## 🤔 왜 이렇게 설계/구현했나요?

### 1. FilterChain 이중 분리 (`@Order(1)` / `@Order(2)`)

Spring Security 7에서 단일 FilterChain으로 구성할 경우, `permitAll()`로 열어둔 경로가 JWT 필터를 통과할 때 토큰이 없으면 예외가 발생하는 문제가 있었다.
예를 들어 `/api/auth/login`은 인증이 필요 없는 경로임에도 JWT 필터가 먼저 실행되어 401을 반환했다.

이를 해결하기 위해 FilterChain을 두 개로 분리했다.

- **`@Order(1)` 공개 체인** — `/api/auth/**`, `/swagger-ui/**`, `/error` 등 인증이 필요 없는 경로만 담당하며 JWT 필터 자체를 등록하지 않는다.
- **`@Order(2)` 보안 체인** — 나머지 모든 경로를 담당하며 JWT 필터가 항상 실행된다.

> `/error`를 공개 체인에 포함한 이유는 서버 내부 오류(500) 발생 시 Spring이 `/error`로 포워딩하는데, 이 경로가 보안 체인에 걸리면 오류가 401로 둔갑하기 때문이다.

---

### 2. 401 vs 403 반환 기준

| 코드 | 의미 | 처리 핸들러 |
|------|------|------------|
| **401** Unauthorized | 토큰이 없거나 유효하지 않아 신원 확인 자체가 불가능한 경우 | `CustomAuthenticationEntryPoint` |
| **403** Forbidden | 토큰은 유효하지만 해당 리소스에 접근할 권한이 없는 경우 | `CustomAccessDeniedHandler` |

두 핸들러를 분리한 이유는 클라이언트 입장에서 **"로그인이 필요한가(401)"** vs **"로그인했지만 권한이 없는가(403)"** 를 구분해야 올바른 UX 처리(로그인 페이지로 이동 vs 접근 거부 안내)가 가능하기 때문이다.

---

### 3. Refresh Token Rotation

토큰 재발급 시 Access Token만 새로 발급하지 않고 Refresh Token도 함께 새로 발급하여 DB에 저장한다.

이렇게 한 이유는 Refresh Token이 탈취당했을 때의 피해를 최소화하기 위해서다.

- Rotation을 적용하면 탈취된 Refresh Token으로 재발급을 시도하는 순간 DB의 토큰과 불일치하여 즉시 차단된다.
- 정상 사용자가 먼저 재발급을 받으면 이전 토큰은 무효화되므로 공격자가 구 토큰을 재사용할 수 없다.

---

### 4. JWT 검증 흐름
1. 요청이 들어오면 JwtAuthenticationFilter가 UsernamePasswordAuthenticationFilter 이전에 실행된다.
2. Authorization 헤더에서 Bearer 토큰을 추출한다.
3. JwtTokenProvider.validateToken()으로 서명과 만료 여부를 검증한다.
4. 유효하면 토큰에서 이메일을 꺼내 CustomUserDetailsService로 사용자를 조회한다.
5. UsernamePasswordAuthenticationToken을 생성해 SecurityContextHolder에 저장한다.
6. 이후 컨트롤러에서 @AuthenticationPrincipal로 현재 사용자 정보에 접근할 수 있다.

> 토큰이 없거나 유효하지 않으면 필터를 그냥 통과시키고, 이후 보안 체인의 `authorizeHttpRequests`에서 인증되지 않은 요청으로 판단하여 `CustomAuthenticationEntryPoint`가 401을 반환한다.

---

## 🤝 협업 기록

### API 명세

https://202614thfinalstudybe-production-0389.up.railway.app/swagger-ui/index.html

---

## 연동 중 발생한 이슈

### 1. CORS

**문제**

프론트(Vercel)에서 백엔드로 요청 시 CORS 오류가 발생했다. 브라우저는 다른 출처(Origin)로의 요청을 기본적으로 차단하기 때문에, 백엔드에서 허용할 출처를 명시적으로 설정해야 한다.

**해결**

`SecurityConfig`에서 `CorsConfigurationSource`를 빈으로 등록하고 Vercel 배포 도메인을 `allowedOriginPatterns`에 추가하여 해결했다.
`allowedOrigins` 대신 `allowedOriginPatterns`를 사용한 이유는 `allowCredentials(true)`와 함께 와일드카드(`*`)를 사용할 수 없기 때문이다.

```java
config.setAllowedOriginPatterns(List.of(
    "https://2026-14th-final-study-fe-nu.vercel.app",
    "http://localhost:3000"
));
config.setAllowCredentials(true);
```

---

### 2. Vercel → 백엔드 502 타임아웃

**문제**

Vercel에서 백엔드로 요청 시 응답 없이 타임아웃이 발생하며 502 오류가 반환됐다.
Vercel 에러 헤더에 `X-Vercel-Error: ROUTER_EXTERNAL_TARGET_ERROR`가 포함되어 있었고, 이는 Vercel이 외부 백엔드에 연결하지 못했다는 의미였다.

**원인**

EC2 보안그룹에서 8080 포트가 열려있지 않아 패킷이 드롭되어 타임아웃이 발생했다.
연결 거부(즉시 응답)가 아닌 무응답 타임아웃으로 나타난 것이 단서였다.

**해결**

AWS 콘솔에서 보안그룹 인바운드 규칙에 TCP 8080 / 0.0.0.0/0 추가 후 해결됐다.
이후 Railway로 배포를 이전하여 EC2 메모리 한계 문제도 함께 해결했다.


---

## 💭 고민했던 점 / 아직 모르겠는 점 / 어려웠던 점

t3.micro 환경에서 메모리 제한으로 인해 배포 안정성을 확보하기 어려웠다. 요청이 들어올 때마다 서버가 느려지거나 SSH 접속까지 끊기는 상황이 반복되어 개발과 디버깅에 어려움이 있었다.

Oracle Cloud로 이전을 시도했으나 ARM 아키텍처용 무료 인스턴스(A1 Flex)의 가용 용량이 없어 생성이 불가능했다. Oracle Cloud는 특정 리전에서 프리티어 ARM 인스턴스 용량이 항상 부족한 상태로, 원하는 시점에 인스턴스를 생성할 수 없다는 점이 문제였다.

최종적으로 Railway로 배포를 이전하여 메모리 문제를 해결했지만, 클라우드 서비스마다 네트워크 설정 방식이 달라 내부 네트워크(Private Networking) 연결에서 시행착오가 있었다. Railway에서 같은 프로젝트 내에 있어야 내부 네트워크로 통신이 가능하다는 것을 배웠다.

---

## 🔥 트러블슈팅

### 1. signup 요청에서 401 반환

**문제**

`/api/auth/signup`은 인증이 필요 없는 공개 경로임에도 401이 반환됨

**원인**

signup 자체는 Security에 막히지 않았다. 문제는 컨트롤러 내부에서 예외가 발생했을 때였다.
Spring Boot는 내부 오류 발생 시 자동으로 `GET /error`로 포워딩하는데, 이 `/error` 경로가 `permitAll()` 목록에 없어 보안 체인에 걸렸다.
결과적으로 진짜 오류(500) 대신 `CustomAuthenticationEntryPoint`가 401을 반환했다.
POST /api/auth/signup
→ Spring Security 통과 (문제 없음)
→ 컨트롤러에서 예외 발생 (500)
→ Spring Boot가 GET /error 로 포워딩
→ /error 가 보안 체인에 걸림
→ CustomAuthenticationEntryPoint → 401 반환

처음엔 "왜 signup이 401이지?"라고 착각했지만, 실제로는 signup 이후 발생한 오류를 보여주려다 막힌 것이었다.

**해결**

| 변경 | 이유 |
|------|------|
| SwaggerConfig 전역 `addSecurityItem` 제거 | Swagger가 모든 요청에 Bearer 토큰을 강제하던 것 제거 |
| SecurityConfig를 두 개의 FilterChain으로 분리 | Spring Security 7에서 단일 체인 `permitAll()` 신뢰도 문제 |
| `/error`를 공개 경로에 추가 | 에러 포워딩 경로가 막혀 진짜 오류 대신 401이 반환된 핵심 원인 |

---

### 2. EC2 인스턴스 응답 없음

**문제**

배포 후 SSH 접속도 안 되고 앱도 응답하지 않음

**원인**

t3.micro(RAM 1GB)에서 Java 25 + Spring Boot 실행 시 메모리 부족으로 인스턴스가 멈추거나 SSH 접속이 끊기는 현상이 반복됐다. 프론트 요청이 들어올 때마다 JVM 힙 확장으로 RAM이 초과되어 스왑을 사용하게 되고, API 응답이 Vercel 타임아웃을 초과하여 502가 반환됐다.

**해결**

Railway로 배포를 이전했다. Railway는 메모리 제한이 없어 EC2 t3.micro의 한계를 근본적으로 해결했다.

---

## 🖥️ 배포 URL

| | URL |
|--|-----|
| **프론트엔드** | [바로가기](https://2026-14th-final-study-fe-nu.vercel.app) |
| **백엔드** | [바로가기](https://202614thfinalstudybe-production-0389.up.railway.app) |
