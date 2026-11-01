-- Compatibility upgrade for databases created by Hibernate and later baselined at V1.
-- Additive and safe on fresh databases where V1 already created these objects.
ALTER TABLE show_times ADD COLUMN IF NOT EXISTS format_id BIGINT REFERENCES formats(id);
ALTER TABLE users ADD COLUMN IF NOT EXISTS session_version BIGINT NOT NULL DEFAULT 0;
ALTER TABLE email_verifications ADD COLUMN IF NOT EXISTS attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE email_verifications ADD COLUMN IF NOT EXISTS sent_at TIMESTAMP;

CREATE TABLE IF NOT EXISTS seat_occupations (
    show_time_id UUID NOT NULL REFERENCES show_times(id),
    seat_id UUID NOT NULL REFERENCES seats(id),
    booking_id UUID NOT NULL REFERENCES bookings(id),
    PRIMARY KEY(show_time_id,seat_id)
);
CREATE INDEX IF NOT EXISTS occupation_booking ON seat_occupations(booking_id);
CREATE TABLE IF NOT EXISTS oauth_identities (
    provider VARCHAR(30) NOT NULL, subject VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL REFERENCES users(id), PRIMARY KEY(provider,subject)
);
CREATE TABLE IF NOT EXISTS oauth_codes (
    code_hash VARCHAR(64) PRIMARY KEY, user_id UUID REFERENCES users(id),
    provider VARCHAR(30), subject VARCHAR(255), email VARCHAR(255), expires_at TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS mail_outbox (
    id UUID PRIMARY KEY, recipient VARCHAR(255) NOT NULL, subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL, attempts INTEGER NOT NULL DEFAULT 0,
    next_attempt TIMESTAMP NOT NULL, sent BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE IF NOT EXISTS auth_limits (
    bucket VARCHAR(255) PRIMARY KEY, attempts INTEGER NOT NULL, expires_at TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS payment_attempts (
    id UUID PRIMARY KEY, booking_id UUID NOT NULL UNIQUE REFERENCES bookings(id),
    session_id VARCHAR(255) UNIQUE, payment_intent_id VARCHAR(255), checkout_url TEXT,
    state VARCHAR(40) NOT NULL, refund_id VARCHAR(255), refund_generation INTEGER NOT NULL DEFAULT 0,
    checkout_expires_at TIMESTAMP NOT NULL, last_error VARCHAR(255), updated_at TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS payment_events (event_id VARCHAR(255) PRIMARY KEY, processed_at TIMESTAMP NOT NULL);

-- Resolve an existing room's format only when its name identifies one unambiguous
-- format available at that cinema, or when the cinema has exactly one format.
WITH candidates AS (
    SELECT st.id, MIN(f.id) AS format_id
    FROM show_times st JOIN cinema_rooms cr ON cr.id=st.cinema_room_id
    JOIN cinema_formats cf ON cf.cinema_id=cr.cinema_id JOIN formats f ON f.id=cf.format_id
    WHERE st.format_id IS NULL AND (
        (cr.name ILIKE '%IMAX%' AND f.name ILIKE '%IMAX%') OR
        (cr.name ILIKE '%4DX%' AND f.name ILIKE '%4DX%') OR
        (cr.name ILIKE '%VIP%' AND f.name ILIKE '%VIP%') OR
        (cr.name ILIKE '%3D%' AND f.name ILIKE '%3D%') OR
        (cr.name ILIKE '%2D%' AND f.name ILIKE '%2D%') OR
        (SELECT COUNT(*) FROM cinema_formats single_format WHERE single_format.cinema_id=cr.cinema_id)=1
    ) GROUP BY st.id HAVING COUNT(*)=1
)
UPDATE show_times st SET format_id=c.format_id FROM candidates c WHERE st.id=c.id;

-- Restore inventory for legacy active bookings. Duplicate/conflicting assignments
-- abort the transaction instead of silently making an already sold seat available.
INSERT INTO seat_occupations(show_time_id,seat_id,booking_id)
SELECT b.show_time_entity_id,s.id,b.id
FROM bookings b JOIN booking_details bd ON bd.booking_id=b.id
JOIN show_times st ON st.id=b.show_time_entity_id
JOIN seats s ON s.room_id=st.cinema_room_id AND CONCAT(s."row",s.number)=bd.seat_label
WHERE b.booking_status IN ('CONFIRMED','USED','PENDING')
AND NOT EXISTS (SELECT 1 FROM seat_occupations o WHERE o.booking_id=b.id AND o.seat_id=s.id);

CREATE UNIQUE INDEX IF NOT EXISTS cinema_name_unique ON cinemas(LOWER(name));
CREATE UNIQUE INDEX IF NOT EXISTS movie_title_unique ON movies(LOWER(title));
CREATE UNIQUE INDEX IF NOT EXISTS legacy_room_name_unique ON cinema_rooms(cinema_id,name);
CREATE UNIQUE INDEX IF NOT EXISTS legacy_seat_label_unique ON seats(room_id,"row",number);
CREATE UNIQUE INDEX IF NOT EXISTS legacy_seat_position_unique ON seats(room_id,pos_x,pos_y);
CREATE UNIQUE INDEX IF NOT EXISTS legacy_verification_email_unique ON email_verifications(email);
CREATE UNIQUE INDEX IF NOT EXISTS legacy_role_name_unique ON roles(name);
CREATE INDEX IF NOT EXISTS showtime_movie_start ON show_times(movie_id,start_time);
CREATE INDEX IF NOT EXISTS showtime_room_start ON show_times(cinema_room_id,start_time);
CREATE INDEX IF NOT EXISTS booking_user_created ON bookings(user_id,creat_at);
