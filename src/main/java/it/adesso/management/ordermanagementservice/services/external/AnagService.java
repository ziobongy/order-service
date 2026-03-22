package it.adesso.management.ordermanagementservice.services.external;

import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.BaseDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.IngredientDTO;
import it.adesso.management.ordermanagementservice.DTOs.external.anagservice.PizzaDTO;
import it.adesso.management.ordermanagementservice.exceptions.NotFoundException;

public interface AnagService {
    PizzaDTO findById(Long id) throws NotFoundException;
    BaseDTO findBaseById(Long id) throws NotFoundException;
    IngredientDTO findIngredientById(Long id) throws NotFoundException;
}
