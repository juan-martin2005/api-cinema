package com.jg.sistema.cinema.infrastructure.persistence.repositories;

import com.jg.sistema.cinema.infrastructure.persistence.entities.CinemaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PostgreSqlCinemaRepository extends JpaRepository<CinemaEntity, UUID> {
    Optional<CinemaEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    @Query("SELECT c FROM CinemaEntity c WHERE " +
            "(:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
            "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<CinemaEntity> findWithFilter(Pageable pageable, @Param("city") String city, @Param("name") String name);
}
