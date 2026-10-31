package com.jg.sistema.shared.infrastructure.persistence;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import com.jg.sistema.shared.application.port.CoordinationPort;
@Repository @RequiredArgsConstructor
public class JdbcCoordination implements CoordinationPort {
    private final JdbcTemplate jdbc;
    private final java.time.Clock clock;
    private void lock(String table, UUID id) {
        if (jdbc.queryForList("SELECT id FROM " + table + " WHERE id=? FOR UPDATE", id).isEmpty())
            throw new ResponseStatusException(NOT_FOUND, "Resource not found");
    }
    public void lockShowTime(UUID id) { lock("show_times", id); }
    public void lockRoom(UUID id) { lock("cinema_rooms", id); }
    public void lockMovie(UUID id) { lock("movies", id); }
    public void occupy(UUID showTime, List<UUID> seats, UUID booking) {
        for(UUID seat:seats) jdbc.update("INSERT INTO seat_occupations(show_time_id,seat_id,booking_id) VALUES (?,?,?)",showTime,seat,booking);
    }
    public boolean hasBookings(UUID id) { return jdbc.queryForObject("SELECT COUNT(*) FROM bookings WHERE show_time_entity_id=? AND booking_status IN ('PENDING','CONFIRMED','USED')", Long.class,id)>0; }
    public boolean hasFutureShowTimes(UUID id) { return jdbc.queryForObject("SELECT COUNT(*) FROM show_times WHERE movie_id=? AND end_time>?",Long.class,id,java.time.LocalDateTime.now(clock))>0; }
}
