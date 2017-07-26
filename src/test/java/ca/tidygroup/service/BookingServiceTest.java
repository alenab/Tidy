package ca.tidygroup.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookingServiceTest {

    @Autowired
    private BookingService service;

    @Test
    public void checkFreeTime() {
        List<LocalTime> list = service.getFreeTime(LocalDate.now());
        System.out.println(list);
        assertThat(list, hasSize(greaterThan(0)));
    }
}
