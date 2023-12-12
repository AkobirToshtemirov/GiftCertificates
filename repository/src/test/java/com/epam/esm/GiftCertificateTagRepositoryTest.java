package com.epam.esm;

import com.epam.esm.config.DatabaseConfig;
import com.epam.esm.entity.GiftCertificateTag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.GiftCertificateTagRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.esm.constants.GiftCertificateTagRepositoryTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GiftCertificateTagRepositoryTest {

    @Autowired
    private GiftCertificateTagRepository giftCertificateTagRepository;

    @Test
    void findAssociationsByGiftCertificateId_correctAssociationList_whenFindAssociationsByGiftCertificateId()
            throws NotFoundException {
        List<GiftCertificateTag> associations = giftCertificateTagRepository.findAssociationsByGiftCertificateId(GIFT_CERTIFICATE_1_ID);
        Assertions.assertEquals(GIFT_CERTIFICATE_TAG_LIST, associations, "When finding associations, association list should be equal to database values");
    }

    @Test
    void findAssociationsByGiftCertificateId_exceptionThrown_whenInvalidGiftCertificateId() throws NotFoundException {
        assertThrows(NotFoundException.class, () -> giftCertificateTagRepository.findById(INVALID_GIFT_CERTIFICATE_ID), "Tag should be not found and  DataNotFoundException should be thrown");
    }
}