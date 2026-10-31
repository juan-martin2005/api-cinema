package com.jg.sistema.show_time.application.port.in;

import com.jg.sistema.show_time.domain.model.ShowTime;

public interface CreateShowTimeUseCase {
    void createShowTime(ShowTime showTime);
}
