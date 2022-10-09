package com.springframework.spring5recipeapp.bootstrap;

import com.springframework.spring5recipeapp.domain.*;
import com.springframework.spring5recipeapp.repositories.CategoryRepository;
import com.springframework.spring5recipeapp.repositories.RecipeRepository;
import com.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class RecipeBootstrap implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    private List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        Category category = new Category();
        category.setId(1l);
        category.setDescription("Meal");
        category = categoryRepository.save(category);

        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.setId(1l);
        unitOfMeasure.setDescription("Cup");
        unitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(1l);
        ingredient.setDescription("Sugar");
        ingredient.setAmount(new BigDecimal(5));
        ingredient.setUom(unitOfMeasure);

        Difficulty difficulty = Difficulty.EASY;

        Recipe recipe = new Recipe();
        recipe.setId(1l);
        recipe.setDescription("good for you");
        recipe.setCategories(Set.of(category));
        recipe.setDifficulty(difficulty);
        recipe.setDirections("None");
        recipe.setCookTime(5);
        recipe.setIngredients(Set.of(ingredient));
        recipe.setPrepTime(12);
        recipe.setServings(4);
        recipe.setUrl("My recipe URL");
        recipe.setSource("My Book");

        ingredient.setRecipe(recipe);
        category.setRecipes(Set.of(recipe));

        recipes.add(recipe);
        return recipes;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        recipeRepository.save(getRecipes().get(0));
        log.debug("Loading Bootstrap Data");
    }
}
