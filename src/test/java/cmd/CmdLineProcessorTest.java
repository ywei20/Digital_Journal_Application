package cmd;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CmdLineProcessorTest {

  private static final String CSV_FILE = "/csv_file.csv";
  private static final String ENTRY_TEXT = "sample entry text";
  private static final String DATE = "01/01/2020";
  private static final String PRIORITY = "1";
  private static final String CATEGORY = "category";
  private static final String COMPLETED_ENTRY_ID_1 = "id1";
  private static final String COMPLETED_ENTRY_ID_2 = "id2";
  private static final String SHOW_CATEGORY = "show-category";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  private CmdLineProcessor subject;

  @Before
  public void setUp() {
    subject = CmdLineProcessor.getInstance();
  }

  // Test 1: happy case with all the commands
  @Test
  public void processArgument() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);

    Assert.assertEquals(CSV_FILE, ret.getCsvFile());
    Assert.assertEquals(ENTRY_TEXT, ret.getEntryText());
    Assert.assertEquals(true, ret.getCompleted());
    Assert.assertEquals(LocalDate.parse(DATE, FORMATTER), ret.getDate());
    Assert.assertEquals(PRIORITY, ret.getPriority());
    Assert.assertEquals(CATEGORY, ret.getCategory());

    Map<Operation, Set<String>> operations = new HashMap<>();
    operations.put(Operation.ADD_ENTRY, new HashSet<>());
    operations.put(Operation.COMPLETE_ENTRY,
        new HashSet<>(Arrays.asList(COMPLETED_ENTRY_ID_1, COMPLETED_ENTRY_ID_2)));
    operations.put(Operation.DISPLAY, new HashSet<>());
    operations.put(Operation.SHOW_INCOMPLETE, new HashSet<>());
    operations.put(Operation.SHOW_CATEGORY, new HashSet<>(Arrays.asList(SHOW_CATEGORY)));
    operations.put(Operation.SORT_BY_DATE, new HashSet<>());
    operations.put(Operation.SORT_BY_PRIORITY, new HashSet<>());
    Assert.assertEquals(operations, ret.getOperations());
  }

  // Test 2: the required --csv-file option not provided
  @Test(expected = CmdLineException.class)
  public void processArgument_requiredFieldNotProvided() {
    String[] args = {
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);

    // TODO the following assertions won't be executed
    // Need find a better way to test the exception logic
    // Similarly with the rest of the test cases
    Assert.assertEquals(null, ret.getCsvFile());
    Assert.assertEquals(null, ret.getEntryText());
    Assert.assertEquals(null, ret.getCompleted());
    Assert.assertEquals(null, ret.getDate());
    Assert.assertEquals(null, ret.getPriority());
    Assert.assertEquals(null, ret.getCategory());
    Assert.assertEquals(null, ret.getOperations());
  }

  // Test 3: --add-entry provided but no --entry-text
  @Test(expected = CmdLineException.class)
  public void processArgument_addEntryWithoutEntryText() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);

    Assert.assertEquals(CSV_FILE, ret.getCsvFile());
    Assert.assertEquals(null, ret.getEntryText());
    Assert.assertEquals(null, ret.getCompleted());
    Assert.assertEquals(null, ret.getDate());
    Assert.assertEquals(null, ret.getPriority());
    Assert.assertEquals(null, ret.getCategory());
    Assert.assertEquals(null, ret.getOperations());
  }

  // Test 4: two --add-entry options provided
  @Test(expected = CmdLineException.class)
  public void processArgument_twoAddEntryOptions() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--add-entry",  // duplicate
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);

    Assert.assertEquals(CSV_FILE, ret.getCsvFile());
    Assert.assertEquals(null, ret.getEntryText());
    Assert.assertEquals(null, ret.getCompleted());
    Assert.assertEquals(null, ret.getDate());
    Assert.assertEquals(null, ret.getPriority());
    Assert.assertEquals(null, ret.getCategory());
    Assert.assertEquals(null, ret.getOperations());
  }

  // Test 5: error parsing the provided date
  @Test(expected = CmdLineException.class)
  public void processArgument_dateParsingError() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", "random-string-for-date",
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);

    Assert.assertEquals(CSV_FILE, ret.getCsvFile());
    Assert.assertEquals(ENTRY_TEXT, ret.getEntryText());
    Assert.assertEquals(null, ret.getCompleted());
    Assert.assertEquals(null, ret.getDate());
    Assert.assertEquals(null, ret.getPriority());
    Assert.assertEquals(null, ret.getCategory());

    Map<Operation, Set<String>> operations = new HashMap<>();
    operations.put(Operation.ADD_ENTRY, new HashSet<>());
    Assert.assertEquals(operations, ret.getOperations());
  }

  // Test 6: invalid priority number provided
  @Test(expected = CmdLineException.class)
  public void processArgument_invalidPriority() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", "4", // can only be 1, 2, 3
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);
  }

  // Test 7: two --show-category options
  @Test(expected = CmdLineException.class)
  public void processArgument_twoShowCategoryOptions() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--show-category", SHOW_CATEGORY + 1,
        "--sort-by-date",
        "--sort-by-priority"
    };

    ParsedArgs ret = subject.processArgument(args);
  }

  // Test 8: option provided but no following argument
  @Test(expected = CmdLineException.class)
  public void processArgument_optionWithoutFollowingArgument() {
    String[] args = {
        "--csv-file", // NO following argument provided
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority",
    };

    ParsedArgs ret = subject.processArgument(args);
  }

  // Test 9: invalid command
  @Test(expected = CmdLineException.class)
  public void processArgument_invalidCommand() {
    String[] args = {
        "--csv-file", CSV_FILE,
        "--add-entry",
        "--entry-text", ENTRY_TEXT,
        "--completed",
        "--date", DATE,
        "--priority", PRIORITY,
        "--category", CATEGORY,
        "--complete-entry", COMPLETED_ENTRY_ID_1,
        "--complete-entry", COMPLETED_ENTRY_ID_2,
        "--display",
        "--show-incomplete",
        "--show-category", SHOW_CATEGORY,
        "--sort-by-date",
        "--sort-by-priority",
        "--random-command" // INVALID Command
    };

    ParsedArgs ret = subject.processArgument(args);
  }
}