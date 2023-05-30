package smu.poodle.smnavi.info.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import smu.poodle.smnavi.info.domain.InfoEntity;
import smu.poodle.smnavi.user.auth.Kind;
import smu.poodle.smnavi.info.domain.Location;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class InfoDto {
    private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int increaseCount;
    private Kind kind;
    private Location location;

    @NotEmpty(message = "제목은 필수 항목 입니다.")
    @Size(min = 1, max = 30, message = "제목은 1자 이상 30자 이하로 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용은 필수 항목 입니다.")
    @Size(min = 1, max = 5000, message = "내용은 10자 이상 5000자 이하로 입력해주세요.")
    private String content;

    public InfoEntity ToEntity() {
        return InfoEntity.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .regDate(this.regDate)
                .updateDate(this.updateDate)
                .increaseCount(0)
                .kind(this.kind)
                .location(this.location)
                .build();
    }

}


