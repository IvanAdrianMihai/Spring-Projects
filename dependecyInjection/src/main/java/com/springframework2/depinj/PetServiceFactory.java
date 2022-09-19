package com.springframework2.depinj;

public class PetServiceFactory {
    public PetService getPetService(String type) {
        switch (type) {
            case "cat":
                return new CatPetService();
            default:
                return new DogPetService();
        }
    }
}
