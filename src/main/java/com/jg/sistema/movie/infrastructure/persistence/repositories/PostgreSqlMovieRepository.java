package com.jg.sistema.movie.infrastructure.persistence.repositories;

import com.jg.sistema.movie.infrastructure.persistence.entities.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostgreSqlMovieRepository extends JpaRepository<MovieEntity, UUID> {

    @Query("SELECT m FROM MovieEntity m WHERE "+
            "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', CAST(:title AS String), '%'))) AND " +
            "(:genre IS NULL OR LOWER(m.genre) = LOWER(CAST(:genre AS String)))")
    List<MovieEntity> findAllWithFilter(String title, String genre);

    @Query("SELECT m FROM MovieEntity m WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', CAST(:title AS String), '%'))) AND (:genre IS NULL OR LOWER(m.genre) = LOWER(CAST(:genre AS String)))")
    Page<MovieEntity> search(Pageable pageable, String title, String genre);
    boolean existsByTitleIgnoreCase(String title);
}
