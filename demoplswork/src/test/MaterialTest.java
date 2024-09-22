import com.example.demoplswork.model.Material;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaterialTest {
    private Material material;

    @BeforeEach
    public void setUp() {
        material = new Material("Screws", 50, 3.5);
    }

    @Test
    public void getName() {
        assertEquals("Screws", material.getName());
    }

    @Test
    public void getQuantity() {
        assertEquals(50, material.getQuantity());
    }

    @Test
    public void getCost() {
        assertEquals(3.5, material.getCost());
    }

//    @Test
//    public void testGetLastName() {
//        assertEquals("Doe", contact.getLastName());
//    }
//
//    @Test
//    public void testSetLastName() {
//        contact.setLastName("Smith");
//        assertEquals("Smith", contact.getLastName());
//    }
//
}