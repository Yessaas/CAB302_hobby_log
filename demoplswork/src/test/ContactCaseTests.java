import static org.junit.jupiter.api.Assertions.*;

import com.example.demoplswork.model.Contact;
import com.example.demoplswork.model.ProfileDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ContactCaseTests {

    private ProfileDAO profileDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up an in-memory SQLite database for testing
        String url = "jdbc:sqlite::memory:";
        connection = DriverManager.getConnection(url);
        profileDAO = new ProfileDAO();
        profileDAO.connection = connection; // Set the inherited connection from BaseDAO
        profileDAO.createProfileTable(); // Create the user_profiles table in the in-memory database
    }

    @Test
    public void testInsertProfile_NullBioAndPhoto() {
        // Attempt to insert a profile with null bio and photo
        assertDoesNotThrow(() -> {
            profileDAO.insertProfile(1, null, null);
        });
    }

    @Test
    public void testInsertProfile_InvalidUserId() {
        // Attempt to insert a profile with an invalid user ID
        SQLException exception = assertThrows(SQLException.class, () -> {
            profileDAO.insertProfile(-1, "Test Bio", "test_photo.jpg");
        });
        assertNotNull(exception);
    }

    @Test
    public void testGetProfileByUserId_InvalidUserId() {
        // Attempt to retrieve a profile for an invalid user ID
        Contact contact = new Contact("FirstName", "LastName", "Bio", "photo.png");
        profileDAO.getProfileByUserId(contact, -1);
        assertNull(contact.getBio());
        assertNull(contact.getPhoto());
    }

    @Test
    public void testUpdateProfile_NullFields() {
        // Insert a valid profile
        profileDAO.insertProfile(1, "Test Bio", "test_photo.jpg");

        // Attempt to update the profile with null fields
        assertDoesNotThrow(() -> {
            profileDAO.updateProfile(1, null, null);
        });
    }

    @Test
    public void testUpdateProfile_InvalidUserId() {
        // Attempt to update a profile with an invalid user ID
        SQLException exception = assertThrows(SQLException.class, () -> {
            profileDAO.updateProfile(-1, "Updated Bio", "updated_photo.jpg");
        });
        assertNotNull(exception);
    }

    /*@Test
    public void testDeleteProfile_InvalidUserId() {
        // Attempt to delete a profile for an invalid user ID
        SQLException exception = assertThrows(SQLException.class, () -> {
            profileDAO.deleteProfile(-1);
        });
        assertNotNull(exception);
    }*/

    @Test
    public void testInsertProfile_DuplicateUserId() {
        // Insert a profile with a valid user ID
        profileDAO.insertProfile(1, "Test Bio", "test_photo.jpg");

        // Attempt to insert another profile with the same user ID
        profileDAO.insertProfile(1, "Duplicate Bio", "duplicate_photo.jpg");
        Contact contact = new Contact("FirstName", "LastName", "Bio", "photo.png");
        profileDAO.getProfileByUserId(contact, 1);
        assertEquals("Test Bio", contact.getBio());
    }

}
