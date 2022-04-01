package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.NutritionImage;

@Repository
public interface NutritionImageRepository extends JpaRepository<NutritionImage,Long> {
    NutritionImage findByImageName(String imageName);
}
