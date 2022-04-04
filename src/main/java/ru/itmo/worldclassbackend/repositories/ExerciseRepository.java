package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.Exercise;
import ru.itmo.worldclassbackend.entities.ExerciseCompilation;

import java.util.List;
import java.util.Map;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {

    @Query(value = "select exercise_id," +
            " name," +
            " (select image from exercise_images where exercise_id = a.exercise_id limit 1) as image" +
            " from exercises as a" +
            " where a.compilation_id = :#{#exerciseCompilation?.id}",nativeQuery = true)
    List<Map<String,Object>> getAllByCompilation(ExerciseCompilation exerciseCompilation);

}
