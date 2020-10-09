package functionality;

import static org.junit.Assert.*;

import ProcessFile.DigitalEntry;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DigitalEntryManagerImplTest {

  private List<DigitalEntry> data;
  private List<String> header;
  private String outFileUpdateCompletion, outFileAddOneEntry;
  private DigitalEntryManagerImpl digitalEntryManager, digitalEntryManagerSame, digitalEntryManagerDiff;
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private ByteArrayOutputStream outStream;


  @Before
  public void setUp() throws Exception {
    outStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outStream));

    DigitalEntry d1 = DigitalEntry.builder().setText("Finished HW9").setCompleted(true)
        .setDate(LocalDate.parse("07/29/2020", formatter)).setPriority(2).setCategory("school").build();
    DigitalEntry d2 = DigitalEntry.builder().setText("I can’t wait for the end of this semester…").setCompleted(false)
        .setDate(LocalDate.parse("07/20/2020", formatter)).setPriority(1).build();
    DigitalEntry d3 = DigitalEntry.builder().setText("We should really clean this place").setCompleted(false)
        .setDate(LocalDate.parse("07/23/2020", formatter)).setPriority(1).setCategory("Home").build();
    DigitalEntry d4 = DigitalEntry.builder().setText("This person is really annoying me…").setCompleted(true)
        .setDate(LocalDate.parse("07/22/2020", formatter)).setPriority(3).setCategory("Relationship").build();
    DigitalEntry d5 = DigitalEntry.builder().setText("I want to learn how to pick locks…").setCompleted(false)
        .setDate(LocalDate.parse("08/02/2020", formatter)).build();
    d1.setId(1);
    d2.setId(2);
    d3.setId(3);
    d4.setId(4);
    d5.setId(5);

    data = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5));
    header = new ArrayList<>(Arrays.asList("id", "text", "completed", "due", "priority", "category"));
    outFileAddOneEntry = "out/digital_journal_add_oneline_test.csv";
    outFileUpdateCompletion = "out/digital_journal_update_completion_test.csv";

    digitalEntryManager = new DigitalEntryManagerImpl(data, header, outFileAddOneEntry);
    digitalEntryManagerSame = new DigitalEntryManagerImpl(data, header, outFileAddOneEntry);
    digitalEntryManagerDiff = new DigitalEntryManagerImpl(data, header, outFileUpdateCompletion);
  }

  @After
  public void restoreStreams() {
    System.setOut(System.out);
  }

  @Test
  public void addEntry() throws IOException {
    DigitalEntry newEntry = DigitalEntry.builder().setText("Cheer up and go to hike").setCompleted(true)
        .setDate(LocalDate.parse("07/25/2020", formatter)).setPriority(2).setCategory("Relationship").build();
    digitalEntryManager.addEntry(newEntry);

    BufferedReader inFile = new BufferedReader(new FileReader(outFileAddOneEntry));
    String line;
    List<String> allLines = new ArrayList<>();
    while ((line = inFile.readLine()) != null) {
      allLines.add(line);
    }
    String expected = "6,Cheer up and go to hike,True,07/25/2020,2,Relationship";
    assertEquals(expected, allLines.get(allLines.size()-1));
  }

  @Test
  public void completeDigitalEntry() throws IDNotFoundException, IOException {
    Set<Integer> ids = new HashSet<>();
    ids.add(2);
    ids.add(3);
    digitalEntryManager.completeDigitalEntry(ids);

    BufferedReader inFile = new BufferedReader(new FileReader(outFileUpdateCompletion));
    String line;
    List<String> allLines = new ArrayList<>();
    while ((line = inFile.readLine()) != null) {
      allLines.add(line);
    }
    String expected = "id,text,completed,due,priority,category" + "\n" +
        "1,Finished HW9,True,07/29/2020,2,school" + "\n" +
        "2,I can’t wait for the end of this semester…,True,07/20/2020,1,?" + "\n" +
        "3,We should really clean this place,True,07/23/2020,1,Home" + "\n" +
        "4,This person is really annoying me…,True,07/22/2020,3,Relationship" + "\n" +
        "5,I want to learn how to pick locks…,False,08/02/2020,3,?";
    assertEquals(expected, String.join("\n", allLines));
  }


  @Test(expected = IDNotFoundException.class)
  public void completeDigitalEntryIDNotFound() throws IDNotFoundException {
    Set<Integer> ids = new HashSet<>();
    ids.add(100);
    digitalEntryManager.completeDigitalEntry(ids);
  }

  @Test(expected = IDNotFoundException.class)
  public void completeDigitalEntryIDIsZero() throws IDNotFoundException {
    Set<Integer> ids = new HashSet<>();
    ids.add(0);
    digitalEntryManager.completeDigitalEntry(ids);
  }

  @Test
  public void displayAllDigitalEntriesSortByDate() throws ConflictFilterArgumentException {
    digitalEntryManager.displayDigitalEntries(false, null, true, false);
    String expected = "Showing 5 results:" + "\n" +
        "id,text,completed,due,priority,category" + "\n" +
        "2,I can’t wait for the end of this semester…,False,07/20/2020,1,?" + "\n" +
        "4,This person is really annoying me…,True,07/22/2020,3,Relationship" + "\n" +
        "3,We should really clean this place,False,07/23/2020,1,Home" + "\n" +
        "1,Finished HW9,True,07/29/2020,2,school" + "\n" +
        "5,I want to learn how to pick locks…,False,08/02/2020,3,?" + "\n";
    assertEquals(expected, outStream.toString());
  }

  @Test
  public void displayShowIncompleteByCategorySortByPriority() throws ConflictFilterArgumentException {
    digitalEntryManager.displayDigitalEntries(true, "Home", false, true);
    String expected = "Showing 1 results:" + "\n" +
        "id,text,completed,due,priority,category" + "\n" +
        "3,We should really clean this place,False,07/23/2020,1,Home" + "\n";
    assertEquals(expected, outStream.toString());
  }

  @Test
  public void displayNoMatchingResults() throws ConflictFilterArgumentException {
    digitalEntryManager.displayDigitalEntries(true, "school", false, true);
    String expected = "No matching results\n";
    assertEquals(expected, outStream.toString());
  }

  @Test(expected = ConflictFilterArgumentException.class)
  public void displayConflictSort() throws ConflictFilterArgumentException {
    digitalEntryManager.displayDigitalEntries(false, null, true, true);
  }

  @Test
  public void testEquals() {
    assertEquals(digitalEntryManager, digitalEntryManager);
    assertEquals(digitalEntryManager, digitalEntryManagerSame);
    assertNotEquals(digitalEntryManager, null);
    assertNotEquals(digitalEntryManager, new Object());
    assertNotEquals(digitalEntryManager, digitalEntryManagerDiff);
  }

  @Test
  public void testHashCode() {
    assertEquals(digitalEntryManager.hashCode(), digitalEntryManagerSame.hashCode());
    assertNotEquals(digitalEntryManager.hashCode(), digitalEntryManagerDiff.hashCode());
  }

  @Test
  public void testToString() {
    String expected = "DigitalEntryManagerImpl{" +
        "data=" + data +
        ", header=" + header +
        ", fileName='" + outFileAddOneEntry + '\'' +
        '}';
    assertEquals(expected, digitalEntryManager.toString());
  }
}