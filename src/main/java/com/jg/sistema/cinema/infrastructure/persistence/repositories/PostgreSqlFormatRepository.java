package com.jg.sistema.cinema.infrastructure.persistence.repositories;

import com.jg.sistema.cinema.infrastructure.persistence.entities.FormatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PostgreSqlFormatRepository extends JpaRepository<FormatEntity, Long> {
    Optional<FormatEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
    Set<FormatEntity> findByIdIn(Set<Long> ids);
}
