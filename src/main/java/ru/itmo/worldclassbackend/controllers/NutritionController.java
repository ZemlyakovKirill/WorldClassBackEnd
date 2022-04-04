package ru.itmo.worldclassbackend.controllers;

import com.google.gson.JsonElement;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.worldclassbackend.entities.Nutrition;
import ru.itmo.worldclassbackend.entities.NutritionImage;
import ru.itmo.worldclassbackend.entities.User;
import ru.itmo.worldclassbackend.utils.AbstractController;
import ru.itmo.worldclassbackend.utils.NutritionType;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json")
@Api(tags = "Эндпоинты питания")
@Validated
public class NutritionController extends AbstractController {

    @PostMapping("/user/nutrition/add")
    public ResponseEntity<String> nutritionAdd(@Valid @RequestParam("type") NutritionType type,
                                               @Valid @RequestParam("day") DayOfWeek dayOfWeek,
                                               @Valid @RequestParam("weight") Integer weight,
                                               @Valid @RequestParam("name") String name,
                                               @Valid @RequestParam("calories") Integer calories) {
        Nutrition nutrition = new Nutrition(
                name,
                calories,
                weight,
                dayOfWeek,
                type
        );
        nutritionRepository.save(nutrition);
        return responseCreated(null);
    }

    @GetMapping("/user/nutrition/all")
    public ResponseEntity<String> getAllNutritions(
            @RequestParam("type") NutritionType nutritionType,
            @RequestParam("day") DayOfWeek dayOfWeek) {
        List<Map<String, Object>> response = nutritionRepository.getAllByTypeAndDay(dayOfWeek, nutritionType);
        return responseSuccess(response);
    }

    @PutMapping("/user/nutrition/edit/{id}")
    public ResponseEntity<String> editNutrition(@PathVariable("id") Long id,
                                                @Valid @RequestParam("type") NutritionType type,
                                                @Valid @RequestParam("day") DayOfWeek dayOfWeek,
                                                @Valid @RequestParam("weight") Integer weight,
                                                @Valid @RequestParam("name") String name,
                                                @Valid @RequestParam("calories") Integer calories) {
        Optional<Nutrition> nutritionOptional = nutritionRepository.findById(id);
        if (nutritionOptional.isPresent()) {
            Nutrition nutrition = nutritionOptional.get();
            nutrition.setName(name);
            nutrition.setNutritionType(type);
            nutrition.setCalories(calories);
            nutrition.setDayOfWeek(dayOfWeek);
            nutrition.setWeight(weight);
            nutritionRepository.save(nutrition);
            return responseSuccess("Обновлено");
        } else {
            return responseBad("Элемент питания не найден");
        }
    }

    @GetMapping("/user/nutrition/get/{id}")
    public ResponseEntity<String> getNutrition(
            Principal principal,
            @PathVariable("id") Long nutritionId) {
        User user = userService.findByEmail(principal.getName());
        Optional<Nutrition> optionalNutrition = nutritionRepository.findById(nutritionId);
        if (optionalNutrition.isPresent()) {
            Optional<Nutrition> optionalFavouriteNutrition = user.getFavouriteNutritions().stream().filter(e -> e.getId().equals(nutritionId)).findFirst();
            JsonElement jsonElement = json.toJsonTree(optionalNutrition.get());
            jsonElement.getAsJsonObject().addProperty("is_favourite", optionalFavouriteNutrition.isPresent());
            return responseSuccess(jsonElement);
        } else {
            return responseBad("Элемент питания не найден");
        }
    }

    @Transactional
    @DeleteMapping("/user/nutrition/delete/{id}")
    public ResponseEntity<String> deleteNutrition(@PathVariable("id") Long id) {
        Optional<Nutrition> nutritionOptional = nutritionRepository.findById(id);
        if (nutritionOptional.isPresent()) {
            Nutrition nutrition = nutritionOptional.get();
            nutritionRepository.delete(nutrition);
            return responseSuccess(nutrition);
        } else {
            return responseBad("Удалено");
        }
    }

    @PostMapping("/user/nutrition/image/add")
    public ResponseEntity<String> imageAdd(@RequestParam("nutrition") Long nutritionId,
                                           @RequestParam("image") MultipartFile file) throws IOException, SQLException {
        Optional<Nutrition> optionalNutrition = nutritionRepository.findById(nutritionId);
        if (optionalNutrition.isPresent()) {
            if (!file.isEmpty()) {
                if (nutritionImageRepository.findByImageName(file.getOriginalFilename()) != null) {
                    return responseBad("Image with that name already exists");
                }
                if (file.getOriginalFilename() == null) {
                    return responseBad("Неверное название файла");
                }
                Nutrition nutrition = optionalNutrition.get();
                byte[] bytes = file.getBytes();
                NutritionImage nutritionImage = new NutritionImage(
                        Base64.getEncoder().encode(bytes),
                        file.getOriginalFilename(),
                        nutrition
                );
                nutritionImageRepository.save(nutritionImage);
                return responseCreated(null);
            } else {
                return responseBad("Файл пуст");
            }
        } else {
            return responseBad("Элемент питания не найден");
        }
    }

    @GetMapping("/user/nutrition/imageCount/get")
    public ResponseEntity<String> getNutrittionImageCount(@RequestParam("nutrition") Long nutritionId) {
        Optional<Nutrition> optionalNutrition = nutritionRepository.findById(nutritionId);
        if (optionalNutrition.isPresent()) {
            Nutrition nutrition = optionalNutrition.get();
            return responseSuccess(nutrition.getImages().size());
        } else {
            return responseBad("Элемент питания не найден");
        }
    }

    @GetMapping("/user/nutrition/image/get/{ordinal}")
    public ResponseEntity<String> getImage(@PathVariable("ordinal") int ordinal,
                                           @RequestParam("nutrition") Long nutritionId) {
        if (ordinal < 1) {
            return responseBad("Invalid ordinal");
        }
        Optional<Nutrition> optionalNutrition = nutritionRepository.findById(nutritionId);
        if (optionalNutrition.isPresent()) {
            Nutrition nutrition = optionalNutrition.get();
            List<NutritionImage> images = nutrition.getImages().stream().sorted(Comparator.comparing(NutritionImage::getId)).collect(Collectors.toList());
            NutritionImage nutritionImage = images.get(ordinal - 1);
            String response = new String(nutritionImage.getImage(), StandardCharsets.UTF_8);
            return responseSuccess(response);
        } else {
            return responseBad("Элемент питания не найден");
        }
    }

}
