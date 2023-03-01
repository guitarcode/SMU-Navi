package smu.poodle.smnavi.externapi;

import lombok.Data;
import smu.poodle.smnavi.domain.DetailPosition;

@Data
public class DetailPositionDto {
    private String gpsX;
    private String gpsY;

    public DetailPositionDto(DetailPosition detailPosition) {
        this.gpsX = detailPosition.getX();
        this.gpsY = detailPosition.getY();
    }
}
