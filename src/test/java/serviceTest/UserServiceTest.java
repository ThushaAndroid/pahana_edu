package serviceTest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

import dao.UserDAO;
import model.User;
import service.UserService;
public class UserServiceTest {
    
    private UserService userService;
    @Mock
    private UserDAO mockUserDAO;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userService = new UserService();
        
     
        java.lang.reflect.Field daoField = UserService.class.getDeclaredField("userDAO");
        daoField.setAccessible(true);
        daoField.set(userService, mockUserDAO);
    }
    
    @Test
  
    public void testAddUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass123");
        user.setRole("staff");
        user.setStatus("active");
        
      
        when(mockUserDAO.generateNextUserId()).thenReturn("USR0001");
        when(mockUserDAO.addUser(any(User.class))).thenReturn(true);
        
      
        boolean result = userService.addUser(user);
        
      
        assertTrue(result, "User should be added successfully");
        assertEquals("USR0001", user.getUser_id(), "User ID should be set");
        
      
        verify(mockUserDAO, times(1)).generateNextUserId();
        verify(mockUserDAO, times(1)).addUser(user);
    }
    
    @Test
 
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User("USR0001", "testuser1", "pass1", "staff", "active");
        User user2 = new User("USR0002", "testuser2", "pass2", "admin", "active");
        List<User> expectedUsers = List.of(user1, user2);
        
        when(mockUserDAO.getAllUsers()).thenReturn(expectedUsers);
        
     
        List<User> users = userService.getAllUsers();
        
    
        assertNotNull(users, "Users list should not be null");
        assertEquals(2, users.size(), "Should return 2 users");
        assertEquals("testuser1", users.get(0).getUsername());
        assertEquals("testuser2", users.get(1).getUsername());
        
        verify(mockUserDAO, times(1)).getAllUsers();
    }
    
    @Test
  
    public void testUpdateUser() {
        // Arrange
        User user = new User("USR0001", "testuser", "oldpass", "staff", "active");
        user.setPassword("newpass123");
        
        when(mockUserDAO.updateUser(user)).thenReturn(true);
        
     
        boolean result = userService.updateUser(user);
        
       
        assertTrue(result, "User should be updated successfully");
        
        verify(mockUserDAO, times(1)).updateUser(user);
    }
    
    @Test
  
    public void testDeleteUser() {
        // Arrange
        String userId = "USR0001";
        when(mockUserDAO.deleteUserById(userId)).thenReturn(true);
        
      
        boolean result = userService.deleteUserById(userId);
        
       
        assertTrue(result, "User should be deleted successfully");
        
        verify(mockUserDAO, times(1)).deleteUserById(userId);
    }
    
    @Test
    public void testGetUserByUsername() {
      
        String username = "testuser";
        User expectedUser = new User("USR0001", username, "password", "staff", "active");
        
        when(mockUserDAO.getUserByUsername(username)).thenReturn(expectedUser);
        
    
        User result = userService.getUserByUsername(username);
        
       
        assertNotNull(result, "User should not be null");
        assertEquals(username, result.getUsername());
        assertEquals("USR0001", result.getUser_id());
        
        verify(mockUserDAO, times(1)).getUserByUsername(username);
    }
    
    @Test
    public void testGetUserById() {
      
        String userId = "USR0001";
        User expectedUser = new User(userId, "testuser", "password", "staff", "active");
        
        when(mockUserDAO.getUserById(userId)).thenReturn(expectedUser);
        
       
        User result = userService.getUserById(userId);
        
      
        assertNotNull(result, "User should not be null");
        assertEquals(userId, result.getUser_id());
        assertEquals("testuser", result.getUsername());
        
        verify(mockUserDAO, times(1)).getUserById(userId);
    }
    
    @Test
    public void testAddUser_NullUser() {
       
        boolean result = userService.addUser(null);
        assertFalse(result, "Adding null user should return false");
        
       
        verifyNoInteractions(mockUserDAO);
    }
    
    @Test
    public void testAddUser_NullUsername() {
      
        User user = new User();
        user.setUsername(null);
        user.setPassword("password");
        user.setRole("staff");
        
       
        boolean result = userService.addUser(user);
        
      
        assertFalse(result, "Adding user with null username should return false");
        
      
        verifyNoInteractions(mockUserDAO);
    }
    
    @Test
    public void testAddUser_NullPassword() {
       
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(null);
        user.setRole("staff");
        
      
        boolean result = userService.addUser(user);
        
       
        assertFalse(result, "Adding user with null password should return false");
        
       
        verifyNoInteractions(mockUserDAO);
    }
    
    @Test
    public void testGenerateNextUserId() {
        
        when(mockUserDAO.generateNextUserId()).thenReturn("USR0005");
        
        
        String result = userService.generateNextUserId();
        
        
        assertEquals("USR0005", result, "Should return the generated user ID");
        
        verify(mockUserDAO, times(1)).generateNextUserId();
    }
    
    @Test
    public void testUpdateUserStatus() {
      
        String userId = "USR0001";
        String newStatus = "inactive";
        
        when(mockUserDAO.updateUserStatus(userId, newStatus)).thenReturn(true);
        
     
        boolean result = userService.updateUserStatus(userId, newStatus);
        
      
        assertTrue(result, "User status should be updated successfully");
        
        verify(mockUserDAO, times(1)).updateUserStatus(userId, newStatus);
    }
}