package ru.psuti.conf.entity;


import jakarta.persistence.*;
import lombok.*;

//Учёное звание
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scientist_ranks")
public class ScientistRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_of_rank")
    private String nameOfRank;

    private String abbreviation;

}
