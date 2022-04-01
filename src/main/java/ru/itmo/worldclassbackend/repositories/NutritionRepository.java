package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.Nutrition;
import ru.itmo.worldclassbackend.utils.NutritionType;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Repository
public interface NutritionRepository extends JpaRepository<Nutrition,Long> {

    List<Nutrition> findByNutritionType(NutritionType type);
    List<Nutrition> findByNutritionTypeAndDayOfWeek(NutritionType type, DayOfWeek day);
    List<Nutrition> findByDayOfWeek(DayOfWeek day);

    @Query(value = "select nutrition_id, name," +
            " (select image from nutrition_images where nutrition_id = a.nutrition_id limit 1) as image" +
            " from nutritions as a where type=:#{#type?.ordinal()} and day=:#{#dayOfWeek?.ordinal()}",nativeQuery = true)
    List<Map<String,Object>> getAllByTypeAndDay(DayOfWeek dayOfWeek, NutritionType type);

    @Query(value = "select e.exercise_id, e.name, " +
            " (select image from exercise_images where exercise_id = ue.exercise_id limit 1) as image" +
            " from worldclassdb.users" +
            " join user_exercises ue on users.user_id = ue.user_id" +
            " join exercises e on ue.exercise_id = e.exercise_id" +
            " where ue.user_id=:userId",nativeQuery = true)
    List<Map<String,Object>> getFavouritesByUser(Long userId);
}
