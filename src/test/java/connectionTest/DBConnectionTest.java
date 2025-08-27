package connectionTest;
import static org.junit.Assert.fail;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;


import connection.DBConnectionFactory;

public class DBConnectionTest {
	
	  @Test
	    public void testGetConnection() {
	        try (Connection conn = DBConnectionFactory.getConnection()) {
	          
	            assertNotNull(conn, "Connection should not be null");

	           
	            assertFalse(conn.isClosed(), "Connection should be open");

	          
	            assertTrue(conn.getAutoCommit(), "Connection should have autoCommit enabled");

	          
	            assertNotNull(conn.getMetaData().getDatabaseProductName(),
	                    "Database product name should not be null");

	        } catch (SQLException e) {
	            fail("Database connection failed: " + e.getMessage());
	        }
	    }

//	@Test
//    public void testGetConnection() {
//        Connection conn = null;
//        try {
//            // Attempt to get connection
//            conn = DBConnection.getConnection();
//
//            // Assert connection is not null
//            assertNotNull( "Connection should not be null",conn);
//
//            // Assert connection is open
//            assertFalse( "Connection should be open",conn.isClosed());
//
//        } catch (SQLException e) {
//            fail("Database connection failed: " + e.getMessage());
//        } finally {
//            // Close connection
//            DBConnection.close(conn);
//        }
//    }


}
