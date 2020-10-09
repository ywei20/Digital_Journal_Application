package ProcessFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReadJournalDataTest {
    ReadJournalData readJournalData, readJournalDataWrong;
    List<String> header = new ArrayList<String>(Arrays.asList("id", "text", "completed", "due", "priority", "category"));
    List<DigitalEntry> data;

    @Before
    public void setUp() throws Exception {
        readJournalData = ReadJournalData.getInstance();
        data = readJournalData.readData("out/digital_journal.csv");
        readJournalDataWrong = ReadJournalData.getInstance();
    }

    @Test
    public void getHeader() {
        assertEquals(header, readJournalData.getHeader());
    }

//    @Test(expected = IllegalStateException.class)
//    public void getHeaderThrowException() {
//        readJournalDataWrong.getHeader();
//    }

    @Test(expected = IOException.class)
    public void readDataThrowsException() throws Exception {
        readJournalDataWrong.readData("fileNotExist");
    }

    @Test
    public void readDataSuccess() {
        DigitalEntry d1 = DigitalEntry.builder().setText("Finished HW9").setCompleted(true)
            .setDate(LocalDate.of(2020, 7, 29)).setPriority(1).setCategory("school").build();
        DigitalEntry d2 = DigitalEntry.builder().setText("I can’t wait for the end of this semester…")
            .setDate(LocalDate.of(2020, 7, 30)).setPriority(1).build();
        DigitalEntry d3 = DigitalEntry.builder().setText("We should really clean this place")
            .setDate(LocalDate.of(2020, 7, 30)).setPriority(2).setCategory("Home").build();
        DigitalEntry d4 = DigitalEntry.builder().setText("This person is really annoying me…").setCompleted(true)
            .setDate(LocalDate.of(2020, 7, 31)).setPriority(3).setCategory("Relationship").build();
        DigitalEntry d5 = DigitalEntry.builder().setText("I want to learn how to pick locks…")
            .setDate(LocalDate.of(2020, 8, 2)).build();
        d1.setId(1);
        d2.setId(2);
        d3.setId(3);
        d4.setId(4);
        d5.setId(5);

        d1.setId(1);
        d2.setId(2);
        List<DigitalEntry> expectedData = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5));
        assertEquals(expectedData, data);
    }

    @Test
    public void testToString() {
      String expected = "ReadJournalData{" +
          "header=" + header +
          '}';
      assertEquals(expected, readJournalData.toString());
    }
}