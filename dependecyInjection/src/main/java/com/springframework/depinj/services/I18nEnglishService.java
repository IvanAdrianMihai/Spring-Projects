package com.springframework.depinj.services;

import com.springframework.depinj.repositories.EnglishGreetingRepository;
import com.springframework.depinj.services.GreetingService;

public class I18nEnglishService implements GreetingService {
    private final EnglishGreetingRepository englishGreetingRepository;

    public I18nEnglishService(EnglishGreetingRepository englishGreetingRepository) {
        this.englishGreetingRepository = englishGreetingRepository;
    }

    @Override
    public String sayGreeting() {
        return englishGreetingRepository.getGreeting();
    }
}
