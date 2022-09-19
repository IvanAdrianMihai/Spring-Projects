package com.springframework.depinj.controllers;

import com.springframework2.depinj.PetService;
import org.springframework.stereotype.Controller;

@Controller
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    public String witchPetIsTheBest() {
        return petService.getPetType();
    }
}
