package com.epam.esm.service.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User entity) {
        entity.setOrderList(Collections.emptyList());
        return userRepository.save(entity);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithPage(int page, int size) {
        try {
            return userRepository.findAllWithPage(page, size);
        } catch (ValidationException e) {
            throw new ValidationException("Page number and page size must be positive");
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public Tag findMostUsedTagOfUserWithHighestOrderCost(Long userId) {
        return userRepository.findMostUsedTagOfUserWithHighestOrderCost(userId);
    }
}
