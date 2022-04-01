package ru.itmo.worldclassbackend.entities;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exercise_images")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class ExerciseImage {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Expose
    @Column(name="image",nullable = false)
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @NotNull(message = "Image must be not null")
    @NonNull
    private byte[] image;

    @Expose
    @Column(unique = true,name = "image_name")
    @NotNull(message = "Image name must be not null")
    @NonNull
    private String imageName;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY,targetEntity = Exercise.class)
    @JoinColumn(name = "exercise_id",nullable = false)
    @NotNull(message = "Exercise must be not null")
    @NonNull
    private Exercise exercise;
}

