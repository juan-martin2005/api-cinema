package com.jg.sistema.cinema.application.port.in;

import com.jg.sistema.cinema.domain.model.Format;

import java.util.List;

public interface FormatUseCase {
    List<Format> getAllFormats();
    Format getFormatById(Long id);
    Format createFormat(Format format);
    Format updateFormat(Long id, Format format);
    void deleteFormat(Long id);
}
