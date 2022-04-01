package ru.itmo.worldclassbackend.entities;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class User {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "user_id")
    private Long id;

    @Expose
    @Column(name = "email",nullable = false,unique = true)
    @NotNull(message = "Email must be not null")
    @Pattern(regexp = "^([a-z_0-9]{2,40})(@)([a-z0-9._-]{2,7})(\\.)([a-z]{2,5})$", message = "E-mail must be like \"example@exmp.com\"")
    @Size(min = 7, max = 100, message = "E-mail length must be in range from 7 to 100")
    @NonNull
    private String email;

    @Column(name = "password",nullable = false)
    @NotNull(message = "Password must be not null")
    @NonNull
    private String password;
    @Column(name = "is_active",nullable = false)
    @NotNull(message = "Is active must be not null")
    @NonNull
    private boolean isActive;

    @Expose
    @Column(name = "name",nullable = false)
    @NotNull(message = "Name must be not null")
    @NonNull
    private String name;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "authority_id",nullable = false))
    @NotNull
    @NonNull
    private Set<Authority> authorities;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_exercises",
            joinColumns = @JoinColumn(name = "user_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "exercise_id",nullable = false))
    private Set<Exercise> favouriteExercises;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_nutritions",
            joinColumns = @JoinColumn(name = "user_id",nullable = false),
            inverseJoinColumns = @JoinColumn(name = "nutrition_id",nullable = false))
    private Set<Nutrition> favouriteNutritions;

}
