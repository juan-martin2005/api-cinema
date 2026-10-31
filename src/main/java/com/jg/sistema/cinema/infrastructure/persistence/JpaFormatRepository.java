package com.jg.sistema.cinema.infrastructure.persistence;

import com.jg.sistema.cinema.application.port.out.FormatRepositoryPort;
import com.jg.sistema.cinema.domain.model.Format;
import com.jg.sistema.cinema.infrastructure.persistence.entities.FormatEntity;
import com.jg.sistema.cinema.infrastructure.persistence.mapper.FormatMapper;
import com.jg.sistema.cinema.infrastructure.persistence.repositories.PostgreSqlFormatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaFormatRepository implements FormatRepositoryPort {

    private final PostgreSqlFormatRepository postgreSqlFormatRepository;

    @Override
    public List<Format> findAll() {
        return postgreSqlFormatRepository.findAll().stream()
                .map(FormatMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Format> findById(Long id) {
        return postgreSqlFormatRepository.findById(id)
                .map(FormatMapper::toDomain);
    }

    @Override
    public Optional<Format> findByName(String name) {
        return postgreSqlFormatRepository.findByNameIgnoreCase(name)
                .map(FormatMapper::toDomain);
    }

    @Override
    public Set<Format> findByIds(Set<Long> ids) {
        return FormatMapper.toDomainSet(postgreSqlFormatRepository.findByIdIn(ids));
    }

    @Override
    public boolean existsByName(String name) {
        return postgreSqlFormatRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return postgreSqlFormatRepository.existsByNameIgnoreCaseAndIdNot(name, id);
    }

    @Override
    public Format save(Format format) {
        FormatEntity entity = FormatMapper.toEntity(format);
        FormatEntity saved = postgreSqlFormatRepository.save(entity);
        return FormatMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        postgreSqlFormatRepository.deleteById(id);
    }
}
