package com.epam.esm;

import com.epam.esm.config.DatabaseConfig;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.OperationException;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.epam.esm.constants.TagRepositoryTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DatabaseConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void getTags_correctTagList_whenGetTags() throws NotFoundException {
        assertEquals(TAG_LIST, tagRepository.findAll(), "When getting tags, tag list should be equal to database values");
    }

    @Test
    void getTag_correctTag_whenGetTag() throws NotFoundException, OperationException {
        assertEquals(TAG_1, tagRepository.findById(TAG_1.getId()).orElse(new Tag()), "When getting tag, tag should be equal to database value");
    }

    @Test
    void getTagByName_correctTag_whenGetTagByName() throws NotFoundException {
        assertEquals(TAG_2, tagRepository.findByName(TAG_2.getName()).orElse(new Tag()), "When getting tag, tag should be equal to database value");
    }

    @Test
    void saveTag_savedTag_whenTagWasSaved() throws NotFoundException, OperationException {
        Tag savingTag = NEW_TAG;
        tagRepository.save(savingTag);
        savingTag.setId(NEW_ID);
        assertEquals(savingTag, tagRepository.findById(NEW_ID).orElse(new Tag()), "Tag should be saved with correct ID");
    }

    @Test
    void deleteTag_Exception_whenTryToGetDeletedTag() throws NotFoundException {
        tagRepository.delete(TAG_1.getId());
        assertThrows(NotFoundException.class, () -> tagRepository.findById(TAG_1.getId()), "Tag should be not found and  DataNotFoundException should be thrown");
    }
}
