package com.epam.esm;

import com.epam.esm.entity.Tag;
import com.epam.esm.exception.TagNotFoundException;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.epam.esm.constants.TagTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepositoryImpl mockTagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findTagById_ShouldReturnExpectedTag_WhenTagExists() throws TagNotFoundException {
        when(mockTagRepository.findById(TAG_1.getId())).thenReturn(Optional.of(TAG_1));

        Tag actualTag = tagService.findById(TAG_1.getId());

        assertEquals("Actual tag should be equal to expected", TAG_1, actualTag);
    }

    @Test
    void findTagById_ShouldThrowTagNotFoundException_WhenTagDoesNotExist() {
        when(mockTagRepository.findById(TAG_1.getId())).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class,
                () -> tagService.findById(TAG_1.getId()),
                "Tag should not be found, and TagNotFoundException should be thrown");
    }

    @Test
    void findAllTags_ShouldReturnExpectedTagList() throws TagNotFoundException {
        when(mockTagRepository.findAll()).thenReturn(TAG_LIST);

        Iterable<Tag> actualTags = tagService.findAll();

        assertEquals("Actual tag list should be equal to expected", TAG_LIST, actualTags);
    }

    @Test
    void findTagByName_ShouldReturnExpectedTag_WhenTagExists() throws TagNotFoundException {
        when(mockTagRepository.findByName(TAG_1.getName())).thenReturn(Optional.of(TAG_1));

        Tag actualTag = tagService.findTagByName(TAG_1.getName()).orElse(new Tag());

        assertEquals("Actual tag should be equal to expected", TAG_1, actualTag);
    }

    @Test
    void createTag_ShouldReturnExpectedTag_WhenSavingTag() {
        Tag savingTag = new Tag();
        savingTag.setName(TAG_2.getName());
        when(mockTagRepository.save(savingTag)).thenReturn(TAG_2);

        Tag actualTag = tagService.create(savingTag);

        assertEquals("Actual tag should be equal to expected", TAG_2, actualTag);
    }
}