# API 명세서

> Base URL: `https://your-server.com`  
> 모든 요청/응답의 Content-Type은 `application/json`

---

## 공통

### 인증 방식
보호된 API는 요청 헤더에 Access Token을 포함해야 합니다.

```
Authorization: Bearer {accessToken}
```

### 공통 에러 응답

```json
{
  "status": 401,
  "message": "인증이 필요합니다."
}
```

| HTTP Status | 설명 |
|---|---|
| `400` | 잘못된 요청 (유효성 검사 실패 등) |
| `401` | 인증 실패 (토큰 없음, 만료, 잘못된 토큰) |
| `403` | 권한 없음 (인증은 됐지만 접근 불가) |

---

## Auth

### 회원가입

```
POST /api/auth/signup
```

**Request Body**

```json
{
  "name": "홍길동",
  "email": "user@example.com",
  "password": "pass1234!"
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `name` | String | O | 이름 (한글 석 자 등) |
| `email` | String | O | 이메일 형식 |
| `password` | String | O | 8자 이상 |

**Response `201 Created`**

```json
{
  "id": 1,
  "name": "홍길동",
  "email": "user@example.com"
}
```

**Error**

| Status | 메시지 | 조건 |
|---|---|---|
| `400` | 이미 사용 중인 이메일입니다. | 중복 이메일 |
| `400` | 입력값이 올바르지 않습니다. | 유효성 검사 실패 |

---

### 로그인

```
POST /api/auth/login
```

**Request Body**

```json
{
  "email": "user@example.com",
  "password": "pass1234!"
}
```

| 필드 | 타입 | 필수 | 설명 |
|---|---|---|---|
| `email` | String | O | 가입한 이메일 |
| `password` | String | O | 비밀번호 |

**Response `200 OK`**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

| 필드 | 설명 |
|---|---|
| `accessToken` | 유효기간 24시간, API 호출 시 사용 |
| `refreshToken` | 유효기간 14일, Access Token 재발급 시 사용 |

**Error**

| Status | 메시지 | 조건 |
|---|---|---|
| `401` | 이메일 또는 비밀번호가 올바르지 않습니다. | 로그인 실패 |

---

### Access Token 재발급

```
POST /api/auth/reissue
```

**Request Body**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response `200 OK`**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

> Refresh Token도 함께 재발급 (Rotate 방식)

**Error**

| Status | 메시지 | 조건 |
|---|---|---|
| `401` | 유효하지 않은 리프레시 토큰입니다. | 만료 또는 변조 |

---

## Member

### 내 정보 조회 (마이페이지)

```
GET /api/members/me
```

**Request Header**

```
Authorization: Bearer {accessToken}
```

**Response `200 OK`**

```json
{
  "id": 1,
  "name": "홍길동",
  "email": "user@example.com"
}
```

**Error**

| Status | 메시지 | 조건 |
|---|---|---|
| `401` | 인증이 필요합니다. | 토큰 없음 또는 만료 |
| `403` | 접근 권한이 없습니다. | 권한 부족 |

---

## 엔드포인트 요약

| 메서드 | URL | 인증 | 설명 |
|---|---|---|---|
| `POST` | `/api/auth/signup` | 불필요 | 회원가입 |
| `POST` | `/api/auth/login` | 불필요 | 로그인 |
| `POST` | `/api/auth/reissue` | 불필요 | Access Token 재발급 |
| `GET` | `/api/members/me` | 🔒 필요 | 내 정보 조회 |
