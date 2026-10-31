package com.jg.sistema.cinema.application.service;

import com.jg.sistema.cinema.application.port.in.FormatUseCase;
import com.jg.sistema.cinema.application.port.out.FormatRepositoryPort;
import com.jg.sistema.cinema.domain.exception.FormatAlreadyExistsException;
import com.jg.sistema.cinema.domain.exception.FormatNotFoundException;
import com.jg.sistema.cinema.domain.model.Format;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormatService implements FormatUseCase {

    private final FormatRepositoryPort formatRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public List<Format> getAllFormats() {
        return formatRepositoryPort.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Format getFormatById(Long id) {
        return formatRepositoryPort.findById(id)
                .orElseThrow(() -> new FormatNotFoundException("Format not found with id: " + id));
    }

    @Override
    @Transactional
    public Format createFormat(Format format) {
        if (formatRepositoryPort.existsByName(format.name())) {
            throw new FormatAlreadyExistsException("Format with name '" + format.name() + "' already exists");
        }
        return formatRepositoryPort.save(format);
    }

    @Override
    @Transactional
    public Format updateFormat(Long id, Format format) {
        if (formatRepositoryPort.findById(id).isEmpty()) {
            throw new FormatNotFoundException("Format not found with id: " + id);
        }
        if (formatRepositoryPort.existsByNameAndIdNot(format.name(), id)) {
            throw new FormatAlreadyExistsException("Format with name '" + format.name() + "' already exists");
        }
        Format updated = new Format(id, format.name(), format.price());
        return formatRepositoryPort.save(updated);
    }

    @Override
    @Transactional
    public void deleteFormat(Long id) {
        if (formatRepositoryPort.findById(id).isEmpty()) {
            throw new FormatNotFoundException("Format not found with id: " + id);
        }
        formatRepositoryPort.deleteById(id);
    }
}
