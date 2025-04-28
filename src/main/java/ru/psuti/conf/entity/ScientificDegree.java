package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;

//Учёная степень
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scientist_degrees")
public class ScientificDegree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_of_degree")
    private String nameOfDegree;

    //аббревиатура (к.т.н.)
    private String abbreviation;
}
