package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.Exercise;
import ru.itmo.worldclassbackend.entities.Nutrition;
import ru.itmo.worldclassbackend.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value="select n.* from users" +
            " join user_nutritions un on users.user_id = un.user_id" +
            " join nutritions n on n.nutrition_id = un.nutrition_id" +
            " where n.nutrition_id=:nutritionId and users.user_id=:#{#user?.id}" +
            " limit 1",nativeQuery = true)
    Optional<Nutrition> findFavoriteNutrition(Long nutritionId, User user);

    @Query(value="select e.* from users" +
            " join user_exercises ue on users.user_id = ue.user_id" +
            " join exercises e on e.exercise_id = ue.exercise_id" +
            " where e.exercise_id=:exerciseId and users.user_id=:#{#user?.id}" +
            " limit 1",nativeQuery = true)
    Optional<Exercise> findFavoriteExercise(Long exerciseId,User user);

    @Query(value = "select n.nutrition_id,n.name," +
            " (select image from nutrition_images where n.nutrition_id = un.nutrition_id limit 1) as image from users" +
            " join user_nutritions un on users.user_id = un.user_id" +
            " join nutritions n on n.nutrition_id = un.nutrition_id" +
            " where un.user_id=:#{#user?.id}",nativeQuery = true)
    List<Map<String,Object>> getAllFavouriteNutritions(User user);

    @Query(value = "select e.exercise_id,e.name," +
            " (select image from exercise_images where e.exercise_id = ue.exercise_id limit 1) as image from users" +
            " join user_exercises ue on users.user_id = ue.user_id" +
            " join exercises e on e.exercise_id = ue.exercise_id" +
            " where ue.user_id=:#{#user?.id}",nativeQuery = true)
    List<Map<String,Object>> getAllFavouriteExercises(User user);

}
