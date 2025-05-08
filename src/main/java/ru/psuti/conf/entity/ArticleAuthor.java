package ru.psuti.conf.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.convert.ValueConverter;
import ru.psuti.conf.entity.auth.UserLocalized;
import ru.psuti.conf.util.JSONConverter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "authors")
public class ArticleAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String phone;

    private String firstNameRu;

    private String middleNameRu;

    private String lastNameRu;

    private String firstNameEn;

    private String middleNameEn;

    private String lastNameEn;

    @Column(name = "country", length = 300)
    private String country;

    @Column(name = "city", length = 300)
    private String city;

    @Column(name = "organization", length = 900)
    private String organization;

    @Column(name = "organization_address", length = 450)
    private String organizationAddress;

    @JoinColumn(name = "scientific_degree", referencedColumnName = "id")
    @OneToOne(targetEntity = ScientificDegree.class, cascade = CascadeType.MERGE)
    private ScientificDegree scientificDegree;

    @JoinColumn(name = "scientist_rank", referencedColumnName = "id")
    @OneToOne(targetEntity = ScientistRank.class, cascade = CascadeType.MERGE)
    private ScientistRank scientistRank;

//    @Convert(converter = JSONConverter.class)
//    @Column(columnDefinition = "jsonb")
//    private Map<String, Object> additionalFields = new HashMap<>();
}
