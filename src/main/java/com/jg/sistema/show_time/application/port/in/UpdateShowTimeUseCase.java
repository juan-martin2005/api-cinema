package com.jg.sistema.show_time.application.port.in;

import com.jg.sistema.show_time.domain.model.ShowTime;

import java.util.UUID;

public interface UpdateShowTimeUseCase {
    void updateShowTime(UUID id, ShowTime showTime);
}
