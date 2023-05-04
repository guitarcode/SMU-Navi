package smu.poodle.smnavi.accident;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import smu.poodle.smnavi.map.externapi.GpsPoint;
import smu.poodle.smnavi.map.externapi.accident.AccidentApi;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccidentInfoTest {

    @Autowired AccidentApi accidentApi;

    @Test
    public void updateAccidentInfo () throws Exception {

//        AccidentApi accidentApi = new AccidentApi(transitRepository);
        //given
        GpsPoint gpsPoint = new GpsPoint("127.050746", "37.646249");
        GpsPoint editedgpsPoint = new GpsPoint("127.050", "37.646");

        //when
//        accidentApi.updateAccidentInfo(gpsPoint,editedgpsPoint);
        //then
    }
}
