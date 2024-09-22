import com.example.demoplswork.model.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void setUp() {
        account = new Account("Nicolas", "Tabakov", "n11294035@qut.edu.au", "Pass.123");
    }

    @Test
    public void getfirstName() {
        assertEquals("Nicolas", account.getfirstName());
    }

    @Test
    public void getlastName() {
        assertEquals("Tabakov", account.getlastName());
    }

    @Test
    public void getemail() {
        assertEquals("n11294035@qut.edu.au", account.getemail());
    }

    @Test
    public void getpassword() {
        assertEquals("Pass.123", account.getpassword());
    }

}
