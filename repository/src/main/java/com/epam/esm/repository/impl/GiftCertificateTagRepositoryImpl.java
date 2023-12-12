package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.GiftCertificateOperationException;
import com.epam.esm.repository.GiftCertificateTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Repository
public class GiftCertificateTagRepositoryImpl implements GiftCertificateTagRepository {
    private static final String INSERT_QUERY = "INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String FIND_BY_CERTIFICATE_ID_QUERY = "SELECT * FROM gift_certificate_tags WHERE gift_certificate_id = ?";
    private static final String FIND_ASSOCIATION_QUERY = "SELECT * FROM gift_certificate_tags WHERE gift_certificate_id = ? AND tag_id =? ";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificateTag save(GiftCertificateTag giftCertificateTag) throws GiftCertificateOperationException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
                ps.setLong(1, giftCertificateTag.getGiftCertificateId());
                ps.setLong(2, giftCertificateTag.getTagId());

                return ps;
            }, keyHolder);

            giftCertificateTag.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

            return giftCertificateTag;
        } catch (DataAccessException e) {
            log.error("Error occurred while saving gift certificate tag association", e);
            throw new GiftCertificateOperationException("Error occurred while saving gift certificate tag association", e);
        }
    }

    @Override
    public boolean hasAssociation(Long giftCertificateId, Long tagId) {
        GiftCertificateTag giftCertificateTag = jdbcTemplate.queryForObject(FIND_ASSOCIATION_QUERY, new BeanPropertyRowMapper<>(GiftCertificateTag.class), giftCertificateId, tagId);
        return Objects.isNull(giftCertificateTag);
    }

    @Override
    public List<GiftCertificateTag> findAssociationsByGiftCertificateId(Long giftCertificateId) throws GiftCertificateNotFoundException {
        try {
            return jdbcTemplate.query(FIND_BY_CERTIFICATE_ID_QUERY, new BeanPropertyRowMapper<>(GiftCertificateTag.class), giftCertificateId);
        } catch (DataAccessException e) {
            log.error("Error has occurred while getting associations for gift certificate with id: {}", giftCertificateId, e);
            throw new GiftCertificateNotFoundException("Error has occurred while getting associations for gift certificate", e);
        }
    }
}
