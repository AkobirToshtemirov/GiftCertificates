//package com.epam.esm;
//
//import com.epam.esm.entity.Tag;
//import com.epam.esm.exception.NotFoundException;
//import com.epam.esm.repository.TagRepository;
//import com.epam.esm.service.impl.TagServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TagServiceTest {
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @InjectMocks
//    private TagServiceImpl tagService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateTag() {
//        Tag tag = new Tag();
//        tag.setName("Test Tag");
//
//        when(tagRepository.save(any(Tag.class))).thenReturn(tag);
//
//        Tag createdTag = tagService.create(tag);
//
//        assertNotNull(createdTag);
//        assertEquals("Test Tag", createdTag.getName());
//        verify(tagRepository, times(1)).save(tag);
//    }
//
//    @Test
//    public void testFindAllTags() {
//        List<Tag> tagList = new ArrayList<>();
//        tagList.add(new Tag());
//        tagList.add(new Tag());
//
//        when(tagRepository.findAll()).thenReturn(tagList);
//
//        List<Tag> retrievedTags = tagService.findAll();
//
//        assertNotNull(retrievedTags);
//        assertEquals(2, retrievedTags.size());
//        verify(tagRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void testFindTagById() {
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("Test Tag");
//
//        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
//
//        Optional<Tag> retrievedTag = tagService.findById(1L);
//
//        assertTrue(retrievedTag.isPresent());
//        assertEquals("Test Tag", retrievedTag.get().getName());
//        verify(tagRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    public void testDeleteTag() {
//        doNothing().when(tagRepository).delete(1L);
//
//        assertDoesNotThrow(() -> tagService.delete(1L));
//        verify(tagRepository, times(1)).delete(1L);
//    }
//
//    @Test
//    public void testDeleteTag_NotFound() {
//        doThrow(NotFoundException.class).when(tagRepository).delete(1L);
//
//        assertThrows(NotFoundException.class, () -> tagService.delete(1L));
//        verify(tagRepository, times(1)).delete(1L);
//    }
//
//    @Test
//    public void testFindTagByName() {
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("Test Tag");
//
//        when(tagRepository.findByName("Test Tag")).thenReturn(Optional.of(tag));
//
//        Optional<Tag> retrievedTag = tagService.findTagByName("Test Tag");
//
//        assertTrue(retrievedTag.isPresent());
//        assertEquals(1L, retrievedTag.get().getId());
//        assertEquals("Test Tag", retrievedTag.get().getName());
//        verify(tagRepository, times(1)).findByName("Test Tag");
//    }
//}
//
