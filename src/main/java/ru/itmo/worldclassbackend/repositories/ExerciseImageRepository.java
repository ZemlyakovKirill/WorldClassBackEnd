package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.ExerciseImage;

@Repository
public interface ExerciseImageRepository extends JpaRepository<ExerciseImage,Long> {
    ExerciseImage findByImageName(String imageName);
}
