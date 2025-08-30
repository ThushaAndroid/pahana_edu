package serviceTest;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import dao.CustomerDAO;
import model.Customer;
import service.CustomerService;

public class CustomerServiceTest {
    
    private CustomerService customerService;
    
    @Mock
    private CustomerDAO customerDAOMock;
    
    @BeforeEach
    public void setUp() throws Exception {
       
        MockitoAnnotations.openMocks(this);
        customerService = CustomerService.getInstance();
        
      
        Field daoField = CustomerService.class.getDeclaredField("customerDAO");
        daoField.setAccessible(true);
        daoField.set(customerService, customerDAOMock);
    }
    
    @Test
    public void testGenerateNextAccountNumber() {
     
        when(customerDAOMock.generateNextAccountNumber()).thenReturn("CUST010");
        
     
        String accountNumber = customerService.generateNextAccountNumber();
        
       
        assertEquals("CUST010", accountNumber);
        verify(customerDAOMock, times(1)).generateNextAccountNumber();
    }
    
    @Test
    public void testAddCustomer() {
        // Arrange
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setNic("123456789V");
        customer.setAddress("Test Address");
        customer.setTelephone("077126787");
        customer.setEmail("john@test.com");
        
        when(customerDAOMock.generateNextAccountNumber()).thenReturn("CUST011");
        when(customerDAOMock.addCustomer(any(Customer.class))).thenReturn(true);
        
     
        boolean result = customerService.addCustomer(customer);
        
       
        assertTrue(result);
        assertEquals("CUST011", customer.getAccountNumber());
        verify(customerDAOMock, times(1)).generateNextAccountNumber();
        verify(customerDAOMock, times(1)).addCustomer(customer);
    }
    
    @Test
    public void testGetCustomerByNIC() {
     
        String nic = "123456789V";
        Customer expectedCustomer = new Customer();
        expectedCustomer.setAccountNumber("CUST001");
        expectedCustomer.setNic(nic);
        expectedCustomer.setName("Alice");
        expectedCustomer.setAddress("Street 1");
        expectedCustomer.setTelephone("0773464567");
        expectedCustomer.setEmail("alice@test.com");
        
        when(customerDAOMock.getCustomerByNIC(nic)).thenReturn(expectedCustomer);
        
      
        Customer result = customerService.getCustomerByNIC(nic);
        
       
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertEquals(nic, result.getNic());
        verify(customerDAOMock, times(1)).getCustomerByNIC(nic);
    }
    
    @Test
    public void testGetAllCustomers() {
      
        Customer c1 = new Customer();
        c1.setAccountNumber("CUST001");
        c1.setNic("NIC1");
        c1.setName("Alice");
        c1.setAddress("Addr1");
        c1.setTelephone("0774554567");
        c1.setEmail("a@test.com");
        
        Customer c2 = new Customer();
        c2.setAccountNumber("CUST002");
        c2.setNic("NIC2");
        c2.setName("Bob");
        c2.setAddress("Addr2");
        c2.setTelephone("0771234567");
        c2.setEmail("b@test.com");
        
        List<Customer> expectedCustomers = Arrays.asList(c1, c2);
        when(customerDAOMock.getAllCustomers()).thenReturn(expectedCustomers);
        
      
        List<Customer> customers = customerService.getAllCustomers();
        
        
        assertNotNull(customers);
        assertEquals(2, customers.size());
        assertEquals("Alice", customers.get(0).getName());
        assertEquals("Bob", customers.get(1).getName());
        verify(customerDAOMock, times(1)).getAllCustomers();
    }
}