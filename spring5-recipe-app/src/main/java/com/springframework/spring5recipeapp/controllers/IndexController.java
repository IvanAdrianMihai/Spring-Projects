package com.springframework.spring5recipeapp.controllers;

import com.springframework.spring5recipeapp.domain.Category;
import com.springframework.spring5recipeapp.domain.UnitOfMeasure;
import com.springframework.spring5recipeapp.repositories.CategoryRepository;
import com.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import com.springframework.spring5recipeapp.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @RequestMapping({"", "/", "/index"})
    public String getIndexPage(Model model) {
        model.addAttribute("recipes", recipeService.getRecipes());

        return "index";
    }
}