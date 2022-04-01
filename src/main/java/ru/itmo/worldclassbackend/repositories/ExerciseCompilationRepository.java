package ru.itmo.worldclassbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.worldclassbackend.entities.ExerciseCompilation;

@Repository
public interface ExerciseCompilationRepository extends JpaRepository<ExerciseCompilation, Long> {
}
