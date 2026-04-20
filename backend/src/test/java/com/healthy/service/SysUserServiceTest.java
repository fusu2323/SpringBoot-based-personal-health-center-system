package com.healthy.service;

import com.healthy.common.ServiceException;
import com.healthy.entity.SysUser;
import com.healthy.mapper.SysUserMapper;
import com.healthy.service.impl.SysUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserServiceTest {

    @Mock
    private SysUserMapper sysUserMapper;

    @InjectMocks
    private SysUserServiceImpl sysUserService;

    // --- testLogin ---

    @Test
    void testLogin_Success() {
        SysUser mockUser = new SysUser();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        when(sysUserMapper.selectByUsername("testuser")).thenReturn(mockUser);

        var result = sysUserService.login("testuser", "password123");

        assertNotNull(result);
        assertEquals(mockUser, result.get("user"));
        assertEquals("user-token-1", result.get("token"));
        verify(sysUserMapper, times(1)).selectByUsername("testuser");
    }

    @Test
    void testLogin_WrongPassword() {
        SysUser mockUser = new SysUser();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        when(sysUserMapper.selectByUsername("testuser")).thenReturn(mockUser);

        assertThrows(ServiceException.class, () -> {
            sysUserService.login("testuser", "wrongpassword");
        });
    }

    @Test
    void testLogin_UserNotFound() {
        when(sysUserMapper.selectByUsername("nonexistent")).thenReturn(null);

        assertThrows(ServiceException.class, () -> {
            sysUserService.login("nonexistent", "password");
        });
    }

    // --- testRegister ---

    @Test
    void testRegister_Success() {
        SysUser newUser = new SysUser();
        newUser.setUsername("newuser");
        newUser.setPassword("password123");
        when(sysUserMapper.selectByUsername("newuser")).thenReturn(null);
        doNothing().when(sysUserMapper).insert(any(SysUser.class));

        assertDoesNotThrow(() -> sysUserService.register(newUser));

        verify(sysUserMapper, times(1)).insert(any(SysUser.class));
    }

    @Test
    void testRegister_DuplicateUsername() {
        SysUser existingUser = new SysUser();
        existingUser.setUsername("existing");
        when(sysUserMapper.selectByUsername("existing")).thenReturn(existingUser);

        SysUser newUser = new SysUser();
        newUser.setUsername("existing");
        newUser.setPassword("password123");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            sysUserService.register(newUser);
        });
        assertEquals("409", exception.getCode());
        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testRegister_BlankUsername() {
        SysUser newUser = new SysUser();
        newUser.setUsername("");
        newUser.setPassword("password123");

        assertThrows(ServiceException.class, () -> {
            sysUserService.register(newUser);
        });
    }

    // --- testAddUser (delegates to register) ---

    @Test
    void testAddUser_Success() {
        SysUser newUser = new SysUser();
        newUser.setUsername("adduser");
        newUser.setPassword("password123");
        when(sysUserMapper.selectByUsername("adduser")).thenReturn(null);
        doNothing().when(sysUserMapper).insert(any(SysUser.class));

        assertDoesNotThrow(() -> sysUserService.addUser(newUser));
        verify(sysUserMapper, times(1)).insert(any(SysUser.class));
    }

    // --- testUpdateUser ---

    @Test
    void testUpdateUser_Success() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("updateduser");
        user.setNickname("Updated Nickname");
        doNothing().when(sysUserMapper).update(any(SysUser.class));

        assertDoesNotThrow(() -> sysUserService.updateUser(user));
        verify(sysUserMapper, times(1)).update(any(SysUser.class));
    }

    // --- testDeleteUser ---

    @Test
    void testDeleteUser_Success() {
        doNothing().when(sysUserMapper).deleteById(1L);

        assertDoesNotThrow(() -> sysUserService.deleteUser(1L));
        verify(sysUserMapper, times(1)).deleteById(1L);
    }

    // --- testGetUserById ---

    @Test
    void testGetUserById_Success() {
        SysUser mockUser = new SysUser();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        when(sysUserMapper.selectById(1L)).thenReturn(mockUser);

        SysUser result = sysUserService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(sysUserMapper.selectById(999L)).thenReturn(null);

        SysUser result = sysUserService.getUserById(999L);

        assertNull(result);
    }

    // --- testGetAllUsers ---

    @Test
    void testGetAllUsers_Success() {
        SysUser user1 = new SysUser();
        user1.setId(1L);
        user1.setUsername("user1");
        SysUser user2 = new SysUser();
        user2.setId(2L);
        user2.setUsername("user2");
        when(sysUserMapper.selectAll()).thenReturn(List.of(user1, user2));

        List<SysUser> result = sysUserService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(sysUserMapper, times(1)).selectAll();
    }
}
