package it.adesso.management.ordermanagementservice.entities.orders;
import it.adesso.management.ordermanagementservice.entities.external.Basis;
import it.adesso.management.ordermanagementservice.entities.external.ComposedPizza;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
class OrderEntryTest {
    private OrderEntry orderEntry;
    private Order testOrder;
    private ComposedPizza pizza;
    private Basis basis;
    @BeforeEach
    void setUp() {
        testOrder = Order.builder()
                .id(1L)
                .externalId(100L)
                .placedBy("testUser")
                .build();
        basis = Basis.builder()
                .id(1L)
                .name("Base")
                .build();
        pizza = ComposedPizza.builder()
                .id(1L)
                .name("Margherita")
                .base(basis)
                .build();
        orderEntry = OrderEntry.builder()
                .id(1L)
                .order(testOrder)
                .idPizza(pizza)
                .base(basis)
                .addedIngredients(new HashSet<>())
                .removedIngredients(new HashSet<>())
                .build();
    }
    @Test
    void testOrderEntryBuilder() {
        // Assert
        assertNotNull(orderEntry);
        assertEquals(1L, orderEntry.getId());
        assertEquals(testOrder, orderEntry.getOrder());
        assertEquals(pizza, orderEntry.getIdPizza());
        assertEquals(basis, orderEntry.getBase());
    }
    @Test
    void testOrderEntrySetters() {
        // Arrange
        ComposedPizza newPizza = ComposedPizza.builder()
                .id(2L)
                .name("Hawaiiana")
                .base(basis)
                .build();
        // Act
        orderEntry.setIdPizza(newPizza);
        orderEntry.setId(2L);
        // Assert
        assertEquals(2L, orderEntry.getId());
        assertEquals(newPizza, orderEntry.getIdPizza());
    }
    @Test
    void testOrderEntryWithIngredients() {
        // Arrange
        Set<OrderEntryAddedIngredient> addedIngredients = new HashSet<>();
        Set<OrderEntryRemovedIngredient> removedIngredients = new HashSet<>();
        // Act
        orderEntry.setAddedIngredients(addedIngredients);
        orderEntry.setRemovedIngredients(removedIngredients);
        // Assert
        assertNotNull(orderEntry.getAddedIngredients());
        assertNotNull(orderEntry.getRemovedIngredients());
        assertTrue(orderEntry.getAddedIngredients().isEmpty());
        assertTrue(orderEntry.getRemovedIngredients().isEmpty());
    }
    @Test
    void testOrderEntryNoArgsConstructor() {
        // Act
        OrderEntry newEntry = new OrderEntry();
        // Assert
        assertNotNull(newEntry);
        assertNull(newEntry.getId());
    }
    @Test
    void testOrderEntryWithNullPizza() {
        // Act
        orderEntry.setIdPizza(null);
        // Assert
        assertNull(orderEntry.getIdPizza());
    }
    @Test
    void testOrderEntryWithNullBase() {
        // Act
        orderEntry.setBase(null);
        // Assert
        assertNull(orderEntry.getBase());
    }
}
