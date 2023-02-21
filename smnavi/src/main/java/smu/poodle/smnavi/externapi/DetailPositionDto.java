package smu.poodle.smnavi.externapi;

import lombok.Data;
import smu.poodle.smnavi.domain.DetailPosition;

@Data
public class DetailPositionDto {
    private String x;
    private String y;

    public DetailPositionDto(DetailPosition detailPosition) {
        this.x = detailPosition.getX();
        this.y = detailPosition.getY();
    }
}
