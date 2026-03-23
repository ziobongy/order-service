package it.adesso.management.ordermanagementservice.services.external.impl;

import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import it.adesso.management.ordermanagementservice.exceptions.NotFoundException;
import it.adesso.management.ordermanagementservice.services.external.AnagService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class AnagServiceImpl implements AnagService {
    @Value("${anag-service.base-path}")
    private String anagServiceBasePath;


    private final WebClient webClient;
    @Override
    public PizzaDTO findById(Long id) throws NotFoundException {
        return this.webClient.get()
            .uri(anagServiceBasePath + "/pizzas/{id}", id)
            .retrieve()
            .bodyToMono(PizzaDTO.class)
            .doOnError(
                throwable -> {
                    throw new NotFoundException("Pizza with id " + id + " not found");
                }
            )
            .block();
    }

    @Override
    public BaseDTO findBaseById(Long id) throws NotFoundException {
        return this.webClient.get()
            .uri(anagServiceBasePath + "/bases/{id}", id)
            .retrieve()
            .bodyToMono(BaseDTO.class)
            .doOnError(
                throwable -> {
                    throw new NotFoundException("Base with id " + id + " not found");
                }
            )
            .block();
    }

    @Override
    public IngredientDTO findIngredientById(Long id) throws NotFoundException {
        return this.webClient.get()
            .uri(anagServiceBasePath + "/ingredients/{id}", id)
            .retrieve()
            .bodyToMono(IngredientDTO.class)
            .doOnError(
                throwable -> {
                    throw new NotFoundException("Ingredient with id " + id + " not found");
                }
            )
            .block();
    }
}
