package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.daos.impl.TheatreImplDAO;
import com.example.theatreoffice.models.Theatre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TheatreDAOTest {
    @TestConfiguration
    static class TheatreDAOTestContextConfiguration {

        @Bean
        public TheatreDAO theatreDAO() {
            return new TheatreImplDAO();
        }

        @Bean
        public ScheduleDAO scheduleDAO() {
            return new ScheduleImplDAO();
        }
    }

    @Autowired
    private TheatreDAO theatreDAO;

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Test
    public void testGetTheatresByName() {
        Theatre theatre1 = new Theatre("Ноябрь", 25, 26, 5, 7, "Ульянова", "Ханты-Мансийск", "Россия");
        Theatre theatre2 = new Theatre("Ноябрь", 78, 65, 25, 8, "Желтая", "Уссурийск", "Россия");

        theatreDAO.save(theatre1);
        theatreDAO.save(theatre2);
        List<Theatre> theatresExpected = new ArrayList<>();
        theatresExpected.add(theatre1);
        theatresExpected.add(theatre2);

        List<Theatre> theatresGot = theatreDAO.getTheatresByName("Ноябрь");

        Assertions.assertNotNull(theatresExpected);
        Assertions.assertEquals(theatresExpected, theatresGot);

        theatreDAO.delete(theatre1);
        theatreDAO.delete(theatre2);
    }

    @Test
    public void testGetTheatre() {
        Theatre theatreExpected = new Theatre("Ноябрь", 25, 26, 5, 7, "Ульянова", "Ханты-Мансийск", "Россия");
        theatreDAO.save(theatreExpected);

        Optional<Theatre> somethingGot = theatreDAO.getTheatre("Лучший", "Москва", "Россия");
        Assertions.assertTrue(somethingGot.isEmpty());

        somethingGot = theatreDAO.getTheatre("Ноябрь", "Ханты-Мансийск", "Россия");
        Assertions.assertTrue(somethingGot.isPresent());

        Theatre theatreGot = somethingGot.get();
        Assertions.assertEquals(theatreExpected, theatreGot);
        theatreDAO.delete(theatreExpected);
    }

    @Test
    public void testGetTheatresByLocation() {
        Theatre theatre1 = new Theatre("Ноябрь", 25, 26, 5, 7, "Ульянова", "Ханты-Мансийск", "Россия");
        Theatre theatre2 = new Theatre("Октябрь", 78, 65, 25, 8, "Желтая", "Ханты-Мансийск", "Россия");

        theatreDAO.save(theatre1);
        theatreDAO.save(theatre2);

        List<Theatre> theatresExpected = theatreDAO.getAllTheatres();

        List<Theatre> theatresGot = theatreDAO.getTheatresByLocation("", "");
        Assertions.assertEquals(theatresExpected, theatresGot);

        theatresExpected = new ArrayList<>();
        theatresExpected.add(theatre1);
        theatresExpected.add(theatre2);

        theatresGot = theatreDAO.getTheatresByLocation("Ханты-Мансийск", "Россия");
        Assertions.assertEquals(theatresExpected, theatresGot);

        Optional<Theatre> somethingGot = theatreDAO.getTheatreById(theatre1.getId());
        Assertions.assertTrue(somethingGot.isPresent());
        Theatre theatreGot = somethingGot.get();
        Assertions.assertEquals(theatre1, theatreGot);

        theatreDAO.delete(theatre1);
        theatreDAO.delete(theatre2);
    }

}
