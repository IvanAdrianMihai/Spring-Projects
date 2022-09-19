package com.springframework.depinj.services;

import com.springframework.depinj.services.GreetingService;

public class I18nSpanishService implements GreetingService {
    @Override
    public String sayGreeting() {
        return "Hola Mundo - ES";
    }
}
