package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

public interface UserRepository extends BaseRepository<User> {
    Tag findMostUsedTagOfUserWithHighestOrderCost(Long userId);
}
