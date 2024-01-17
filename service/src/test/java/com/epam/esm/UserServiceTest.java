package com.epam.esm;

import com.epam.esm.config.security.JwtTokenUtil;
import com.epam.esm.dto.TokenRequest;
import com.epam.esm.dto.TokenResponse;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.entity.User;
import com.epam.esm.exception.AuthException;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("testUser");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(id);

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void testFindAllWithPage() {
        when(userRepository.findAllWithPage(anyInt(), anyInt())).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.findAllWithPage(1, 10);
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAllWithPage(anyInt(), anyInt());
    }

    @Test
    void testGenerateToken() {
        TokenRequest tokenRequest = new TokenRequest("testUser", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtTokenUtil.generateToken(anyString())).thenReturn(new TokenResponse("accessToken"));
        TokenResponse tokenResponse = userService.generateToken(tokenRequest);

        assertEquals("accessToken", tokenResponse.accessToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenUtil, times(1)).generateToken(anyString());
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("existing@example.com", "testUser", "password");

        when(userRepository.findByEmail(userRegisterDTO.email())).thenReturn(Optional.of(new User()));

        assertThrows(AuthException.class, () -> userService.register(userRegisterDTO));

        verify(userRepository, times(1)).findByEmail(userRegisterDTO.email());
        verify(userRepository, never()).findByUsername(anyString());
        verify(roleRepository, never()).findByCode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegisterUserWithExistingUsername() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@example.com", "existingUser", "password");

        when(userRepository.findByEmail(userRegisterDTO.email())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(userRegisterDTO.username())).thenReturn(Optional.of(new User()));

        assertThrows(AuthException.class, () -> userService.register(userRegisterDTO));

        verify(userRepository, times(1)).findByEmail(userRegisterDTO.email());
        verify(userRepository, times(1)).findByUsername(userRegisterDTO.username());
        verify(roleRepository, never()).findByCode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }
}
