package com.epam.esm.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GiftCertificateTag {
    private Long id;
    private Long giftCertificateId;
    private Long tagId;

    public GiftCertificateTag(Long giftCertificateId, Long tagId) {
        this.giftCertificateId = giftCertificateId;
        this.tagId = tagId;
    }
}
