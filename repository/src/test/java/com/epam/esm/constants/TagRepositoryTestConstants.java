package com.epam.esm.constants;

import com.epam.esm.entity.Tag;

import java.util.Arrays;
import java.util.List;

public class TagRepositoryTestConstants {
    public static final Tag TAG_1 = new Tag(1L, "Tag 1");
    public static final Tag TAG_2 = new Tag(2L, "Tag 2");
    public static final Tag TAG_3 = new Tag(3L, "Tag 3");
    public static final Tag TAG_4 = new Tag(4L, "Tag 4");
    public static final Tag TAG_5 = new Tag(5L, "Tag 5");
    public static final List<Tag> TAG_LIST = Arrays.asList(TAG_1, TAG_2, TAG_3, TAG_4, TAG_5);
    public static final Tag NEW_TAG = new Tag(999L, "New Tag");
    public static final Long NEW_ID = 6L;
}