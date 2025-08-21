package connectionTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import connection.DBConnection;

public class DBConnectionTest {

	@Test
    public void testGetConnection() {
        Connection conn = null;
        try {
            // Attempt to get connection
            conn = DBConnection.getConnection();

            // Assert connection is not null
            assertNotNull( "Connection should not be null",conn);

            // Assert connection is open
            assertFalse( "Connection should be open",conn.isClosed());

        } catch (SQLException e) {
            fail("Database connection failed: " + e.getMessage());
        } finally {
            // Close connection
            DBConnection.close(conn);
        }
    }


}
