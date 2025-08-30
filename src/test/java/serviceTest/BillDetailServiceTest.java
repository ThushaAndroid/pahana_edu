package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.BillDetailDAO;
import model.BillDetail;
import service.BillDetailService;

public class BillDetailServiceTest {

    private BillDetailService billDetailService;
    
    @Mock
    private BillDetailDAO mockBillDetailDAO;

    private BillDetail testBillDetail;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        billDetailService = BillDetailService.getInstance();
        
      
        java.lang.reflect.Field daoField = BillDetailService.class.getDeclaredField("billDetailDAO");
        daoField.setAccessible(true);
        daoField.set(billDetailService, mockBillDetailDAO);

        testBillDetail = new BillDetail(
            "INV001",
            "ITEM001", 
            "BOOK",
            "Reading book",
            10.50,
            2,
            21.00
        );
    }

  
    @Test
    public void testInsertBillDetail_ValidBillDetail() {
      
        when(mockBillDetailDAO.insertBillDetail(testBillDetail)).thenReturn(true);

      
        boolean result = billDetailService.insertBillDetail(testBillDetail);

       
        assertTrue(result, "Bill detail should be inserted successfully");
        verify(mockBillDetailDAO, times(1)).insertBillDetail(testBillDetail);
    }

    
    @Test
    public void testInsertBillDetail_NullBillDetail() {
       
        boolean result = billDetailService.insertBillDetail(null);

       
        assertFalse(result, "Inserting null bill detail should return false");
        verifyNoInteractions(mockBillDetailDAO);
    }

    
    @Test
    public void testGetBillDetailsByInvoice_ExistingInvoice() {
     
        String invoiceNo = "INV001";
        List<BillDetail> expectedBillDetails = Arrays.asList(testBillDetail);
        when(mockBillDetailDAO.getBillDetailsByInvoice(invoiceNo)).thenReturn(expectedBillDetails);

    
        List<BillDetail> result = billDetailService.getBillDetailsByInvoice(invoiceNo);

     
        assertNotNull(result, "Bill details list should not be null");
        assertEquals(1, result.size(), "Should return 1 bill detail");
        assertEquals("ITEM001", result.get(0).getItemCode());
        assertEquals("BOOK", result.get(0).getItemName());
        assertEquals(21.00, result.get(0).getTotal());
        
        verify(mockBillDetailDAO, times(1)).getBillDetailsByInvoice(invoiceNo);
    }

  
    @Test
    public void testInsertBillDetail_EmptyInvoiceNo() {
      
        BillDetail billWithEmptyInvoice = new BillDetail(
            "",
            "ITEM001",
            "BOOK", 
            "Reading book",
            10.50,
            2,
            21.00
        );

        
        boolean result = billDetailService.insertBillDetail(billWithEmptyInvoice);

  
        assertFalse(result, "Inserting bill detail with empty invoice number should return false");
        verifyNoInteractions(mockBillDetailDAO);
    }

 
    @Test
    public void testGetBillDetailsByInvoice_NonExistingInvoice() {
             String invoiceNo = "INV999";
        List<BillDetail> emptyList = new ArrayList<>();
        when(mockBillDetailDAO.getBillDetailsByInvoice(invoiceNo)).thenReturn(emptyList);

        List<BillDetail> result = billDetailService.getBillDetailsByInvoice(invoiceNo);

        // Assert
        assertNotNull(result, "Bill details list should not be null");
        assertTrue(result.isEmpty(), "Bill details list should be empty for non-existing invoice");
        verify(mockBillDetailDAO, times(1)).getBillDetailsByInvoice(invoiceNo);
    }
}