package com.springframework2.depinj;

public class CatPetService implements PetService {
    @Override
    public String getPetType() {
        return "Cats are the best!";
    }
}
