//package com.epam.esm;
//
//import com.epam.esm.entity.Tag;
//import com.epam.esm.entity.User;
//import com.epam.esm.repository.TagRepository;
//import com.epam.esm.repository.UserRepository;
//import com.epam.esm.service.impl.UserServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private TagRepository tagRepository;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testFindAllUsers() {
//        List<User> users = new ArrayList<>();
//        users.add(new User());
//        users.add(new User());
//
//        when(userRepository.findAll()).thenReturn(users);
//
//        List<User> foundUsers = userService.findAll();
//
//        assertNotNull(foundUsers);
//        assertEquals(2, foundUsers.size());
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testFindUserById() {
//        Long id = 1L;
//        User user = new User();
//        user.setId(id);
//        user.setUsername("testUser");
//
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//
//        Optional<User> foundUser = userService.findById(id);
//
//        assertTrue(foundUser.isPresent());
//        assertEquals("testUser", foundUser.get().getUsername());
//        verify(userRepository, times(1)).findById(id);
//    }
//
//    @Test
//    void testFindMostUsedTagOfUserWithHighestOrderCost() {
//        Long userId = 1L;
//        Tag tag = new Tag();
//        tag.setId(1L);
//        tag.setName("TestTag");
//
//        when(tagRepository.findMostUsedTagOfUserWithHighestOrderCost(userId)).thenReturn(tag);
//
//        Tag foundTag = userService.findMostUsedTagOfUserWithHighestOrderCost(userId);
//
//        assertNotNull(foundTag);
//        assertEquals("TestTag", foundTag.getName());
//        verify(tagRepository, times(1)).findMostUsedTagOfUserWithHighestOrderCost(userId);
//    }
//
//}
