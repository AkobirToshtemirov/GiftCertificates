package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.GiftCertificateOperationException;
import com.epam.esm.repository.GiftCertificateRepository;
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
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String INSERT_QUERY = "INSERT INTO gift_certificates (name, description, price, duration, created_date, last_updated_date) VALUES (?, ? ,?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM gift_certificates WHERE id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM gift_certificates";
    private static final String UPDATE_QUERY = "UPDATE gift_certificates SET name = ?, description = ?, price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM gift_certificates WHERE id = ?";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) throws GiftCertificateOperationException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                ps.setString(1, giftCertificate.getName());
                ps.setString(2, giftCertificate.getDescription());
                ps.setDouble(3, giftCertificate.getPrice());
                ps.setDouble(4, giftCertificate.getDuration());
                ps.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreatedDate()));
                ps.setTimestamp(6, Timestamp.valueOf(giftCertificate.getLastUpdatedDate()));

                return ps;
            }, keyHolder);

            giftCertificate.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

            return giftCertificate;
        } catch (DataAccessException e) {
            log.error("Error occurred while saving gift certificate", e);
            throw new GiftCertificateOperationException("Error occurred while saving gift certificate", e);
        }
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) throws GiftCertificateNotFoundException {
        try {
            GiftCertificate giftCertificate = jdbcTemplate.queryForObject(FIND_BY_ID_QUERY, new BeanPropertyRowMapper<>(GiftCertificate.class), id);
            return Optional.ofNullable(giftCertificate);
        } catch (EmptyResultDataAccessException e) {
            log.error("Gift certificate not found with id: {}", id, e);
            throw new GiftCertificateNotFoundException("Gift certificate not found with id: " + id, e);
        }
    }

    @Override
    public List<GiftCertificate> findAll() throws GiftCertificateNotFoundException {
        try {
            return jdbcTemplate.query(FIND_ALL_QUERY, new BeanPropertyRowMapper<>(GiftCertificate.class));
        } catch (EmptyResultDataAccessException e) {
            log.error("No gift certificates found", e);
            throw new GiftCertificateNotFoundException("No gift certificates found", e);
        }
    }

    @Override
    public void update(GiftCertificate giftCertificate) throws GiftCertificateNotFoundException, GiftCertificateOperationException {
        try {
            int updatedRows = jdbcTemplate.update(UPDATE_QUERY, giftCertificate.getName(), giftCertificate.getDescription(), giftCertificate.getPrice(), giftCertificate.getDuration(), giftCertificate.getLastUpdatedDate(), giftCertificate.getId());
            if (updatedRows == 0) {
                log.error("Gift certificate not found with id: {}", giftCertificate.getId());
                throw new GiftCertificateNotFoundException("Gift certificate not found with id: " + giftCertificate.getId());
            }
        } catch (DataAccessException e) {
            log.error("Error occurred while updating gift certificate with id: {}", giftCertificate.getId(), e);
            throw new GiftCertificateOperationException("Error occurred while updating gift certificate", e);
        }
    }

    @Override
    public void delete(Long id) throws GiftCertificateOperationException {
        try {
            jdbcTemplate.update(DELETE_QUERY, id);
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting gift certificate with id: {}", id, e);
            throw new GiftCertificateOperationException("Error occurred while deleting gift certificate", e);
        }
    }

    @Override
    public List<GiftCertificate> findCertificatesByCriteria(String tagName, String search, String sortBy, boolean ascending) throws GiftCertificateOperationException {
        StringBuilder queryBuilder = new StringBuilder("SELECT gc.*, t.* FROM gift_certificates gc ");
        queryBuilder.append("LEFT JOIN gift_certificate_tags gct ON gc.id = gct.gift_certificate_id ");
        queryBuilder.append("LEFT JOIN tags t ON gct.tag_id = t.id ");
        queryBuilder.append("WHERE 1=1 ");

        if (tagName != null && !tagName.isEmpty()) queryBuilder.append("AND t.name = ? ");

        if (search != null && !search.isEmpty()) queryBuilder.append("AND (gc.name LIKE ? OR gc.description LIKE ?) ");

        if (sortBy != null && !sortBy.isEmpty()) {
            String sortField = sortBy.equalsIgnoreCase("name") ? "gc.name" : "gc.created_date";
            queryBuilder.append("ORDER BY ").append(sortField).append(ascending ? " ASC" : " DESC");
        }

        try {
            List<Object> queryParams = new ArrayList<>();

            if (tagName != null && !tagName.isEmpty()) {
                queryParams.add(tagName);
            }

            if (search != null && !search.isEmpty()) {
                queryParams.add("%" + search + "%");
                queryParams.add("%" + search + "%");
            }

            String finalQuery = queryBuilder.toString();
            return jdbcTemplate.query(finalQuery, queryParams.toArray(), (resultSet) -> {
                Map<Long, GiftCertificate> certificateMap = new LinkedHashMap<>();
                GiftCertificate giftCertificate;
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    giftCertificate = certificateMap.get(id);
                    if (giftCertificate == null) {
                        giftCertificate = new GiftCertificate();
                        giftCertificate.setId(id);
                        giftCertificate.setName(resultSet.getString("name"));
                        giftCertificate.setDescription(resultSet.getString("description"));
                        giftCertificate.setPrice(resultSet.getDouble("price"));
                        giftCertificate.setDuration(resultSet.getDouble("duration"));
                        giftCertificate.setCreatedDate(resultSet.getTimestamp("created_date").toLocalDateTime());
                        giftCertificate.setLastUpdatedDate(resultSet.getTimestamp("last_updated_date").toLocalDateTime());
                        certificateMap.put(id, giftCertificate);
                    }
                }
                return new ArrayList<>(certificateMap.values());
            });
        } catch (DataAccessException e) {
            log.error("Error has occurred while searching for certificates by criteria", e);
            throw new GiftCertificateOperationException("Error has occurred while searching for certificates by criteria", e);
        }
    }

}
