package smu.poodle.smnavi.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class Scheduler {

    @Scheduled(cron="0/10 * * * * *")
    public void test2() {
            System.out.println("@Scheduled annotation : 10초에 1번씩 console 찍기");


    }
}
