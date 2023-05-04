package smu.poodle.smnavi.map.externapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ApiConstantValue {
    @Value("${SMU-X}")
    private String SMU_X;
    @Value("${SMU-Y}")
    private String SMU_Y;
    @Value("${SEOUL-DATA-API-KEY}")
    private String seoulDataApiKey;
    @Value("${ODSAY-API-KEY}")
    private String odsayApiKey;
    @Value("${GPS-CONVERT-API-KEY}")
    private String gpsConvertApiKey;
}
