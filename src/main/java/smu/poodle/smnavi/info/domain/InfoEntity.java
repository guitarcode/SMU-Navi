package smu.poodle.smnavi.info.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class InfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    @CreationTimestamp
    private LocalDateTime regDate;
    @Column
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column
    private int increaseCount;



    public void increaseViews(){
        this.increaseCount++;
    }
}
