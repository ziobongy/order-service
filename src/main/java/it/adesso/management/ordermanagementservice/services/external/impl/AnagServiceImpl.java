package it.adesso.management.ordermanagementservice.services.external.impl;

import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import it.adesso.management.ordermanagementservice.exceptions.NotFoundException;
import it.adesso.management.ordermanagementservice.services.external.AnagService;
import org.springframework.stereotype.Service;

@Service
public class AnagServiceImpl implements AnagService {
    @Override
    public PizzaDTO findById(Long id) throws NotFoundException {
        return PizzaDTO.builder().id(id).name("Capricciosa").baseDTO(
            BaseDTO.builder().id(id + Math.round(Math.random() * 100 + 1)).name("Classica").build()
        ).build();
    }

    @Override
    public BaseDTO findBaseById(Long id) throws NotFoundException {
        return BaseDTO.builder().id(id).name("Classica").build();
    }

    @Override
    public IngredientDTO findIngredientById(Long id) throws NotFoundException {
        return IngredientDTO.builder().id(id).name("Mozzarella di Bufala").build();
    }
}
