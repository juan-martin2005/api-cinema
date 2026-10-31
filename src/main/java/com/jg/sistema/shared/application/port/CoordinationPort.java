package com.jg.sistema.shared.application.port;
import java.util.*;
public interface CoordinationPort {
    void lockShowTime(UUID id);
    void lockRoom(UUID id);
    void lockMovie(UUID id);
    void occupy(UUID showTime, List<UUID> seats, UUID booking);
    boolean hasBookings(UUID showTime);
    boolean hasFutureShowTimes(UUID movie);
}
