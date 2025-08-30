package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.LogingDAO;
import model.User;
import service.LoginService;

public class LoginServiceTest {
    
    private LoginService loginService;
    
    @Mock
    private LogingDAO mockLogingDAO;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        loginService = LoginService.getInstance();
        
        
        java.lang.reflect.Field daoField = LoginService.class.getDeclaredField("logingDAO");
        daoField.setAccessible(true);
        daoField.set(loginService, mockLogingDAO);
    }
    
    @Test
    public void testAuthenticate_ValidCredentials() {
        // Arrange
        String username = "admin";
        String password = "admin123";
        
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setPassword(password);
        expectedUser.setRole("admin");
        expectedUser.setStatus("active");
        
        when(mockLogingDAO.authenticate(username, password)).thenReturn(expectedUser);
        
      
        User result = loginService.authenticate(username, password);
        
      
        assertNotNull(result, "Authentication should return a user");
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals("admin", result.getRole());
        assertEquals("active", result.getStatus());
        
        verify(mockLogingDAO, times(1)).authenticate(username, password);
    }
    
    @Test
    public void testAuthenticate_InvalidCredentials() {
        // Arrange
        String username = "invaliduser";
        String password = "wrongpassword";
        
        when(mockLogingDAO.authenticate(username, password)).thenReturn(null);
        
       
        User result = loginService.authenticate(username, password);
        
       
        assertNull(result, "Authentication with invalid credentials should return null");
        
        verify(mockLogingDAO, times(1)).authenticate(username, password);
    }
    
    @Test
    public void testAuthenticate_NullUsername() {
        // Arrange
        String username = null;
        String password = "password";
        
        when(mockLogingDAO.authenticate(username, password)).thenReturn(null);
        
       
        User result = loginService.authenticate(username, password);
        
      
        assertNull(result, "Authentication with null username should return null");
        
        verify(mockLogingDAO, times(1)).authenticate(username, password);
    }
    
    @Test
    public void testAuthenticate_NullPassword() {
        // Arrange
        String username = "user";
        String password = null;
        
        when(mockLogingDAO.authenticate(username, password)).thenReturn(null);
        
      
        User result = loginService.authenticate(username, password);
        
        
        assertNull(result, "Authentication with null password should return null");
        
        verify(mockLogingDAO, times(1)).authenticate(username, password);
    }
    
    @Test
    public void testAuthenticate_EmptyCredentials() {
        // Arrange
        String username = "";
        String password = "";
        
        when(mockLogingDAO.authenticate(username, password)).thenReturn(null);
        
      
        User result = loginService.authenticate(username, password);
        
       
        assertNull(result, "Authentication with empty credentials should return null");
        
        verify(mockLogingDAO, times(1)).authenticate(username, password);
    }
    
    @Test
    public void testGetActiveUserByUsername_ActiveUser() {
       
        String username = "activeuser";
        
        User expectedUser = new User();
        expectedUser.setUsername(username);
        expectedUser.setPassword("password123");
        expectedUser.setRole("staff");
        expectedUser.setStatus("active");
        
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(expectedUser);
        
       
        User result = loginService.getActiveUserByUsername(username);
        
        
        assertNotNull(result, "Should return active user");
        assertEquals(username, result.getUsername());
        assertEquals("active", result.getStatus());
        assertEquals("staff", result.getRole());
        
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
    
    @Test
    public void testGetActiveUserByUsername_InactiveUser() {
        
        String username = "inactiveuser";
        
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(null);
        
       
        User result = loginService.getActiveUserByUsername(username);
        
        
        assertNull(result, "Should return null for inactive user");
        
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
    
    @Test
    public void testGetActiveUserByUsername_NonExistentUser() {
        // Arrange
        String username = "nonexistentuser";
        
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(null);
        
       
        User result = loginService.getActiveUserByUsername(username);
        
        
        assertNull(result, "Should return null for non-existent user");
        
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
    
    @Test
    public void testGetActiveUserByUsername_NullUsername() {
        // Arrange
        String username = null;
        
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(null);
        
       
        User result = loginService.getActiveUserByUsername(username);
        
       
        assertNull(result, "Should return null for null username");
        
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
    
    @Test
    public void testGetActiveUserByUsername_EmptyUsername() {
      
        String username = "";
        
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(null);
        
      
        User result = loginService.getActiveUserByUsername(username);
        
       
        assertNull(result, "Should return null for empty username");
        
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
    
    @Test
    public void testAuthenticate_DifferentRoles() {
        
        
       
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("adminpass");
        adminUser.setRole("admin");
        adminUser.setStatus("active");
        
        when(mockLogingDAO.authenticate("admin", "adminpass")).thenReturn(adminUser);
        
        User adminResult = loginService.authenticate("admin", "adminpass");
        assertNotNull(adminResult);
        assertEquals("admin", adminResult.getRole());
        
    
        User staffUser = new User();
        staffUser.setUsername("staff");
        staffUser.setPassword("staffpass");
        staffUser.setRole("staff");
        staffUser.setStatus("active");
        
        when(mockLogingDAO.authenticate("staff", "staffpass")).thenReturn(staffUser);
        
        User staffResult = loginService.authenticate("staff", "staffpass");
        assertNotNull(staffResult);
        assertEquals("staff", staffResult.getRole());
        
        verify(mockLogingDAO, times(1)).authenticate("admin", "adminpass");
        verify(mockLogingDAO, times(1)).authenticate("staff", "staffpass");
    }
    
    @Test
    public void testAuthenticate_UserWithDifferentStatuses() {
       
        String username = "testuser";
        
        
        User inactiveUser = new User();
        inactiveUser.setUsername(username);
        inactiveUser.setPassword("password");
        inactiveUser.setRole("staff");
        inactiveUser.setStatus("inactive");
        
        when(mockLogingDAO.authenticate(username, "password")).thenReturn(inactiveUser);
        when(mockLogingDAO.getActiveUserByUsername(username)).thenReturn(null); // No active user
        
       
        User authResult = loginService.authenticate(username, "password");
        assertNotNull(authResult);
        assertEquals("inactive", authResult.getStatus());
        
      
        User activeResult = loginService.getActiveUserByUsername(username);
        assertNull(activeResult);
        
        verify(mockLogingDAO, times(1)).authenticate(username, "password");
        verify(mockLogingDAO, times(1)).getActiveUserByUsername(username);
    }
}