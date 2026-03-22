package it.adesso.management.ordermanagementservice.services.impl;

import it.adesso.management.ordermanagementservice.DTOs.BFF.OrderBffDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderDTO;
import it.adesso.management.ordermanagementservice.DTOs.OrderEntryDTO;
import it.adesso.management.ordermanagementservice.DTOs.SendingEventDTO;
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
import it.adesso.management.ordermanagementservice.services.OrderService;
import it.adesso.management.ordermanagementservice.services.QueueService;
import it.adesso.management.ordermanagementservice.services.external.AnagService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderBFFMapper orderMapper;
    private final QueueService queueService;
    private final JwtUtilService jwtUtilService;
    private final OrderRepository orderRepository;
    private final ComposedPizzaRepository composedPizzaRepository;
    private final BasisRepository basisRepository;
    private final IngredientRepository ingredientRepository;
    private final AnagService anagService;
    private final OrderEntryRepository orderEntryRepository;
    private final OrderEntryAddedIngredientsRepository orderEntryAddedIngredientsRepository;
    private final OrderEntryRemovedIngredientsRepository orderEntryRemovedIngredientsRepository;

    @Override
    public Page<OrderBffDTO> findPendingOrders(Pageable pageable) {
        // Forza l'ordinamento per data di creazione decrescente, mantenendo paginazione e sort del client
        Pageable pageableWithSort = PageRequest.of(
            pageable.getPageNumber(), 
            pageable.getPageSize(), 
            Sort.by(Sort.Direction.ASC, "createdAt")
        );
        
        Page<Order> availableOrders = this.orderRepository.findAllByStatus(OrderStatusEnum.PAYED.getName(), pageableWithSort);
        List<OrderBffDTO> orderDTOs = new ArrayList<>();
        for(Order order : availableOrders) {
            OrderBffDTO orderDTO = this.orderMapper.toOrderEntry(order);
            orderDTOs.add(orderDTO);
        }
        return new PageImpl<>(orderDTOs, pageable, availableOrders.getTotalElements());
    }

    @Override
    public OrderBffDTO findById(Long externalId) {
        Order order = this.orderRepository.findByExternalId(externalId).orElseThrow(
            () -> new NotFoundException("Order not found with id: " + externalId)
        );
        return this.orderMapper.toOrderEntry(order);
    }


    @Transactional
    @Override
    public void createOrder(Long extenalId, OrderDTO orderDTO) {

        Order order = Order.builder()
                .externalId(extenalId)
                .placedBy(this.jwtUtilService.getUserIdentifier())
                .build();
        this.orderRepository.save(order);

        //ora bisogna salvare tutte le entry dell'ordine
        for (OrderEntryDTO entry: orderDTO.getEntries()) {
            OrderEntry orderEntry = OrderEntry.builder().order(order).build();
            if (entry.getIdPizza() != null) {
                orderEntry.setIdPizza(this.findPizzaById(entry.getIdPizza()));
            }
            if (entry.getIdBase() != null) {
                orderEntry.setBase(this.findBaseById(entry.getIdBase()));
            }

            this.orderEntryRepository.save(orderEntry);

            if (entry.getAddedIngredients() != null && !entry.getAddedIngredients().isEmpty()) {
                this.persistAddedIngredients(entry.getAddedIngredients(), orderEntry);
            }
            if (entry.getRemovedIngredients() != null && !entry.getRemovedIngredients().isEmpty()) {
                this.persistRemovedIngredients(entry.getRemovedIngredients(), orderEntry);
            }
        }

        this.queueService.sendMessage(
            "orderOrchestrationService-out-0",
            SendingEventDTO.builder()
                .orderId(extenalId)
                .event(OrderSendingEventEnum.CREATED).build()
        );
    }

    @Transactional
    @Override
    public void updateOrder(Long externalId, OrderDTO orderDTO) {
        Order order = this.orderRepository.findByExternalId(externalId).orElseThrow(
            () -> new NotFoundException("Order not found")
        );
        order.setStatus(OrderStatusEnum.UNDER_EDITING.getName());
        this.orderRepository.save(order);
        if (orderDTO.getEntries() != null && !orderDTO.getEntries().isEmpty()) {
            for (OrderEntryDTO entryDto: orderDTO.getEntries()) {
                // Se entryDto ha un ID, aggiorna l'entry esistente, altrimenti creane una nuova
                OrderEntry orderEntry;
                if (entryDto.getId() != null) {
                    orderEntry = this.orderEntryRepository.findById(entryDto.getId()).orElseThrow(
                        () -> new NotFoundException("OrderEntry not found with id: " + entryDto.getId())
                    );
                } else {
                    // Crea una nuova entry se l'ID non è fornito
                    orderEntry = OrderEntry.builder().order(order).build();
                }
                
                // Aggiorna la pizza se fornita
                if (entryDto.getIdPizza() != null) {
                    orderEntry.setIdPizza(this.findPizzaById(entryDto.getIdPizza()));
                } else {
                    orderEntry.setIdPizza(null);
                }
                
                // Aggiorna la base se fornita
                if (entryDto.getIdBase() != null) {
                    orderEntry.setBase(this.findBaseById(entryDto.getIdBase()));
                } else {
                    orderEntry.setBase(null);
                }
                
                // Salva l'entry aggiornata/nuova
                this.orderEntryRepository.save(orderEntry);
                
                // Gestisci gli ingredienti aggiunti: cancella quelli vecchi e ricrea quelli nuovi
                if (orderEntry.getAddedIngredients() != null && !orderEntry.getAddedIngredients().isEmpty()) {
                    this.orderEntryAddedIngredientsRepository.deleteAll(orderEntry.getAddedIngredients());
                    this.persistAddedIngredients(entryDto.getAddedIngredients(), orderEntry);
                }
                
                // Gestisci gli ingredienti rimossi: cancella quelli vecchi e ricrea quelli nuovi
                if (orderEntry.getRemovedIngredients() != null && !orderEntry.getRemovedIngredients().isEmpty()) {
                    this.orderEntryRemovedIngredientsRepository.deleteAll(orderEntry.getRemovedIngredients());
                    this.persistRemovedIngredients(entryDto.getRemovedIngredients(), orderEntry);
                }
            }
        }

        this.queueService.sendMessage(
            "orderOrchestrationService-out-0",
            SendingEventDTO.builder()
                .orderId(externalId)
                .event(OrderSendingEventEnum.EDITED).build()
        );
    }

    @Transactional
    @Override
    public void deleteOrder(Long externalId) {
        Order order = this.orderRepository.findByExternalId(externalId).orElseThrow(
            () -> new NotFoundException("Order not found with external id: " + externalId)
        );
        
        // Elimina tutti gli ingredienti associati alle entries
        if (order.getEntries() != null && !order.getEntries().isEmpty()) {
            for (OrderEntry entry : order.getEntries()) {
                // Elimina gli ingredienti aggiunti
                if (entry.getAddedIngredients() != null && !entry.getAddedIngredients().isEmpty()) {
                    this.orderEntryAddedIngredientsRepository.deleteAll(entry.getAddedIngredients());
                }
                // Elimina gli ingredienti rimossi
                if (entry.getRemovedIngredients() != null && !entry.getRemovedIngredients().isEmpty()) {
                    this.orderEntryRemovedIngredientsRepository.deleteAll(entry.getRemovedIngredients());
                }
            }
            // Elimina tutte le entries dell'ordine
            this.orderEntryRepository.deleteAll(order.getEntries());
        }
        
        // Elimina l'ordine stesso
        this.orderRepository.delete(order);
        
        // Invia evento di eliminazione alla coda
        this.queueService.sendMessage(
            "orderOrchestrationService-out-0",
            SendingEventDTO.builder()
                .orderId(externalId)
                .event(OrderSendingEventEnum.DELETED).build()
        );
    }

    @Override
    public void takeCharge(Long externalId) {
        Order order = this.orderRepository.findByExternalId(externalId).orElseThrow(
            () -> new NotFoundException("Order not found with external id: " + externalId)
        );
        order.setStatus(OrderStatusEnum.CHARGED.getName());
        order.setChargedBy(this.jwtUtilService.getUserIdentifier());
        this.orderRepository.save(order);
         this.queueService.sendMessage(
            "orderOrchestrationService-out-0",
            SendingEventDTO.builder()
                .orderId(externalId)
                .event(OrderSendingEventEnum.CHARGED).build()
        );
    }

    public void saveStatusByExternalId(Long externalId, OrderStatusEnum status) {
        Order order = this.orderRepository.findByExternalId(externalId).orElseThrow(
            () -> new NotFoundException("Order not found with external id: " + externalId)
        );
        order.setStatus(status.getName());
        this.orderRepository.save(order);
    }
    private ComposedPizza findPizzaById(Long pizzaId) {
        // vuol dire che ha scelto una pizza già composta
        PizzaDTO pizza;
        pizza = this.anagService.findById(pizzaId);
        ComposedPizza composedPizza = composedPizzaRepository.findById(pizzaId).orElse(null);
        // se ancora nessuno l'aveva ordinata la inseriamo compresa di base
        if (composedPizza == null) {
            Basis base = this.findBaseById(pizza.getBaseDTO().getId());
            composedPizza = ComposedPizza.builder()
                    .id(pizza.getId())
                    .name(pizza.getName()).base(base).build();
            this.composedPizzaRepository.save(composedPizza);
        }
        return composedPizza;
    }
    private Basis findBaseById(Long idBase) {
        BaseDTO base = this.anagService.findBaseById(idBase);
        Basis baseEntity = this.basisRepository.findById(base.getId()).orElse(null);
        if (baseEntity == null) {
            baseEntity = Basis.builder().id(base.getId()).name(base.getName()).build();
            this.basisRepository.save(baseEntity);
        }
        return baseEntity;
    }
    private void persistAddedIngredients(Set<Long> ids, OrderEntry orderEntry) {
        for (Long addedIngredientId : ids) {
            Ingredient ingredientEntity = this.findIngredientById(addedIngredientId);
            OrderEntryAddedIngredientId orderEntryAddedIngredientId = OrderEntryAddedIngredientId.builder()
                    .ingredient(addedIngredientId)
                    .orderEntry(orderEntry.getId()).build();
            OrderEntryAddedIngredient orderEntryAddedIngredient = OrderEntryAddedIngredient.builder()
                    .id(orderEntryAddedIngredientId)
                    .ingredient(ingredientEntity)
                    .orderEntry(orderEntry)
                    .build();
            this.orderEntryAddedIngredientsRepository.save(orderEntryAddedIngredient);
        }
    }
    private void persistRemovedIngredients(Set<Long> ids, OrderEntry orderEntry) {
        for (Long removedIngredientId : ids) {
            Ingredient ingredientEntity = this.findIngredientById(removedIngredientId);
             OrderEntryRemovedIngredientId orderEntryRemovedIngredientId = OrderEntryRemovedIngredientId.builder()
                    .ingredient(removedIngredientId)
                    .orderEntry(orderEntry.getId()).build();
            OrderEntryRemovedIngredient orderEntryRemovedIngredient = OrderEntryRemovedIngredient.builder()
                    .id(orderEntryRemovedIngredientId)
                    .ingredient(ingredientEntity)
                    .orderEntry(orderEntry)
                    .build();
            this.orderEntryRemovedIngredientsRepository.save(orderEntryRemovedIngredient);
        }
    }
    private Ingredient findIngredientById(Long id) throws NotFoundException {
        IngredientDTO ingredient = this.anagService.findIngredientById(id);
        Ingredient ingredientEntity = this.ingredientRepository.findById(ingredient.getId()).orElse(null);
        if (ingredientEntity == null) {
            ingredientEntity = Ingredient.builder().id(id).name(ingredient.getName()).build();
            this.ingredientRepository.save(ingredientEntity);
        }
        return ingredientEntity;
    }
}
