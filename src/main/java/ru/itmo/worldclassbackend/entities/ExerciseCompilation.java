package ru.itmo.worldclassbackend.entities;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "exercise_compilations")
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExerciseCompilation {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Expose
    @Column(name="name",nullable = false,unique = true)
    @NotNull(message = "Exercise Compilation Name must be not null")
    @NonNull
    private String name;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "exerciseCompilation")
    private Set<Exercise> exerciseSet;

}
