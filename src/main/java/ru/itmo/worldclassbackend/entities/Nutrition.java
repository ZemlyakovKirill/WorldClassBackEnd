package ru.itmo.worldclassbackend.entities;

import com.google.gson.annotations.Expose;
import lombok.*;
import ru.itmo.worldclassbackend.utils.NutritionType;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.util.List;

@Entity
@Table(name = "nutritions")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Nutrition {
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nutrition_id")
    private Long id;

    @Expose
    @Column(name="name",nullable = false)
    @NotNull(message = "Name must be not null")
    @NonNull
    private String name;

    @Expose
    @Column(name="calories",nullable = false)
    @NotNull(message = "Calories must be not null")
    @Min(value = 0,message = "Calories must be positive")
    @NonNull
    private int calories;

    @Expose
    @Column(name="weight",nullable = false)
    @NotNull(message = "Weight must be not null")
    @Min(value = 0,message = "Weight must be positive")
    @NonNull
    private int weight;


    @Expose
    @Enumerated(EnumType.ORDINAL)
    @Column(name="day",nullable = false)
    @NotNull(message = "Day must be provided")
    @NonNull
    private DayOfWeek dayOfWeek;

    @Expose
    @Enumerated(EnumType.ORDINAL)
    @Column(name="type",nullable = false)
    @NotNull(message = "Type must be provided")
    @NonNull
    private NutritionType nutritionType;

    @Expose
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "nutrition")
    private List<NutritionImage> images;

}
