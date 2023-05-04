package smu.poodle.smnavi.notice.domain;

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
@AllArgsConstructor //다넣거나
@NoArgsConstructor //안넣거나
@Data
public class NoticeEntity {
    @Id //pk 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // GeneratedValue, 값이 어떻게 만들어지는지 설정, GenerationType.IDENTITY, 자동으로 값 만들어지게 한다
    private Long id; //id

    @Column
    private String title; //제목

    @Column
    private String content; //내용

    @Column
    @CreationTimestamp
    private LocalDateTime regDate; //날짜

    @Column
    @UpdateTimestamp
    private LocalDateTime updateDate; //수정날짜
    @Column
    private int increaseCount; //조회수

    public void increaseViews() {
        this.increaseCount++;
    }

}
