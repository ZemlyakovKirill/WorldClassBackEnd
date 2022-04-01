package ru.itmo.worldclassbackend.controllers;

import com.google.gson.JsonElement;
import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.itmo.worldclassbackend.entities.Exercise;
import ru.itmo.worldclassbackend.entities.ExerciseCompilation;
import ru.itmo.worldclassbackend.entities.ExerciseImage;
import ru.itmo.worldclassbackend.entities.User;
import ru.itmo.worldclassbackend.utils.AbstractController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;

@RestController
@Api(tags = "Эндпоинты упражений")
@RequestMapping(produces = "application/json")
@Validated
public class ExerciseController extends AbstractController {

    @PostMapping("/user/exercise/compilation/add")
    public ResponseEntity<String> compilationAdd(Principal principal, @Valid @RequestParam("name") String name){
        ExerciseCompilation compilation=new ExerciseCompilation(name);
        exerciseCompilationRepository.save(compilation);
        return responseCreated(null);
    }

    @GetMapping("/user/exercise/compilation/get")
    public ResponseEntity<String> getAllCompilations(){
        List<ExerciseCompilation> all = exerciseCompilationRepository.findAll();
        return responseSuccess(all);
    }

    @PutMapping("/user/exercise/compilation/edit/{id}")
    public ResponseEntity<String> editCompilation(@PathVariable("id") Long id,
                                                  @Valid @RequestParam("name") String name){
        Optional<ExerciseCompilation> exerciseCompilation = exerciseCompilationRepository.findById(id);
        if(exerciseCompilation.isPresent()){
            ExerciseCompilation compilation = exerciseCompilation.get();
            compilation.setName(name);
            exerciseCompilationRepository.save(compilation);
            return responseSuccess("Edited");
        }else{
            return responseBad("Exercise compilation was not found");
        }
    }

    @Transactional
    @DeleteMapping("/user/exercise/compilation/delete/{id}")
    public ResponseEntity<String> deleteCompilation(@PathVariable("id") Long id){
        Optional<ExerciseCompilation> exerciseCompilation = exerciseCompilationRepository.findById(id);
        if(exerciseCompilation.isPresent()){
            ExerciseCompilation compilation = exerciseCompilation.get();
            exerciseCompilationRepository.delete(compilation);
            return responseSuccess("Deleted");
        }else{
            return responseBad("Exercise compilation was not found");
        }
    }

    @PostMapping("/user/exercise/add")
    public ResponseEntity<String> exerciseAdd(Principal principal,
                                               @RequestParam("compilation") Long compilationId,
                                               @Valid @RequestParam("name") String name,
                                               @Valid @RequestParam("advice") String advice){
        Optional<ExerciseCompilation> optionalCompilation = exerciseCompilationRepository.findById(compilationId);
        if(optionalCompilation.isPresent()){
            ExerciseCompilation exerciseCompilation = optionalCompilation.get();
            Exercise exercise=new Exercise(
                    name,
                    advice,
                    exerciseCompilation
            );
            exerciseRepository.save(exercise);
            return responseCreated(null);
        }else{
            return responseBad("Exercise compilation was not found");
        }
    }

    @GetMapping("/user/exercise/all")
    public ResponseEntity<String> getAllExercises(Principal principal,@RequestParam("compilation") Long compilationId){
        User user = userService.findByEmail(principal.getName());
        Optional<ExerciseCompilation> compilationOptional = exerciseCompilationRepository.findById(compilationId);
        if(compilationOptional.isPresent()){
            List<Map<String, Object>> response = exerciseRepository.getAllByCompilation(compilationOptional.get());
            return responseSuccess(response);
        }else{
            return responseBad("Nutrition compilation was not found");
        }
    }

    @GetMapping("/user/exercise/get/{id}")
    public ResponseEntity<String> getExercise(
            Principal principal,
            @PathVariable("id") Long exerciseId){
        User user=userService.findByEmail(principal.getName());
        Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);
        if(optionalExercise.isPresent()){
            Optional<Exercise> optionalFavouriteExercise = user.getFavouriteExercises().stream().filter(e -> e.getId().equals(exerciseId)).findFirst();
            JsonElement jsonElement=json.toJsonTree(optionalExercise.get());
            jsonElement.getAsJsonObject().addProperty("is_favourite", optionalFavouriteExercise.isPresent());
            return responseSuccess(jsonElement);
        }else{
            return responseBad("Exercise was not found");
        }
    }


    @PutMapping("/user/exercise/edit/{id}")
    public ResponseEntity<String> editExercise(@PathVariable("id") Long id,
                                                @RequestParam("compilation") Long compilationId,
                                                @Valid @RequestParam("name") String name,
                                                @Valid @RequestParam("advice") String advice){
        Optional<ExerciseCompilation> exerciseCompilation = exerciseCompilationRepository.findById(compilationId);
        if(exerciseCompilation.isPresent()){
            ExerciseCompilation compilation = exerciseCompilation.get();
            Optional<Exercise> exerciseOptional = exerciseRepository.findById(id);
            if(exerciseOptional.isPresent()){
                Exercise exercise = exerciseOptional.get();
                exercise.setExerciseCompilation(compilation);
                exercise.setName(name);
                exercise.setAdvice(advice);
                exerciseRepository.save(exercise);
                return responseSuccess("Edited");
            }else{
                return responseBad("Exercise was not found");
            }
        }else{
            return responseBad("Exercise compilation was not found");
        }
    }



    @Transactional
    @DeleteMapping("/user/exercise/delete/{id}")
    public ResponseEntity<String> deleteExercise(@PathVariable("id") Long id){
        Optional<Exercise> exerciseOptional= exerciseRepository.findById(id);
        if(exerciseOptional.isPresent()){
            Exercise exercise = exerciseOptional.get();
            exerciseRepository.delete(exercise);
            return responseSuccess("Deleted");
        }else{
            return responseBad("Exercise was not found");
        }
    }

    @PostMapping(value = "/user/exercise/image/add",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> imageAdd(@RequestParam("exercise") Long exerciseId,
                                           @RequestParam("image") MultipartFile file) throws IOException, SQLException {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);
        if(optionalExercise.isPresent()) {
            if(!file.isEmpty()){
                if(exerciseImageRepository.findByImageName(file.getOriginalFilename())!=null){
                    return responseBad("Image with that name already exists");
                }
                if(file.getOriginalFilename()==null){
                    return responseBad("Bad image name");
                }
                Exercise exercise = optionalExercise.get();
                byte[] bytes = file.getBytes();
                ExerciseImage exerciseImage=new ExerciseImage(
                        Base64.getEncoder().encode(bytes),
                        file.getOriginalFilename(),
                        exercise
                );
                exerciseImageRepository.save(exerciseImage);
                return responseCreated(null);
            }else{
                return responseBad("Image is empty");
            }
        }else{
            return responseBad("Exercise was not found");
        }
    }

    @GetMapping("/user/exercise/image/all/{id}")
    public ResponseEntity<String> getAllImages(@PathVariable("id") Long exerciseId){
        Optional<Exercise> optionalExercise = exerciseRepository.findById(exerciseId);
        if(optionalExercise.isPresent()){
            Exercise exercise = optionalExercise.get();
            List<ExerciseImage> images = exercise.getImages();
            return responseSuccess(images);
        }else{
            return responseBad("Exercise was not found");
        }
    }
}
