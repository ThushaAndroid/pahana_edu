package serviceTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dao.ItemDAO;
import model.Item;
import service.ItemService;

public class ItemServiceTest {
    
    private ItemService itemService;
    
    @Mock
    private ItemDAO mockItemDAO;
    
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemService();
        
       
        java.lang.reflect.Field daoField = ItemService.class.getDeclaredField("itemDAO");
        daoField.setAccessible(true);
        daoField.set(itemService, mockItemDAO);
    }
    
    @Test
    public void testGenerateNextItemCode() {
      
        String expectedCode = "ITMC005";
        when(mockItemDAO.generateNextItemCode()).thenReturn(expectedCode);
        
       
        String result = itemService.generateNextItemCode();
        
        
        assertEquals(expectedCode, result);
        verify(mockItemDAO, times(1)).generateNextItemCode();
    }
    
    @Test
    public void testAddItem_ValidItem() {
        
        Item item = new Item();
        item.setItemName("Test Item");
        item.setDescription("Test Description");
        item.setPrice(99.99);
        item.setQuantity(10);
        
        when(mockItemDAO.generateNextItemCode()).thenReturn("ITMC001");
        when(mockItemDAO.addItem(any(Item.class))).thenReturn(true);
        
      
        boolean result = itemService.addItem(item);
        
       
        assertTrue(result, "Item should be added successfully");
        assertEquals("ITMC001", item.getItemCode(), "Item code should be set");
        
        verify(mockItemDAO, times(1)).generateNextItemCode();
        verify(mockItemDAO, times(1)).addItem(item);
    }
    
    @Test
    public void testAddItem_NullItem() {
        
        boolean result = itemService.addItem(null);
        
       
        assertFalse(result, "Adding null item should return false");
        
       
        verifyNoInteractions(mockItemDAO);
    }
    
    @Test
    public void testAddItem_NullItemName() {
        // Arrange
        Item item = new Item();
        item.setItemName(null);
        item.setDescription("Test Description");
        item.setPrice(99.99);
        item.setQuantity(10);
        
       
        boolean result = itemService.addItem(item);
        
        
        assertFalse(result, "Adding item with null name should return false");
        
       
        verifyNoInteractions(mockItemDAO);
    }
    
    @Test
    public void testAddItem_EmptyItemName() {
        // Arrange
        Item item = new Item();
        item.setItemName("");
        item.setDescription("Test Description");
        item.setPrice(99.99);
        item.setQuantity(10);
        
        when(mockItemDAO.generateNextItemCode()).thenReturn("ITMC002");
        when(mockItemDAO.addItem(any(Item.class))).thenReturn(false);
        
      
        boolean result = itemService.addItem(item);
        
       
        assertFalse(result, "Adding item with empty name should return false");
        assertEquals("ITMC002", item.getItemCode(), "Item code should still be set");
        
        verify(mockItemDAO, times(1)).generateNextItemCode();
        verify(mockItemDAO, times(1)).addItem(item);
    }
    
    @Test
    public void testGetItemByCode_ExistingItem() {
        // Arrange
        String itemCode = "ITMC001";
        Item expectedItem = new Item(itemCode, "Test Item", "Description", 50.0, 5);
        
        when(mockItemDAO.getItemByCode(itemCode)).thenReturn(expectedItem);
        
      
        Item result = itemService.getItemByCode(itemCode);
        
      
        assertNotNull(result, "Item should not be null");
        assertEquals(itemCode, result.getItemCode());
        assertEquals("Test Item", result.getItemName());
        assertEquals(50.0, result.getPrice());
        
        verify(mockItemDAO, times(1)).getItemByCode(itemCode);
    }
    
    @Test
    public void testGetItemByCode_NonExistingItem() {
      
        String itemCode = "ITMC999";
        when(mockItemDAO.getItemByCode(itemCode)).thenReturn(null);
        
       
        Item result = itemService.getItemByCode(itemCode);
        
       
        assertNull(result, "Non-existing item should return null");
        
        verify(mockItemDAO, times(1)).getItemByCode(itemCode);
    }
    
    @Test
    public void testGetItemByName_ExistingItem() {
       
        String itemName = "Laptop";
        Item expectedItem = new Item("ITMC001", itemName, "Gaming Laptop", 1200.0, 3);
        
        when(mockItemDAO.getItemByName(itemName)).thenReturn(expectedItem);
        
      
        Item result = itemService.getItemByName(itemName);
        
       
        assertNotNull(result, "Item should not be null");
        assertEquals(itemName, result.getItemName());
        assertEquals("ITMC001", result.getItemCode());
        assertEquals(1200.0, result.getPrice());
        
        verify(mockItemDAO, times(1)).getItemByName(itemName);
    }
    
    @Test
    public void testGetItemByName_NonExistingItem() {
      
        String itemName = "NonExistentItem";
        when(mockItemDAO.getItemByName(itemName)).thenReturn(null);
        
       
        Item result = itemService.getItemByName(itemName);
        
       
        assertNull(result, "Non-existing item should return null");
        
        verify(mockItemDAO, times(1)).getItemByName(itemName);
    }
    
    @Test
    public void testUpdateItem_ValidItem() {
       
        Item item = new Item("ITMC001", "Updated Item", "Updated Description", 75.0, 15);
        when(mockItemDAO.updateItem(item)).thenReturn(true);
        
        
        boolean result = itemService.updateItem(item);
        
        
        assertTrue(result, "Item should be updated successfully");
        
        verify(mockItemDAO, times(1)).updateItem(item);
    }
    
    @Test
    public void testUpdateItem_InvalidItem() {
      
        Item item = new Item("ITMC999", "Non-existing Item", "Description", 50.0, 10);
        when(mockItemDAO.updateItem(item)).thenReturn(false);
        
       
        boolean result = itemService.updateItem(item);
        
        
        assertFalse(result, "Updating non-existing item should return false");
        
        verify(mockItemDAO, times(1)).updateItem(item);
    }
    
    @Test
    public void testDeleteItem_ExistingItem() {
      
        String itemCode = "ITMC001";
        when(mockItemDAO.deleteItem(itemCode)).thenReturn(true);
        
       
        boolean result = itemService.deleteItem(itemCode);
        
       
        assertTrue(result, "Item should be deleted successfully");
        
        verify(mockItemDAO, times(1)).deleteItem(itemCode);
    }
    
    @Test
    public void testDeleteItem_NonExistingItem() {
      
        String itemCode = "ITMC999";
        when(mockItemDAO.deleteItem(itemCode)).thenReturn(false);
        
       
        boolean result = itemService.deleteItem(itemCode);
        
       
        assertFalse(result, "Deleting non-existing item should return false");
        
        verify(mockItemDAO, times(1)).deleteItem(itemCode);
    }
    
    @Test
    public void testGetAllItems_WithItems() {
        // Arrange
        Item item1 = new Item("ITMC001", "Laptop", "Gaming Laptop", 1200.0, 3);
        Item item2 = new Item("ITMC002", "Mouse", "Wireless Mouse", 25.0, 20);
        Item item3 = new Item("ITMC003", "Keyboard", "Mechanical Keyboard", 80.0, 15);
        
        List<Item> expectedItems = Arrays.asList(item1, item2, item3);
        when(mockItemDAO.getAllItems()).thenReturn(expectedItems);
        
      
        List<Item> result = itemService.getAllItems();
        
        
        assertNotNull(result, "Items list should not be null");
        assertEquals(3, result.size(), "Should return 3 items");
        assertEquals("Laptop", result.get(0).getItemName());
        assertEquals("Mouse", result.get(1).getItemName());
        assertEquals("Keyboard", result.get(2).getItemName());
        
        verify(mockItemDAO, times(1)).getAllItems();
    }
    
    @Test
    public void testGetAllItems_EmptyList() {
        // Arrange
        List<Item> emptyList = new ArrayList<>();
        when(mockItemDAO.getAllItems()).thenReturn(emptyList);
        
     
        List<Item> result = itemService.getAllItems();
        
       
        assertNotNull(result, "Items list should not be null");
        assertTrue(result.isEmpty(), "Items list should be empty");
        
        verify(mockItemDAO, times(1)).getAllItems();
    }
    
    @Test
    public void testSearchItems_WithResults() {
       
        String query = "laptop";
        Item item1 = new Item("ITMC001", "Gaming Laptop", "High-end gaming laptop", 1500.0, 2);
        Item item2 = new Item("ITMC004", "Business Laptop", "Office laptop", 800.0, 5);
        
        List<Item> expectedItems = Arrays.asList(item1, item2);
        when(mockItemDAO.searchItems(query)).thenReturn(expectedItems);
        
      
        List<Item> result = itemService.searchItems(query);
        
      
        assertNotNull(result, "Search results should not be null");
        assertEquals(2, result.size(), "Should return 2 matching items");
        assertTrue(result.get(0).getItemName().toLowerCase().contains(query));
        assertTrue(result.get(1).getItemName().toLowerCase().contains(query));
        
        verify(mockItemDAO, times(1)).searchItems(query);
    }
    
    @Test
    public void testSearchItems_NoResults() {
      
        String query = "nonexistent";
        List<Item> emptyList = new ArrayList<>();
        when(mockItemDAO.searchItems(query)).thenReturn(emptyList);
        
       
        List<Item> result = itemService.searchItems(query);
        
       
        assertNotNull(result, "Search results should not be null");
        assertTrue(result.isEmpty(), "Search results should be empty");
        
        verify(mockItemDAO, times(1)).searchItems(query);
    }
    
    @Test
    public void testSearchItems_EmptyQuery() {
      
        String query = "";
        List<Item> emptyList = new ArrayList<>();
        when(mockItemDAO.searchItems(query)).thenReturn(emptyList);
        
        
        List<Item> result = itemService.searchItems(query);
        
      
        assertNotNull(result, "Search results should not be null");
        assertTrue(result.isEmpty(), "Search results should be empty for empty query");
        
        verify(mockItemDAO, times(1)).searchItems(query);
    }
    
    @Test
    public void testSearchItems_NullQuery() {
     
        String query = null;
        List<Item> emptyList = new ArrayList<>();
        when(mockItemDAO.searchItems(query)).thenReturn(emptyList);
        
      
        List<Item> result = itemService.searchItems(query);
        
      
        assertNotNull(result, "Search results should not be null");
        assertTrue(result.isEmpty(), "Search results should be empty for null query");
        
        verify(mockItemDAO, times(1)).searchItems(query);
    }
    
    @Test
    public void testItemServiceWorkflow() {
      
        Item newItem = new Item();
        newItem.setItemName("Test Workflow Item");
        newItem.setDescription("Item for testing workflow");
        newItem.setPrice(100.0);
        newItem.setQuantity(5);
        
        when(mockItemDAO.generateNextItemCode()).thenReturn("ITMC010");
        when(mockItemDAO.addItem(any(Item.class))).thenReturn(true);
        
        boolean added = itemService.addItem(newItem);
        assertTrue(added, "Item should be added");
        assertEquals("ITMC010", newItem.getItemCode());
        
     
        Item retrievedItem = new Item("ITMC010", "Test Workflow Item", "Item for testing workflow", 100.0, 5);
        when(mockItemDAO.getItemByCode("ITMC010")).thenReturn(retrievedItem);
        
        Item foundItem = itemService.getItemByCode("ITMC010");
        assertNotNull(foundItem);
        assertEquals("Test Workflow Item", foundItem.getItemName());
        
       
        foundItem.setPrice(120.0);
        foundItem.setQuantity(8);
        when(mockItemDAO.updateItem(foundItem)).thenReturn(true);
        
        boolean updated = itemService.updateItem(foundItem);
        assertTrue(updated, "Item should be updated");
        
       
        when(mockItemDAO.deleteItem("ITMC010")).thenReturn(true);
        
        boolean deleted = itemService.deleteItem("ITMC010");
        assertTrue(deleted, "Item should be deleted");
        
     
        verify(mockItemDAO, times(1)).generateNextItemCode();
        verify(mockItemDAO, times(1)).addItem(newItem);
        verify(mockItemDAO, times(1)).getItemByCode("ITMC010");
        verify(mockItemDAO, times(1)).updateItem(foundItem);
        verify(mockItemDAO, times(1)).deleteItem("ITMC010");
    }
}