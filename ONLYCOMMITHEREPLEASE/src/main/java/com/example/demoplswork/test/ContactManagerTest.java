import com.example.demoplswork.Contact;
import com.example.demoplswork.model.ContactManager;
import com.example.demoplswork.model.SqliteContactDAO;
import com.example.demoplswork.model.SqliteConnection;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactManagerTest {
    private ContactManager contactManager;
    private SqliteContactDAO sqliteContactDAO;
    private Contact[] contacts = {
            new Contact("John", "Doe", "johndoe@example.com", "0423423423"),
            new Contact("Jane", "Doe", "janedoe@example.com", "0423423424"),
            new Contact("Jay", "Doe", "jaydoe@example.com", "0423423425"),
            new Contact("John", "Smith", "johnsmith@example.com", "0423423426"),
            new Contact("Jane", "Smith", "janesmith@example.com", "0423423427"),
            new Contact("Alice", "Graystone", "aliceg@gmail.com", "0423423428"),
            new Contact("Shane", "Graystone", "shaneg@gmail.com", "0423423429")
    };

    @BeforeEach
    public void setUp() {
        sqliteContactDAO = new SqliteContactDAO();
        contactManager = new ContactManager(sqliteContactDAO);
        clearDatabase();
    }

    @AfterEach
    public void tearDown() {
        if (sqliteContactDAO != null) {
            sqliteContactDAO.closeConnection();
        }
    }

    private void clearDatabase() {
        try {
            String query = "DELETE FROM users";
            SqliteConnection.getConnection().createStatement().execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSearchInOneContact() {
        contactManager.createAccount(contacts[0]);
        List<Contact> result = contactManager.searchContacts("John");
        assertEquals(1, result.size());
        assertEquals(contacts[0], result.get(0));
    }

    @Test
    public void testSearchInMultipleContacts() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("Doe");
        assertEquals(3, result.size());
        for (Contact contact : result) {
            assertTrue(contact.getLastName().equals("Doe"));
        }
    }

    @Test
    public void testSearchNoResults() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("Dan");
        assertEquals(0, result.size());
    }

    @Test
    public void testSearchEmptyQuery() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("");
        assertEquals(contacts.length, result.size());
    }

    @Test
    public void testSearchNullQuery() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts(null);
        assertEquals(contacts.length, result.size());
    }

    @Test
    public void testSearchCaseInsensitive() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("jane");
        assertEquals(2, result.size());
        for (Contact contact : result) {
            assertTrue(contact.getFirstName().equalsIgnoreCase("Jane"));
        }
    }

    @Test
    public void testSearchPartialQuery() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("ane");
        assertEquals(3, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Shane", result.get(2).getFirstName());
    }

    @Test
    public void testSearchEmptyContacts() {
        List<Contact> result = contactManager.searchContacts("John");
        assertEquals(0, result.size());
    }

    @Test
    public void testSearchByEmail() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("example.com");
        assertEquals(5, result.size());
    }

    @Test
    public void testSearchByPhone() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("0423423423");
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    public void testSearchByFullName() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("Jane Doe");
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
    }

    @Test
    public void testSearchByFullNameCaseInsensitive() {
        for (Contact contact : contacts) {
            contactManager.createAccount(contact);
        }
        List<Contact> result = contactManager.searchContacts("jane doe");
        assertEquals(1, result.size());
        assertEquals("Jane", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
    }
}
