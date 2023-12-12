package com.epam.esm.constants;

import com.epam.esm.entity.GiftCertificate;

import java.time.LocalDateTime;

public class GiftCertificatesRepositoryTestConstants {
    public static final GiftCertificate GIFT_CERTIFICATE_1 = new GiftCertificate(
            1L,
            "Gift Certificate 1",
            "Description 1",
            180.75,
            6.0,
            LocalDateTime.of(2020, 6, 1, 12, 0),
            LocalDateTime.of(2020, 6, 7, 12, 30),
            null
    );


    public static final Long ABSENT_ID = 80L;
}
