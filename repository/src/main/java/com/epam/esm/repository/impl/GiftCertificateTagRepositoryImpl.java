package com.epam.esm.repository.impl;

import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;
import com.epam.esm.repository.GiftCertificateTagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class GiftCertificateTagRepositoryImpl implements GiftCertificateTagRepository {
    private static final String INSERT_QUERY = "INSERT INTO gift_certificate_tags (gift_certificate_id, tag_id) VALUES (?, ?)";
    private static final String FIND_BY_CERTIFICATE_ID_QUERY = "SELECT * FROM gift_certificate_tags WHERE gift_certificate_id = ?";


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GiftCertificateTagRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificateTag save(GiftCertificateTag giftCertificateTag) throws OperationException {
        try {
            jdbcTemplate.update(INSERT_QUERY, giftCertificateTag.getGiftCertificateId(), giftCertificateTag.getTagId());
            return giftCertificateTag;
        } catch (DataAccessException e) {
            log.error("Error has occurred while saving gift certificate tag association", e);
            throw new OperationException("Error has occurred while saving gift certificate tag association", e);
        }
    }

    @Override
    public List<GiftCertificateTag> findAssociationsByGiftCertificateId(Long giftCertificateId) throws NotFoundException {
        try {
            return jdbcTemplate.query(FIND_BY_CERTIFICATE_ID_QUERY, new BeanPropertyRowMapper<>(GiftCertificateTag.class), giftCertificateId);
        } catch (DataAccessException e) {
            log.error("Error has occurred while getting associations for gift certificate with ID: {}", giftCertificateId, e);
            throw new NotFoundException("Error has occurred while getting associations for gift certificate", e);
        }
    }
}
