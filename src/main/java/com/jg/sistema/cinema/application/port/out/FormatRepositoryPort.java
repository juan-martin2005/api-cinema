package com.jg.sistema.cinema.application.port.out;

import com.jg.sistema.cinema.domain.model.Format;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FormatRepositoryPort {
    List<Format> findAll();
    Optional<Format> findById(Long id);
    Optional<Format> findByName(String name);
    Set<Format> findByIds(Set<Long> ids);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    Format save(Format format);
    void deleteById(Long id);
}
