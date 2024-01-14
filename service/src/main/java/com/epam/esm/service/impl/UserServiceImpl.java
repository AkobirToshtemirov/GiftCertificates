package com.epam.esm.service.impl;

import com.epam.esm.config.security.JwtTokenUtil;
import com.epam.esm.dto.TokenRequest;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.AuthException;
import com.epam.esm.exception.NotFoundException;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.impl.RoleRepositoryImpl;
import com.epam.esm.repository.impl.TagRepositoryImpl;
import com.epam.esm.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TagRepositoryImpl tagRepository;
    private final RoleRepositoryImpl roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, TagRepositoryImpl tagRepository, RoleRepositoryImpl roleRepository, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
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
    public String register(@NonNull UserRegisterDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new AuthException("Email is already taken");
        }

        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new AuthException("Username is already taken");
        }

        Role role = roleRepository.findByCode("USER")
                .orElseThrow(() -> new NotFoundException("Role not found with code: USER "));

        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRoles(List.of(role));

        userRepository.save(user);

        return "Success";
    }

    @Override
    public String generateToken(@NonNull TokenRequest request) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(request.username());
    }

    @Override
    public List<Tag> findMostUsedTagOfUserWithHighestOrderCost(Long userId) {
        return tagRepository.findMostUsedTagOfUserWithHighestOrderCost(userId);
    }
}
