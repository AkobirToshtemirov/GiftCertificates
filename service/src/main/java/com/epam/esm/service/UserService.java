package com.epam.esm.service;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

public interface UserService extends BaseService<User> {
    Tag findMostUsedTagOfUserWithHighestOrderCost(Long userId);
}
