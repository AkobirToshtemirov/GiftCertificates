//package com.epam.esm;
//
//import com.epam.esm.entity.GiftCertificate;
//import com.epam.esm.repository.GiftCertificateRepository;
//import com.epam.esm.service.impl.GiftCertificateServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class GiftCertificateServiceTest {
//
//    @Mock
//    private GiftCertificateRepository giftCertificateRepository;
//
//    @InjectMocks
//    private GiftCertificateServiceImpl giftCertificateService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateGiftCertificate() {
//        GiftCertificate giftCertificate = new GiftCertificate();
//        giftCertificate.setName("Test Certificate");
//        giftCertificate.setPrice(BigDecimal.TEN);
//
//        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(giftCertificate);
//
//        GiftCertificate createdCertificate = giftCertificateService.create(giftCertificate);
//
//        assertNotNull(createdCertificate);
//        assertEquals("Test Certificate", createdCertificate.getName());
//        assertEquals(BigDecimal.TEN, createdCertificate.getPrice());
//        assertNotNull(createdCertificate.getCreatedDate());
//        verify(giftCertificateRepository, times(1)).save(any(GiftCertificate.class));
//    }
//
//    @Test
//    void testFindAllGiftCertificates() {
//        List<GiftCertificate> certificates = new ArrayList<>();
//        certificates.add(new GiftCertificate());
//        certificates.add(new GiftCertificate());
//
//        when(giftCertificateRepository.findAll()).thenReturn(certificates);
//
//        List<GiftCertificate> foundCertificates = giftCertificateService.findAll();
//
//        assertNotNull(foundCertificates);
//        assertEquals(2, foundCertificates.size());
//        verify(giftCertificateRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testFindCertificateById() {
//        Long id = 1L;
//        GiftCertificate certificate = new GiftCertificate();
//        certificate.setId(id);
//        certificate.setName("Test Certificate");
//
//        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(certificate));
//
//        Optional<GiftCertificate> foundCertificate = giftCertificateService.findById(id);
//
//        assertTrue(foundCertificate.isPresent());
//        assertEquals("Test Certificate", foundCertificate.get().getName());
//        verify(giftCertificateRepository, times(1)).findById(id);
//    }
//
//    @Test
//    void testDeleteGiftCertificate() {
//        Long id = 1L;
//
//        doNothing().when(giftCertificateRepository).delete(id);
//
//        assertDoesNotThrow(() -> giftCertificateService.delete(id));
//        verify(giftCertificateRepository, times(1)).delete(id);
//    }
//
//    @Test
//    void testUpdateGiftCertificate() {
//        Long id = 1L;
//        GiftCertificate updatedCertificate = new GiftCertificate();
//        updatedCertificate.setId(id);
//        updatedCertificate.setName("Updated Certificate");
//        updatedCertificate.setPrice(BigDecimal.valueOf(15));
//
//        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(new GiftCertificate()));
//        when(giftCertificateRepository.update(any(GiftCertificate.class))).thenReturn(updatedCertificate);
//
//        GiftCertificate result = giftCertificateService.update(id, updatedCertificate);
//
//        assertNotNull(result);
//        assertEquals(id, result.getId());
//        assertEquals("Updated Certificate", result.getName());
//        assertEquals(BigDecimal.valueOf(15), result.getPrice());
//        assertNotNull(result.getLastUpdatedDate());
//        verify(giftCertificateRepository, times(1)).findById(id);
//        verify(giftCertificateRepository, times(1)).update(any(GiftCertificate.class));
//    }
//
//    @Test
//    void testFindCertificatesByCriteria() {
//        List<String> tagNames = new ArrayList<>();
//        tagNames.add("tag1");
//        tagNames.add("tag2");
//
//        when(giftCertificateRepository.findCertificatesByCriteria(tagNames, "search", "sortBy", true)).thenReturn(new ArrayList<>());
//
//        List<GiftCertificate> result = giftCertificateService.findCertificatesByCriteria(tagNames, "search", "sortBy", true);
//
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//        verify(giftCertificateRepository, times(1)).findCertificatesByCriteria(tagNames, "search", "sortBy", true);
//    }
//
//    @Test
//    void testUpdateGiftCertificateDuration() {
//        Long id = 1L;
//        Double newDuration = 10.0;
//
//        GiftCertificate existingCertificate = new GiftCertificate();
//        existingCertificate.setId(id);
//        existingCertificate.setDuration(5.0);
//
//        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(existingCertificate));
//        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(existingCertificate);
//
//        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificateDuration(id, newDuration);
//
//        assertNotNull(updatedCertificate);
//        assertEquals(id, updatedCertificate.getId());
//        assertEquals(newDuration, updatedCertificate.getDuration());
//        assertNotNull(updatedCertificate.getLastUpdatedDate());
//        verify(giftCertificateRepository, times(1)).findById(id);
//        verify(giftCertificateRepository, times(1)).save(any(GiftCertificate.class));
//    }
//
//    @Test
//    void testUpdateGiftCertificatePrice() {
//        Long id = 1L;
//        BigDecimal newPrice = BigDecimal.valueOf(20);
//
//        GiftCertificate existingCertificate = new GiftCertificate();
//        existingCertificate.setId(id);
//        existingCertificate.setPrice(BigDecimal.TEN);
//
//        when(giftCertificateRepository.findById(id)).thenReturn(Optional.of(existingCertificate));
//        when(giftCertificateRepository.save(any(GiftCertificate.class))).thenReturn(existingCertificate);
//
//        GiftCertificate updatedCertificate = giftCertificateService.updateGiftCertificatePrice(id, newPrice);
//
//        assertNotNull(updatedCertificate);
//        assertEquals(id, updatedCertificate.getId());
//        assertEquals(newPrice, updatedCertificate.getPrice());
//        assertNotNull(updatedCertificate.getLastUpdatedDate());
//        verify(giftCertificateRepository, times(1)).findById(id);
//        verify(giftCertificateRepository, times(1)).save(any(GiftCertificate.class));
//    }
//
//}
