package ru.itmo.worldclassbackend.controllers;

import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.worldclassbackend.entities.Exercise;
import ru.itmo.worldclassbackend.entities.Nutrition;
import ru.itmo.worldclassbackend.entities.User;
import ru.itmo.worldclassbackend.utils.AbstractController;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Api(tags = "Эндпоинты избранных")
@RestController
public class FavouritesController extends AbstractController {

    @GetMapping("/user/exercise/favourite/all")
    public ResponseEntity<String> getFavouritesExercises(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Map<String, Object>> favouriteExercises = userService.getAllFavouriteExercises(user);
        return responseSuccess(favouriteExercises);
    }

    @PostMapping("/user/exercise/favourite/add")
    public ResponseEntity<String> addFavouriteExercise(Principal principal, @RequestParam("exercise_id") Long exerciseId) {
        User user = userService.findByEmail(principal.getName());
        Set<Exercise> favouriteExercises = user.getFavouriteExercises();
        Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);
        if (optionalExercise.isPresent()) {
            favouriteExercises.add(optionalExercise.get());
            userService.save(user);
            return responseCreated(null);
        } else {
            return responseBad("Exercise was not found");
        }
    }

    @Transactional
    @DeleteMapping("/user/exercise/favourite/delete")
    public ResponseEntity<String> deleteFavouriteExercise(Principal principal, @RequestParam("exercise_id") Long exerciseId) {
        User user = userService.findByEmail(principal.getName());
        Set<Exercise> favouriteExercises = user.getFavouriteExercises();
        Optional<Exercise> optionalExercise = favouriteExercises.stream().filter(e -> e.getId().equals(exerciseId)).findFirst();
        if(optionalExercise.isPresent()){
            favouriteExercises.removeIf(e -> e.getId().equals(exerciseId));
            userService.save(user);
            return responseSuccess("Deleted");
        }else{
            return responseBad("Exercise was not found");
        }
    }

    @GetMapping("/user/nutrition/favourite/all")
    public ResponseEntity<String> getFavouritesNutritions(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<Map<String, Object>> favouriteNutritions = userService.getAllFavouriteNutritions(user);
        return responseSuccess(favouriteNutritions);
    }

    @PostMapping("/user/nutrition/favourite/add")
    public ResponseEntity<String> addFavouriteNutrition(Principal principal, @RequestParam("nutrition_id") Long nutritionId) {
        User user = userService.findByEmail(principal.getName());
        Set<Nutrition> favouriteNutritions = user.getFavouriteNutritions();
        Optional<Nutrition> optionalNutrition = nutritionRepository.findById(nutritionId);
        if (optionalNutrition.isPresent()) {
            favouriteNutritions.add(optionalNutrition.get());
            userService.save(user);
            return responseCreated(null);
        } else {
            return responseBad("Nutrition was not found");
        }
    }

    @Transactional
    @DeleteMapping("/user/nutrition/favourite/delete")
    public ResponseEntity<String> deleteFavouriteNutrition(Principal principal, @RequestParam("nutrition_id") Long nutritionId) {
        User user = userService.findByEmail(principal.getName());
        Set<Nutrition> favouriteNutritions = user.getFavouriteNutritions();
        Optional<Nutrition> optionalExercise = favouriteNutritions.stream().filter(e -> e.getId().equals(nutritionId)).findFirst();
        if(optionalExercise.isPresent()){
            favouriteNutritions.removeIf(e -> e.getId().equals(nutritionId));
            userService.save(user);
            return responseSuccess("Deleted");
        }else{
            return responseBad("Nutrition was not found");
        }
    }


}
