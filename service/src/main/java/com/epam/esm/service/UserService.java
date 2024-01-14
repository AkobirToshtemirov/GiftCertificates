package com.epam.esm.service;

import com.epam.esm.dto.TokenRequest;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for CRUD operations on User entities.
 */
public interface UserService {

    /**
     * Retrieves the most used Tags of a User with the highest order cost.
     *
     * @param userId the ID of the user
     * @return a list of most used Tags of the user with the highest order cost
     */
    List<Tag> findMostUsedTagOfUserWithHighestOrderCost(Long userId);

    /**
     * Retrieves a paginated list of Users.
     *
     * @param page the page number (0-based)
     * @param size the number of entities per page
     * @return a list of Users for the specified page
     */
    List<User> findAllWithPage(int page, int size);

    /**
     * Retrieves a User by its ID.
     *
     * @param id the ID of the User to be retrieved
     * @return an {@code Optional} containing the User, or empty if not found
     */
    Optional<User> findById(Long id);

    /**
     * Registers a new user based on the provided registration information.
     *
     * @param dto the UserRegisterDTO containing registration information
     * @return a confirmation message for successful registration
     */
    String register(UserRegisterDTO dto);

    /**
     * Generates an authentication token based on the provided token request.
     *
     * @param request the TokenRequest containing authentication information
     * @return the generated authentication token
     */
    String generateToken(TokenRequest request);
}