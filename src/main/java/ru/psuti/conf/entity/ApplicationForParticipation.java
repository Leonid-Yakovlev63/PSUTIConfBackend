package ru.psuti.conf.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.psuti.conf.entity.auth.User;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "application_for_participation")
public class ApplicationForParticipation {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;

    @OneToMany(mappedBy = "applicationForParticipation",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Article> articles;
}
