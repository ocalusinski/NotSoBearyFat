import myPackage.Data;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DataTest {

    @Test
    public void testConstructorWithoutTotalCalBurnt() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        Data d = new Data(date, 2000, 180.5, 7.5);

        assertEquals(date, d.getDate());
        assertEquals(2000, d.getCalConsumed());
        assertEquals(180.5, d.getWeight());
        assertEquals(7.5, d.getSleep());
        assertEquals(0, d.getTotalCalBurnt());
    }

    @Test
    public void testConstructorWithTotalCalBurnt() {
        LocalDate date = LocalDate.of(2025, 1, 2);
        Data d = new Data(date, 1800, 300, 175.2, 6.8);

        assertEquals(date, d.getDate());
        assertEquals(1800, d.getCalConsumed());
        assertEquals(300, d.getTotalCalBurnt());
        assertEquals(175.2, d.getWeight());
        assertEquals(6.8, d.getSleep());
    }

    @Test
    public void testSetters() {
        Data d = new Data(LocalDate.now(), 1500, 180, 7);

        d.setCalConsumed(1900);
        d.setTotalCalBurnt(350);
        d.setWeight(170.5);
        d.setSleep(6.2);
        LocalDate newDate = LocalDate.of(2025, 3, 10);
        d.setDate(newDate);

        assertEquals(1900, d.getCalConsumed());
        assertEquals(350, d.getTotalCalBurnt());
        assertEquals(170.5, d.getWeight());
        assertEquals(6.2, d.getSleep());
        assertEquals(newDate, d.getDate());
    }
}