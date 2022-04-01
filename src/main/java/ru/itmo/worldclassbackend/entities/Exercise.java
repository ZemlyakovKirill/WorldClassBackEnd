package ru.itmo.worldclassbackend.entities;


import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Exercise {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @Expose
    @Column(name="name",nullable = false)
    @NotNull(message = "Name must be not null")
    @NonNull
    private String name;

    @Expose
    @Column(name="advice")
    @NotNull(message = "Name must be not null")
    @NonNull
    private String advice;

    @Expose
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "exercise")
    private List<ExerciseImage> images;


    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY,targetEntity = ExerciseCompilation.class)
    @JoinColumn(name = "compilation_id",nullable = false)
    @NotNull(message = "Exercise Compilation must be not null")
    @NonNull
    private ExerciseCompilation exerciseCompilation;

}
