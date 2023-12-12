package com.epam.esm;

import com.epam.esm.config.DatabaseConfig;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.repository.GiftCertificateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.constants.GiftCertificatesRepositoryTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GiftCertificateRepositoryTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;


    @Test
    void findById_ThrowsExceptionForAbsentCertificate() {
        assertThrows(NotFoundException.class, () ->
                        giftCertificateRepository.findById(ABSENT_ID),
                "Exception should be thrown for absent certificate");
    }

    @Test
    void delete_DeletesGiftCertificate() throws NotFoundException {
        giftCertificateRepository.delete(GIFT_CERTIFICATE_1.getId());

        assertThrows(NotFoundException.class, () ->
                        giftCertificateRepository.findById(GIFT_CERTIFICATE_1.getId()),
                "Exception should be thrown for deleted certificate");
    }
}
