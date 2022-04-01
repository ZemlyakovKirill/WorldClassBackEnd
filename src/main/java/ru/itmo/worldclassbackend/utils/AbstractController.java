package ru.itmo.worldclassbackend.utils;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.itmo.worldclassbackend.providers.JwtProvider;
import ru.itmo.worldclassbackend.repositories.*;
import ru.itmo.worldclassbackend.services.UserService;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public abstract class AbstractController {

    protected final static Gson json=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeHierarchyAdapter(byte[].class,
            new ByteArrayToBase64TypeAdapter()).create();

    private static class ByteArrayToBase64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
        public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return java.util.Base64.getDecoder().decode(json.getAsString());
        }

        public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(new String(src, StandardCharsets.UTF_8));
        }
    }

    @Autowired
    protected NutritionRepository nutritionRepository;

    @Autowired
    protected NutritionImageRepository nutritionImageRepository;

    @Autowired
    protected ExerciseCompilationRepository exerciseCompilationRepository;

    @Autowired
    protected ExerciseRepository exerciseRepository;

    @Autowired
    protected ExerciseImageRepository exerciseImageRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected JwtProvider jwtProvider;


    protected static ResponseEntity<String> response(Object objectToJson, HttpStatus status){
        Map<String,Object> responseMap=new HashMap<>();
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        responseMap.putIfAbsent("response",objectToJson);
        responseMap.putIfAbsent("status",status.getReasonPhrase());
        String jsonString = json.toJson(responseMap);
        return new ResponseEntity<>(jsonString,headers,status);
    }

    /**
     * Generating json string with 200 status
     * @param responseObject - Any object you want, but field must be marked with {@link com.google.gson.annotations.Expose}
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<String> responseSuccess(Object responseObject){
        return response(responseObject,HttpStatus.OK);
    }

    /**
     * Generating json string with 404 status
     * @param responseObject - Any object you want, but field must be marked with {@link com.google.gson.annotations.Expose}
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<String> responseBad(Object responseObject){
        return response(responseObject,HttpStatus.BAD_REQUEST);
    }
    /**
     * Generating json string with 201 status
     * @param responseObject - Any object you want, but field must be marked with {@link com.google.gson.annotations.Expose}
     * @return {@link ResponseEntity}
     */
    public static ResponseEntity<String> responseCreated(Object responseObject){
        return response(responseObject,HttpStatus.CREATED);
    }

    public static String responseString(HttpStatus status, Object response) {
        Map<String,Object> responseMap=new HashMap<>();
        responseMap.putIfAbsent("response",response);
        responseMap.putIfAbsent("status",status.getReasonPhrase());
        return json.toJson(responseMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> validationHandler(Exception e) {
        if (e instanceof ConstraintViolationException) {
            StringBuilder reasons = new StringBuilder();
            ((ConstraintViolationException) e).getConstraintViolations().forEach(cv -> reasons.append(cv.getMessage()).append(", "));
            if(reasons.length()>=2){
                reasons.delete(
                        reasons.length()-2,
                        reasons.length()-1
                );
            }
            return responseBad( reasons);
        }
        System.out.println(e.getCause().toString());
        return responseBad(e.getMessage());
    }
}
