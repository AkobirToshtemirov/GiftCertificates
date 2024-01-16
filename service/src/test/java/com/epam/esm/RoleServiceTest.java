package com.epam.esm;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.impl.RoleRepositoryImpl;
import com.epam.esm.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepositoryImpl roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRole() {
        Role roleToSave = Role.builder().code("USER").build();
        Role savedRole = Role.builder().id(1L).code("USER").build();

        when(roleRepository.save(roleToSave)).thenReturn(savedRole);

        Role result = roleService.save(roleToSave);

        assertEquals(savedRole, result);
        verify(roleRepository, times(1)).save(roleToSave);
    }
}
