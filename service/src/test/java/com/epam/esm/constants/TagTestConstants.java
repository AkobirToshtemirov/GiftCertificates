package com.epam.esm.constants;

import com.epam.esm.entity.Tag;

import java.util.Arrays;
import java.util.List;

public class TagTestConstants {
    public static final Tag TAG_1 = new Tag(1L, "Tag 1");
    public static final Tag TAG_2 = new Tag(2L, "Tag 2");
    public static final List<Tag> TAG_LIST = Arrays.asList(TAG_1, TAG_2);
}