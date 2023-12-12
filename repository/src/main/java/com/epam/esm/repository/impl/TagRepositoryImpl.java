package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.exception.TagOperationException;
import com.epam.esm.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String INSERT_QUERY = "INSERT INTO tags (name) VALUES (?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM tags WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM tags WHERE name = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM tags";
    private static final String DELETE_QUERY = "DELETE FROM tags WHERE id = ?";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag save(Tag tag) throws TagOperationException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                ps.setString(1, tag.getName());
                return ps;
            }, keyHolder);

            tag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

            return tag;
        } catch (DataAccessException e) {
            log.error("Error has occurred while saving tag", e);
            throw new TagOperationException("Error has occurred while saving tag", e);
        }
    }

    @Override
    public Optional<Tag> findById(Long id) throws TagNotFoundException {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new BeanPropertyRowMapper<>(Tag.class), id);
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            log.error("Tag not found with id: {}", id, e);
            throw new TagNotFoundException("Tag not found with id: " + id, e);
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            Tag tag = jdbcTemplate.queryForObject(FIND_BY_NAME_QUERY, new BeanPropertyRowMapper<>(Tag.class), name);
            return Optional.ofNullable(tag);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> findAll() throws TagNotFoundException {
        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, new BeanPropertyRowMapper<>(Tag.class));
        } catch (RuntimeException e) {
            log.error("Error has occurred while getting all tags");
            throw new TagNotFoundException("No tags found");
        }
    }

    @Override
    public void delete(Long id) throws TagOperationException {
        try {
            jdbcTemplate.update(DELETE_QUERY, id);
        } catch (Exception e) {
            log.error("Error has occurred while deleting tag with id = {}", id, e);
            throw new TagOperationException("Failed to delete tag: " + e.getMessage());
        }
    }
}
