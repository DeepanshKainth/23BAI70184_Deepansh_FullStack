# Experiment 9: Secure and Scalable Full Stack System

This project demonstrates a secure and scalable full-stack application using Spring Boot, Spring Security, Google OAuth2 login, RBAC, and JPA performance-oriented data access.

## What it shows

- Spring Security filter-chain setup with stateless JWT authentication
- Authentication with local users and Google OAuth2
- RBAC-protected endpoints with `@PreAuthorize`
- Optimized JPA list APIs for scalability:
	- offset pagination (`/api/polls/optimized`)
	- keyset/cursor pagination (`/api/polls/optimized/cursor`)
	- status filtering (`OPEN`/`CLOSED`)
- Indexed entities for frequent query patterns
- React frontend connected to secured backend over CORS

## Project layout

- `backend/` - Spring Boot API and security configuration
- `frontend/` - React UI loaded from `index.html`

## Default demo users

| Username | Password    | Role |
|----------|-------------|------|
| alice    | password123 | ADMIN |
| bob      | password123 | USER |
| carol    | password123 | USER |

## How to run

1. Start the backend from `backend/src/main/java/com/classroom/security/SecurityDemoApplication.java`.
2. Open `frontend/index.html` in a browser or serve the folder with a local static server.
3. Sign in with a demo user or use Google login after configuring OAuth credentials.

## Google OAuth setup

Set these environment variables before starting the backend:

- `GOOGLE_CLIENT_ID`
- `GOOGLE_CLIENT_SECRET`

The backend maps a successful Google login to a JWT session and sends the tokens back to the React app through a popup bridge.

## API flow

1. User signs in with a password or Google.
2. Backend validates the identity and issues JWT tokens.
3. Frontend stores the tokens in `localStorage`.
4. Requests go through the JWT filter chain.
5. RBAC decides whether the request is allowed.

## Main endpoints

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `GET /api/me`
- `GET /api/polls`
- `GET /api/polls/optimized?page=0&size=10&status=OPEN`
- `GET /api/polls/optimized/cursor?size=10&status=OPEN&cursorCreatedAt=...&cursorId=...`
- `POST /api/polls`
- `POST /api/polls/{id}/vote`
- `GET /api/polls/{id}/results`
- `DELETE /api/polls/{id}`

## Notes

- H2 is used for the poll database.
- Google OAuth will only complete if real Google client credentials are provided.
- Admin actions are restricted to `ROLE_ADMIN`.
- Frontend includes mode switching: standard, paged, and cursor list retrieval.
