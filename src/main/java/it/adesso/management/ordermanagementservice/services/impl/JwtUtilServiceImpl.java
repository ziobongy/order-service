package it.adesso.management.ordermanagementservice.services.impl;

import it.adesso.management.ordermanagementservice.services.JwtUtilService;
import org.springframework.stereotype.Service;

@Service
public class JwtUtilServiceImpl implements JwtUtilService {

    @Override
    public String getUserIdentifier() {
        return "QRT";
    }
}
