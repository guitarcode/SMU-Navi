package smu.poodle.smnavi.map.externapi;

import lombok.Data;
import smu.poodle.smnavi.map.domain.DetailPosition;

@Data
public class DetailPositionDto {
    private String gpsX;
    private String gpsY;

    public DetailPositionDto(DetailPosition detailPosition) {
        this.gpsX = detailPosition.getX();
        this.gpsY = detailPosition.getY();
    }
}
