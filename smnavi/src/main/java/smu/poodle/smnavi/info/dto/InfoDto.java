package smu.poodle.smnavi.info.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import smu.poodle.smnavi.info.domain.InfoEntity;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class InfoDto {
    private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int increaseCount;

    @NotEmpty(message = "제목은 필수 항목 입니다.")
    @Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하로 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용은 필수 항목 입니다.")
    @Size(min = 1, max = 5000, message = "내용은 10자 이상 5000자 이하로 입력해주세요.")
    private String content;

    public InfoDto() {
    }

    public InfoDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public InfoDto(Long id) {
        this.id = id;
    }

    public InfoDto(Long id, LocalDateTime regDate, LocalDateTime updateDate, String title, String content) {
        this.id = id;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.title = title;
        this.content = content;
    }

    public InfoDto(Long id, String title, String content, LocalDateTime regDate, LocalDateTime updateDate, int increaseCount) {
        this.id = id;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.title = title;
        this.content = content;
        this.increaseCount = increaseCount;
    }

    public InfoEntity ToEntity() {
        return new InfoEntity(this.id, this.title, this.content, this.regDate, this.updateDate, 0);
    }
}


