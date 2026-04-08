# SeatSurfer

SeatSurfer is a full-stack office seat reservation platform built from the supplied vision document.
It includes:

- A plain `HTML` + `CSS` + `JavaScript` frontend in [`frontend`](/Users/matei/SeatSurferDemo/frontend)
- A `Spring Boot` REST API in [`backend`](/Users/matei/SeatSurferDemo/backend)
- SQL-backed persistence through Flyway migrations in [`backend/src/main/resources/db/migration`](/Users/matei/SeatSurferDemo/backend/src/main/resources/db/migration)
- Admin OTP login, floor and seat-type management, floor layout replacement, public seat booking, occupancy reporting, PDF export, and monthly report email automation

## What’s implemented

- Pre-seeded admins: `adi`, `gratiela`, `silviu`
- Default admin password: `SeatSurfer!2026`
- OTP-based admin login flow
  - If SMTP is not configured, the OTP is exposed in the login response for local development
- Seat types from the vision document
  - Standard
  - Standing desk
  - Standing desk without chair
  - Desk only
- Public browser workflow
  - Pick a date
  - Inspect free and booked seats
  - See seat type colors
  - Hover for booked-user info
  - Book and unbook a seat
- Admin workflow
  - Create, update, deactivate floors
  - Create, update, deactivate seat types
  - Replace a floor layout effective from a future date
  - Automatically cancel future bookings on removed seats
  - View daily and range-based occupancy
  - Export a PDF management report
  - Email the previous month’s report on a schedule or on demand

## Local run

### 1. Run the backend

```bash
cd /Users/matei/SeatSurferDemo/backend
mvn -Dmaven.repo.local=/Users/matei/SeatSurferDemo/.m2 spring-boot:run
```

The backend starts on `http://localhost:8080`.

Useful environment variables:

```bash
export APP_JWT_SECRET="replace-with-a-long-random-secret"
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/seatsurfer"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"
export SPRING_MAIL_HOST="smtp.example.com"
export SPRING_MAIL_PORT="587"
export SPRING_MAIL_USERNAME="smtp-user"
export SPRING_MAIL_PASSWORD="smtp-password"
export SPRING_MAIL_SMTP_AUTH="true"
export SPRING_MAIL_STARTTLS="true"
export APP_REPORTING_EMAIL_ENABLED="true"
export APP_REPORTING_RECIPIENTS="ops@example.com,facilities@example.com"
```

### 2. Serve the frontend

```bash
cd /Users/matei/SeatSurferDemo/frontend
python3 -m http.server 4173
```

Open `http://localhost:4173`.

By default, [`config.js`](/Users/matei/SeatSurferDemo/frontend/assets/config.js) points local frontend requests at `http://localhost:8080`.

## SQL and database

The schema lives in:

- [`V1__initial_schema.sql`](/Users/matei/SeatSurferDemo/backend/src/main/resources/db/migration/V1__initial_schema.sql)

Flyway runs automatically on startup. For local development the app defaults to H2 in PostgreSQL compatibility mode. For production, point the backend to PostgreSQL through `SPRING_DATASOURCE_*`.

## Vercel deployment

The frontend is ready to deploy to Vercel from [`frontend`](/Users/matei/SeatSurferDemo/frontend).

### Frontend on Vercel

1. Create a Vercel project with the root directory set to [`frontend`](/Users/matei/SeatSurferDemo/frontend)
2. Add a `BACKEND_URL` environment variable that points to your deployed Spring Boot API
3. Deploy

[`frontend/vercel.json`](/Users/matei/SeatSurferDemo/frontend/vercel.json) proxies `/api/*` requests from the Vercel-hosted frontend to the Spring Boot backend.

### Backend hosting note

This repository keeps the backend in Spring Boot exactly as requested. In practice, the clean deployment path is:

- Vercel for the static frontend
- A Java-friendly host for the Spring Boot API and PostgreSQL database

That keeps the architecture correct while still giving you a Vercel-hosted app entrypoint.

## Key API routes

Public:

- `GET /api/public/floors?date=YYYY-MM-DD`
- `GET /api/public/floors/{floorId}/availability?date=YYYY-MM-DD&viewerName=...`
- `POST /api/public/bookings`
- `POST /api/public/bookings/{bookingId}/cancel`
- `GET /api/public/reports/daily?date=YYYY-MM-DD`

Admin:

- `POST /api/auth/login`
- `POST /api/auth/verify-otp`
- `GET /api/auth/me`
- `GET /api/admin/floors`
- `POST /api/admin/floors`
- `PUT /api/admin/floors/{floorId}`
- `DELETE /api/admin/floors/{floorId}`
- `GET /api/admin/seat-types`
- `POST /api/admin/seat-types`
- `PUT /api/admin/seat-types/{seatTypeId}`
- `DELETE /api/admin/seat-types/{seatTypeId}`
- `GET /api/admin/floors/{floorId}/layout?date=YYYY-MM-DD`
- `PUT /api/admin/floors/{floorId}/layout`
- `GET /api/admin/reports/daily?date=YYYY-MM-DD`
- `GET /api/admin/reports/load?from=YYYY-MM-DD&to=YYYY-MM-DD`
- `GET /api/admin/reports/monthly?year=YYYY&month=MM`
- `GET /api/admin/reports/export?date=YYYY-MM-DD&from=YYYY-MM-DD&to=YYYY-MM-DD`
- `POST /api/admin/reports/monthly/email?year=YYYY&month=MM`
