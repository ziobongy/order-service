package it.adesso.management.ordermanagementservice.services.impl;
import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderEntryDTO;
import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import it.adesso.management.ordermanagementservice.entities.external.Basis;
import it.adesso.management.ordermanagementservice.entities.external.ComposedPizza;
import it.adesso.management.ordermanagementservice.entities.external.Ingredient;
import it.adesso.management.ordermanagementservice.entities.orders.*;
import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
import it.adesso.management.ordermanagementservice.enums.OrderStatusEnum;
import it.adesso.management.ordermanagementservice.exceptions.NotFoundException;
import it.adesso.management.ordermanagementservice.mappers.BFF.OrderBFFMapper;
import it.adesso.management.ordermanagementservice.repositories.OrderEntryAddedIngredientsRepository;
import it.adesso.management.ordermanagementservice.repositories.OrderEntryRemovedIngredientsRepository;
import it.adesso.management.ordermanagementservice.repositories.OrderEntryRepository;
import it.adesso.management.ordermanagementservice.repositories.OrderRepository;
import it.adesso.management.ordermanagementservice.repositories.external.BasisRepository;
import it.adesso.management.ordermanagementservice.repositories.external.ComposedPizzaRepository;
import it.adesso.management.ordermanagementservice.repositories.external.IngredientRepository;
import it.adesso.management.ordermanagementservice.services.JwtUtilService;
import it.adesso.management.ordermanagementservice.services.QueueService;
import it.adesso.management.ordermanagementservice.services.external.AnagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderBFFMapper orderMapper;
    @Mock
    private QueueService queueService;
    @Mock
    private JwtUtilService jwtUtilService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ComposedPizzaRepository composedPizzaRepository;
    @Mock
    private BasisRepository basisRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private AnagService anagService;
    @Mock
    private OrderEntryRepository orderEntryRepository;
    @Mock
    private OrderEntryAddedIngredientsRepository orderEntryAddedIngredientsRepository;
    @Mock
    private OrderEntryRemovedIngredientsRepository orderEntryRemovedIngredientsRepository;
    @InjectMocks
    private OrderServiceImpl orderService;
    private Order testOrder;
    private OrderBffDTO testOrderBffDTO;
    private Pageable pageable;
    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        testOrder = Order.builder()
                .id(1L)
                .externalId(100L)
                .placedBy("testUser")
                .status(OrderStatusEnum.PAYED.getName())
                .chargedBy(null)
                .entries(new HashSet<>())
                .createdAt(Instant.now())
                .build();
        testOrderBffDTO = new OrderBffDTO();
        testOrderBffDTO.setId(1L);
        testOrderBffDTO.setStatus(OrderStatusEnum.PAYED.getName());
        testOrderBffDTO.setEntries(new ArrayList<>());
    }
    @Test
    void testFindPendingOrders_Success() {
        List<Order> orders = List.of(testOrder);
        Page<Order> ordersPage = new PageImpl<>(orders, pageable, 1);
        when(orderRepository.findAllByStatus(eq(OrderStatusEnum.PAYED.getName()), any(Pageable.class)))
                .thenReturn(ordersPage);
        when(orderMapper.toOrderEntry(testOrder)).thenReturn(testOrderBffDTO);
        Page<OrderBffDTO> result = orderService.findPendingOrders(pageable);
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(orderRepository, times(1)).findAllByStatus(eq(OrderStatusEnum.PAYED.getName()), any(Pageable.class));
    }
    @Test
    void testFindPendingOrders_Empty() {
        Page<Order> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(orderRepository.findAllByStatus(eq(OrderStatusEnum.PAYED.getName()), any(Pageable.class)))
                .thenReturn(emptyPage);
        Page<OrderBffDTO> result = orderService.findPendingOrders(pageable);
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
    @Test
    void testFindById_Success() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderMapper.toOrderEntry(testOrder)).thenReturn(testOrderBffDTO);
        OrderBffDTO result = orderService.findById(100L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository, times(1)).findByExternalId(100L);
        verify(orderMapper, times(1)).toOrderEntry(testOrder);
    }
    @Test
    void testFindById_NotFound() {
        when(orderRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.findById(999L));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testCreateOrder_Success() {
        OrderDTO orderDTO = OrderDTO.builder().entries(new ArrayList<>()).build();
        when(jwtUtilService.getUserIdentifier()).thenReturn("testUser");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.createOrder(100L, orderDTO);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(100L, capturedOrder.getExternalId());
        assertEquals("testUser", capturedOrder.getPlacedBy());
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testTakeCharge_Success() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(jwtUtilService.getUserIdentifier()).thenReturn("chargedBy");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.takeCharge(100L);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.CHARGED.getName(), capturedOrder.getStatus());
        assertEquals("chargedBy", capturedOrder.getChargedBy());
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testTakeCharge_NotFound() {
        when(orderRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.takeCharge(999L));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testSaveStatusByExternalId_Success() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.saveStatusByExternalId(100L, OrderStatusEnum.CHARGED);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.CHARGED.getName(), capturedOrder.getStatus());
    }
    @Test
    void testSaveStatusByExternalId_NotFound() {
        when(orderRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.saveStatusByExternalId(999L, OrderStatusEnum.CHARGED));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testDeleteOrder_Success() {
        OrderEntry entry = OrderEntry.builder().id(1L).order(testOrder)
                .addedIngredients(new HashSet<>()).removedIngredients(new HashSet<>()).build();
        testOrder.setEntries(Set.of(entry));
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        orderService.deleteOrder(100L);
        verify(orderRepository, times(1)).delete(testOrder);
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testDeleteOrder_NotFound() {
        when(orderRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.deleteOrder(999L));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testUpdateOrder_EmptyEntries() {
        OrderDTO orderDTO = OrderDTO.builder().entries(new ArrayList<>()).build();
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.updateOrder(100L, orderDTO);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.UNDER_EDITING.getName(), capturedOrder.getStatus());
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testUpdateOrder_NullEntries() {
        OrderDTO orderDTO = OrderDTO.builder().entries(null).build();
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.updateOrder(100L, orderDTO);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.UNDER_EDITING.getName(), capturedOrder.getStatus());
    }
    @Test
    void testFindById_WithDifferentExternalId() {
        Order order2 = Order.builder().id(2L).externalId(200L).placedBy("user2")
                .status(OrderStatusEnum.CHARGED.getName()).createdAt(Instant.now()).build();
        OrderBffDTO orderBffDTO2 = new OrderBffDTO();
        orderBffDTO2.setId(2L);
        orderBffDTO2.setStatus(OrderStatusEnum.CHARGED.getName());
        when(orderRepository.findByExternalId(200L)).thenReturn(Optional.of(order2));
        when(orderMapper.toOrderEntry(order2)).thenReturn(orderBffDTO2);
        OrderBffDTO result = orderService.findById(200L);
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(OrderStatusEnum.CHARGED.getName(), result.getStatus());
    }
    @Test
    void testSaveStatusByExternalId_ChangeToCharged() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.saveStatusByExternalId(100L, OrderStatusEnum.CHARGED);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.CHARGED.getName(), capturedOrder.getStatus());
    }
    @Test
    void testSaveStatusByExternalId_ChangeToCompleted() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.saveStatusByExternalId(100L, OrderStatusEnum.COMPLETED);
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());
        Order capturedOrder = orderCaptor.getValue();
        assertEquals(OrderStatusEnum.COMPLETED.getName(), capturedOrder.getStatus());
    }
    @Test
    void testDeleteOrder_WithEntries() {
        OrderEntryAddedIngredient addedIng = OrderEntryAddedIngredient.builder().build();
        OrderEntryRemovedIngredient removedIng = OrderEntryRemovedIngredient.builder().build();
        OrderEntry entry = OrderEntry.builder().id(1L).order(testOrder)
                .addedIngredients(Set.of(addedIng)).removedIngredients(Set.of(removedIng)).build();
        testOrder.setEntries(Set.of(entry));
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        orderService.deleteOrder(100L);
        verify(orderEntryAddedIngredientsRepository, times(1)).deleteAll(any());
        verify(orderEntryRemovedIngredientsRepository, times(1)).deleteAll(any());
        verify(orderEntryRepository, times(1)).deleteAll(any());
        verify(orderRepository, times(1)).delete(testOrder);
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testFindPendingOrders_VerifyPageable() {
        Pageable customPageable = PageRequest.of(2, 5);
        List<Order> orders = List.of(testOrder);
        Page<Order> ordersPage = new PageImpl<>(orders, customPageable, 15);
        when(orderRepository.findAllByStatus(eq(OrderStatusEnum.PAYED.getName()), any(Pageable.class)))
                .thenReturn(ordersPage);
        when(orderMapper.toOrderEntry(testOrder)).thenReturn(testOrderBffDTO);
        Page<OrderBffDTO> result = orderService.findPendingOrders(customPageable);
        assertEquals(15, result.getTotalElements());
        assertEquals(5, result.getSize());
    }
    @Test
    void testCreateOrderWithPizzaAndIngredients() {
        OrderEntryDTO entryDTO = OrderEntryDTO.builder().idPizza(1L).idBase(1L)
                .addedIngredients(Set.of(10L, 11L)).removedIngredients(Set.of(20L)).build();
        OrderDTO orderDTO = OrderDTO.builder().entries(List.of(entryDTO)).build();
        when(jwtUtilService.getUserIdentifier()).thenReturn("testUser");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        PizzaDTO pizzaDTO = new PizzaDTO();
        pizzaDTO.setId(1L);
        pizzaDTO.setName("Margherita");
        BaseDTO baseDTO = new BaseDTO(1L, "Base");
        pizzaDTO.setBase(baseDTO);
        when(anagService.findById(1L)).thenReturn(pizzaDTO);
        when(anagService.findBaseById(1L)).thenReturn(baseDTO);
        Basis basis = Basis.builder().id(1L).name("Base").build();
        when(basisRepository.findById(1L)).thenReturn(Optional.of(basis));
        ComposedPizza pizza = ComposedPizza.builder().id(1L).name("Margherita").base(basis).build();
        when(composedPizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(1L);
        ingredientDTO.setName("Ingredient");
        when(anagService.findIngredientById(anyLong())).thenReturn(ingredientDTO);
        Ingredient ingredient = Ingredient.builder().id(1L).name("Ingredient").build();
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(ingredient));
        OrderEntry orderEntry = OrderEntry.builder().id(1L).order(testOrder).build();
        when(orderEntryRepository.save(any(OrderEntry.class))).thenReturn(orderEntry);
        orderService.createOrder(100L, orderDTO);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderEntryRepository, times(1)).save(any(OrderEntry.class));
        verify(orderEntryAddedIngredientsRepository, times(2)).save(any(OrderEntryAddedIngredient.class));
        verify(orderEntryRemovedIngredientsRepository, times(1)).save(any(OrderEntryRemovedIngredient.class));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testTakeChargeMultipleTimes() {
        when(orderRepository.findByExternalId(100L)).thenReturn(Optional.of(testOrder));
        when(jwtUtilService.getUserIdentifier()).thenReturn("charger");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        orderService.takeCharge(100L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(queueService, times(1)).sendMessage(eq("orderOrchestrationService-out-0"), any(SendingEventDTO.class));
    }
    @Test
    void testCreateOrderWithBase() {
        OrderEntryDTO entryDTO = OrderEntryDTO.builder().idBase(1L).addedIngredients(Set.of(10L)).build();
        OrderDTO orderDTO = OrderDTO.builder().entries(List.of(entryDTO)).build();
        when(jwtUtilService.getUserIdentifier()).thenReturn("testUser");
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
        BaseDTO baseDTO = new BaseDTO(1L, "Base");
        when(anagService.findBaseById(1L)).thenReturn(baseDTO);
        Basis basis = Basis.builder().id(1L).name("Base").build();
        when(basisRepository.findById(1L)).thenReturn(Optional.of(basis));
        IngredientDTO ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(10L);
        ingredientDTO.setName("Ingredient");
        when(anagService.findIngredientById(10L)).thenReturn(ingredientDTO);
        Ingredient ingredient = Ingredient.builder().id(10L).name("Ingredient").build();
        when(ingredientRepository.findById(10L)).thenReturn(Optional.of(ingredient));
        OrderEntry orderEntry = OrderEntry.builder().id(1L).order(testOrder).build();
        when(orderEntryRepository.save(any(OrderEntry.class))).thenReturn(orderEntry);
        orderService.createOrder(100L, orderDTO);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderEntryRepository, times(1)).save(any(OrderEntry.class));
        verify(queueService, times(1)).sendMessage(anyString(), any(SendingEventDTO.class));
    }
    @Test
    void testFindById_SendsErrorEvent() {
        when(orderRepository.findByExternalId(999L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> orderService.findById(999L));
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("not found"));
        verify(queueService, times(1)).sendMessage(eq("orderOrchestrationServiceError-out-0"), any(SendingEventDTO.class));
    }
}
