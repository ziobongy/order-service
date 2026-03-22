package it.adesso.management.ordermanagementservice;

import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
import it.adesso.management.ordermanagementservice.enums.OrderSendingEventEnum;
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
import it.adesso.management.ordermanagementservice.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderManagementServiceApplicationTests {

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

    @Test
    void createOrderShouldSendCreatedEventToQueue() {
        // Arrange
        Long externalId = 10L;
        OrderDTO orderDTO = OrderDTO.builder().entries(new java.util.ArrayList<>()).build();
        when(jwtUtilService.getUserIdentifier()).thenReturn("testUser");
        when(orderRepository.save(any())).thenReturn(null);

        // Act
        orderService.createOrder(externalId, orderDTO);

        // Assert
        ArgumentCaptor<SendingEventDTO> eventCaptor = ArgumentCaptor.forClass(SendingEventDTO.class);
        verify(queueService).sendMessage(org.mockito.ArgumentMatchers.eq("orderOrchestrationService-out-0"), eventCaptor.capture());

        SendingEventDTO capturedEvent = eventCaptor.getValue();
        assertEquals(externalId, capturedEvent.getOrderId());
        assertEquals(OrderSendingEventEnum.CREATED, capturedEvent.getEvent());
    }
}
