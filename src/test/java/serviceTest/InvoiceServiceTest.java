package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.InvoiceDAO;
import model.Invoice;
import service.InvoiceService;

public class InvoiceServiceTest {
    
    private InvoiceService invoiceService;
    
    @Mock
    private InvoiceDAO mockInvoiceDAO;
    
    private Date testDate;
    private Date testDueDate;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        invoiceService = InvoiceService.getInstance();
        
      
        java.lang.reflect.Field daoField = InvoiceService.class.getDeclaredField("invoiceDAO");
        daoField.setAccessible(true);
        daoField.set(invoiceService, mockInvoiceDAO);
        
    
        Calendar cal = Calendar.getInstance();
        testDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30); // Due date 30 days from now
        testDueDate = cal.getTime();
    }
    
    @Test
    public void testGenerateNextInvoiceNo() {
      
        String expectedInvoiceNo = "INV005";
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn(expectedInvoiceNo);
        
       
        String result = invoiceService.generateNextInvoiceNo();
        
       
        assertEquals(expectedInvoiceNo, result);
        verify(mockInvoiceDAO, times(1)).generateNextInvoiceNo();
    }
    
    @Test
    public void testInsertInvoice_ValidInvoice() {
     
        Invoice invoice = new Invoice();
        invoice.setCustomerName("John Doe");
        invoice.setInvoiceDate(testDate);
        invoice.setDueDate(testDueDate);
        invoice.setTotalAmount(1000.0);
        invoice.setCash(800.0);
        invoice.setBalance(200.0);
        invoice.setStatus("Pending");
        
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn("INV001");
        when(mockInvoiceDAO.insertInvoice(any(Invoice.class))).thenReturn(true);
        
        
        boolean result = invoiceService.insertInvoice(invoice);
        
      
        assertTrue(result, "Invoice should be inserted successfully");
        assertEquals("INV001", invoice.getInvoiceNo(), "Invoice number should be set");
        
        verify(mockInvoiceDAO, times(1)).generateNextInvoiceNo();
        verify(mockInvoiceDAO, times(1)).insertInvoice(invoice);
    }
    
    @Test
    public void testInsertInvoice_NullInvoice() {
      
        boolean result = invoiceService.insertInvoice(null);
        
      
        assertFalse(result, "Inserting null invoice should return false");
        
      
        verifyNoInteractions(mockInvoiceDAO);
    }
    
    @Test
    public void testInsertInvoice_NullCustomerName() {
      
        Invoice invoice = new Invoice();
        invoice.setCustomerName(null);
        invoice.setInvoiceDate(testDate);
        invoice.setDueDate(testDueDate);
        invoice.setTotalAmount(1000.0);
        invoice.setStatus("Pending");
        
        
        boolean result = invoiceService.insertInvoice(invoice);
        
       
        assertFalse(result, "Inserting invoice with null customer name should return false");
        
      
        verifyNoInteractions(mockInvoiceDAO);
    }
    
    @Test
    public void testInsertInvoice_EmptyCustomerName() {
       
        Invoice invoice = new Invoice();
        invoice.setCustomerName("");
        invoice.setInvoiceDate(testDate);
        invoice.setDueDate(testDueDate);
        invoice.setTotalAmount(1000.0);
        invoice.setStatus("Pending");
        
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn("INV002");
        when(mockInvoiceDAO.insertInvoice(any(Invoice.class))).thenReturn(false);
        
      
        boolean result = invoiceService.insertInvoice(invoice);
        
        
        assertFalse(result, "Inserting invoice with empty customer name should return false");
        assertEquals("INV002", invoice.getInvoiceNo(), "Invoice number should still be set");
        
        verify(mockInvoiceDAO, times(1)).generateNextInvoiceNo();
        verify(mockInvoiceDAO, times(1)).insertInvoice(invoice);
    }
    
    @Test
    public void testUpdateInvoice_ValidInvoice() {
       
        Invoice invoice = new Invoice("INV001", testDueDate, 900.0, 100.0, "Partially Paid");
        when(mockInvoiceDAO.updateInvoice(invoice)).thenReturn(true);
        
     
        boolean result = invoiceService.updateInvoice(invoice);
        
     
        assertTrue(result, "Invoice should be updated successfully");
        
        verify(mockInvoiceDAO, times(1)).updateInvoice(invoice);
    }
    
    @Test
    public void testUpdateInvoice_NonExistingInvoice() {
      
        Invoice invoice = new Invoice("INV999", testDueDate, 500.0, 0.0, "Paid");
        when(mockInvoiceDAO.updateInvoice(invoice)).thenReturn(false);
        
      
        boolean result = invoiceService.updateInvoice(invoice);
        
        
        assertFalse(result, "Updating non-existing invoice should return false");
        
        verify(mockInvoiceDAO, times(1)).updateInvoice(invoice);
    }
    
    @Test
    public void testDeleteInvoice_ExistingInvoice() {
      
        String invoiceNo = "INV001";
        when(mockInvoiceDAO.deleteInvoice(invoiceNo)).thenReturn(true);
        
      
        boolean result = invoiceService.deleteInvoice(invoiceNo);
        
      
        assertTrue(result, "Invoice should be deleted successfully");
        
        verify(mockInvoiceDAO, times(1)).deleteInvoice(invoiceNo);
    }
    
    @Test
    public void testDeleteInvoice_NonExistingInvoice() {
      
        String invoiceNo = "INV999";
        when(mockInvoiceDAO.deleteInvoice(invoiceNo)).thenReturn(false);
        
       
        boolean result = invoiceService.deleteInvoice(invoiceNo);
        
       
        assertFalse(result, "Deleting non-existing invoice should return false");
        
        verify(mockInvoiceDAO, times(1)).deleteInvoice(invoiceNo);
    }
    
    @Test
    public void testGetInvoiceByNo_ExistingInvoice() {
       
        String invoiceNo = "INV001";
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setInvoiceNo(invoiceNo);
        expectedInvoice.setCustomerName("John Doe");
        expectedInvoice.setInvoiceDate(testDate);
        expectedInvoice.setDueDate(testDueDate);
        expectedInvoice.setTotalAmount(1500.0);
        expectedInvoice.setCash(1200.0);
        expectedInvoice.setBalance(300.0);
        expectedInvoice.setStatus("Pending");
        
        when(mockInvoiceDAO.getInvoiceByNo(invoiceNo)).thenReturn(expectedInvoice);
        
      
        Invoice result = invoiceService.getInvoiceByNo(invoiceNo);
        
      
        assertNotNull(result, "Invoice should not be null");
        assertEquals(invoiceNo, result.getInvoiceNo());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1500.0, result.getTotalAmount());
        assertEquals(300.0, result.getBalance());
        assertEquals("Pending", result.getStatus());
        
        verify(mockInvoiceDAO, times(1)).getInvoiceByNo(invoiceNo);
    }
    
    @Test
    public void testGetInvoiceByNo_NonExistingInvoice() {
     
        String invoiceNo = "INV999";
        when(mockInvoiceDAO.getInvoiceByNo(invoiceNo)).thenReturn(null);
        
      
        Invoice result = invoiceService.getInvoiceByNo(invoiceNo);
        
      
        assertNull(result, "Non-existing invoice should return null");
        
        verify(mockInvoiceDAO, times(1)).getInvoiceByNo(invoiceNo);
    }
    
    @Test
    public void testGetAllInvoices_WithInvoices() {
      
        Invoice invoice1 = new Invoice();
        invoice1.setInvoiceNo("INV001");
        invoice1.setCustomerName("Alice Johnson");
        invoice1.setInvoiceDate(testDate);
        invoice1.setDueDate(testDueDate);
        invoice1.setTotalAmount(800.0);
        invoice1.setBalance(200.0);
        invoice1.setStatus("Pending");
        
        Invoice invoice2 = new Invoice();
        invoice2.setInvoiceNo("INV002");
        invoice2.setCustomerName("Bob Smith");
        invoice2.setInvoiceDate(testDate);
        invoice2.setDueDate(testDueDate);
        invoice2.setTotalAmount(1200.0);
        invoice2.setBalance(0.0);
        invoice2.setStatus("Paid");
        
        List<Invoice> expectedInvoices = Arrays.asList(invoice1, invoice2);
        when(mockInvoiceDAO.getAllInvoices()).thenReturn(expectedInvoices);
        
    
        List<Invoice> result = invoiceService.getAllInvoices();
        
       
        assertNotNull(result, "Invoices list should not be null");
        assertEquals(2, result.size(), "Should return 2 invoices");
        assertEquals("INV001", result.get(0).getInvoiceNo());
        assertEquals("Alice Johnson", result.get(0).getCustomerName());
        assertEquals("INV002", result.get(1).getInvoiceNo());
        assertEquals("Bob Smith", result.get(1).getCustomerName());
        
        verify(mockInvoiceDAO, times(1)).getAllInvoices();
    }
    
    @Test
    public void testGetAllInvoices_EmptyList() {
      
        List<Invoice> emptyList = new ArrayList<>();
        when(mockInvoiceDAO.getAllInvoices()).thenReturn(emptyList);
        
      
        List<Invoice> result = invoiceService.getAllInvoices();
        
      
        assertNotNull(result, "Invoices list should not be null");
        assertTrue(result.isEmpty(), "Invoices list should be empty");
        
        verify(mockInvoiceDAO, times(1)).getAllInvoices();
    }
    
    @Test
    public void testInvoiceServiceWorkflow() {
       
        Invoice newInvoice = new Invoice();
        newInvoice.setCustomerName("Test Customer");
        newInvoice.setInvoiceDate(testDate);
        newInvoice.setDueDate(testDueDate);
        newInvoice.setTotalAmount(2000.0);
        newInvoice.setCash(1500.0);
        newInvoice.setBalance(500.0);
        newInvoice.setStatus("Pending");
        
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn("INV010");
        when(mockInvoiceDAO.insertInvoice(any(Invoice.class))).thenReturn(true);
        
        boolean inserted = invoiceService.insertInvoice(newInvoice);
        assertTrue(inserted, "Invoice should be inserted");
        assertEquals("INV010", newInvoice.getInvoiceNo());
        
      
        Invoice retrievedInvoice = new Invoice();
        retrievedInvoice.setInvoiceNo("INV010");
        retrievedInvoice.setCustomerName("Test Customer");
        retrievedInvoice.setInvoiceDate(testDate);
        retrievedInvoice.setDueDate(testDueDate);
        retrievedInvoice.setTotalAmount(2000.0);
        retrievedInvoice.setCash(1500.0);
        retrievedInvoice.setBalance(500.0);
        retrievedInvoice.setStatus("Pending");
        
        when(mockInvoiceDAO.getInvoiceByNo("INV010")).thenReturn(retrievedInvoice);
        
        Invoice foundInvoice = invoiceService.getInvoiceByNo("INV010");
        assertNotNull(foundInvoice);
        assertEquals("Test Customer", foundInvoice.getCustomerName());
        assertEquals(500.0, foundInvoice.getBalance());
        
       
        foundInvoice.setCash(2000.0);
        foundInvoice.setBalance(0.0);
        foundInvoice.setStatus("Paid");
        when(mockInvoiceDAO.updateInvoice(foundInvoice)).thenReturn(true);
        
        boolean updated = invoiceService.updateInvoice(foundInvoice);
        assertTrue(updated, "Invoice should be updated");
        
     
        when(mockInvoiceDAO.deleteInvoice("INV010")).thenReturn(true);
        
        boolean deleted = invoiceService.deleteInvoice("INV010");
        assertTrue(deleted, "Invoice should be deleted");
        
     
        verify(mockInvoiceDAO, times(1)).generateNextInvoiceNo();
        verify(mockInvoiceDAO, times(1)).insertInvoice(newInvoice);
        verify(mockInvoiceDAO, times(1)).getInvoiceByNo("INV010");
        verify(mockInvoiceDAO, times(1)).updateInvoice(foundInvoice);
        verify(mockInvoiceDAO, times(1)).deleteInvoice("INV010");
    }
    
    @Test
    public void testInvoiceStatusTransitions() {
      
        Invoice pendingInvoice = new Invoice();
        pendingInvoice.setInvoiceNo("INV100");
        pendingInvoice.setCustomerName("Status Test Customer");
        pendingInvoice.setTotalAmount(1000.0);
        pendingInvoice.setCash(600.0);
        pendingInvoice.setBalance(400.0);
        pendingInvoice.setStatus("Partially Paid");
        
        when(mockInvoiceDAO.updateInvoice(pendingInvoice)).thenReturn(true);
        
        boolean partiallyPaid = invoiceService.updateInvoice(pendingInvoice);
        assertTrue(partiallyPaid);
        
   
        pendingInvoice.setCash(1000.0);
        pendingInvoice.setBalance(0.0);
        pendingInvoice.setStatus("Paid");
        
        when(mockInvoiceDAO.updateInvoice(pendingInvoice)).thenReturn(true);
        
        boolean fullyPaid = invoiceService.updateInvoice(pendingInvoice);
        assertTrue(fullyPaid);
        
        verify(mockInvoiceDAO, times(2)).updateInvoice(pendingInvoice);
    }
    
    @Test
    public void testGetInvoicesStatusNDue_ReturnsInvoices() {
        List<Invoice> mockInvoices = new ArrayList<>();
        Invoice inv1 = new Invoice();
        inv1.setInvoiceNo("INV001");
        inv1.setDueDate(new java.sql.Date(System.currentTimeMillis() - 86400000));
        inv1.setStatus("Pending");
        mockInvoices.add(inv1);

        Invoice inv2 = new Invoice();
        inv2.setInvoiceNo("INV002");
        inv2.setDueDate(new java.sql.Date(System.currentTimeMillis() + 86400000));
        inv2.setStatus("Paid");
        mockInvoices.add(inv2);

        when(mockInvoiceDAO.getInvoicesStatusNDue()).thenReturn(mockInvoices);

        List<Invoice> result = invoiceService.getInvoicesStatusNDue();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("INV001", result.get(0).getInvoiceNo());
        assertEquals("Pending", result.get(0).getStatus());

        verify(mockInvoiceDAO, times(1)).getInvoicesStatusNDue();
    }

  
    @Test
    public void testGetInvoicesStatusNDue_EmptyList() {
    	List<Invoice> emptyList = new ArrayList<>();
        when(mockInvoiceDAO.getInvoicesStatusNDue()).thenReturn(emptyList);

        List<Invoice> result = invoiceService.getInvoicesStatusNDue();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Invoice list should be empty");
        verify(mockInvoiceDAO, times(1)).getInvoicesStatusNDue();
    }
    
    @Test
    public void testInvoiceWithDifferentPaymentScenarios() {
     
        Invoice fullPaymentInvoice = new Invoice();
        fullPaymentInvoice.setCustomerName("Full Payment Customer");
        fullPaymentInvoice.setTotalAmount(500.0);
        fullPaymentInvoice.setCash(500.0);
        fullPaymentInvoice.setBalance(0.0);
        fullPaymentInvoice.setStatus("Paid");
        
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn("INV200");
        when(mockInvoiceDAO.insertInvoice(any(Invoice.class))).thenReturn(true);
        
        boolean fullPaymentResult = invoiceService.insertInvoice(fullPaymentInvoice);
        assertTrue(fullPaymentResult);
        assertEquals("INV200", fullPaymentInvoice.getInvoiceNo());
        
     
        Invoice noPaymentInvoice = new Invoice();
        noPaymentInvoice.setCustomerName("Credit Customer");
        noPaymentInvoice.setTotalAmount(1500.0);
        noPaymentInvoice.setCash(0.0);
        noPaymentInvoice.setBalance(1500.0);
        noPaymentInvoice.setStatus("Pending");
        
        when(mockInvoiceDAO.generateNextInvoiceNo()).thenReturn("INV201");
        when(mockInvoiceDAO.insertInvoice(any(Invoice.class))).thenReturn(true);
        
        boolean noPaymentResult = invoiceService.insertInvoice(noPaymentInvoice);
        assertTrue(noPaymentResult);
        assertEquals("INV201", noPaymentInvoice.getInvoiceNo());
        
        verify(mockInvoiceDAO, times(2)).generateNextInvoiceNo();
        verify(mockInvoiceDAO, times(1)).insertInvoice(fullPaymentInvoice);
        verify(mockInvoiceDAO, times(1)).insertInvoice(noPaymentInvoice);
    }
}