package com.epam.esm.constants;


import com.epam.esm.entity.GiftCertificateTag;

import java.util.Arrays;
import java.util.List;

public class GiftCertificateTagRepositoryTestConstants {
    public static final GiftCertificateTag GIFT_TAG_1 = new GiftCertificateTag(1L, 1L, 1L);
    public static final GiftCertificateTag GIFT_TAG_2 = new GiftCertificateTag(2L, 1L, 2L);
    public static final List<GiftCertificateTag> GIFT_CERTIFICATE_TAG_LIST = Arrays.asList(GIFT_TAG_1, GIFT_TAG_2);
    public static final Long INVALID_GIFT_CERTIFICATE_ID = 99L;
    public static final Long GIFT_CERTIFICATE_1_ID = 1L;
}