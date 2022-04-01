package ru.itmo.worldclassbackend.entities;

import com.google.gson.annotations.Expose;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authority_id")
    private int id;
    @Expose
    @Column(name = "authority",unique = true)
    private String authority;


    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
