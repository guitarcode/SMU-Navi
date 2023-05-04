package smu.poodle.smnavi.info.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import smu.poodle.smnavi.user.domain.UserEntity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class LikeHateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @NotNull
    private UserEntity user;

    @ManyToOne
    @NotNull
    private InfoEntity board;

    @Column
    int identify; //버튼 누름 여부

}
