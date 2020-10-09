package ProcessFile;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.junit.Before;
import org.junit.Test;

public class DateParserTest {

  String dateWrongLength, dateInvalidYear, dateInvalidMonth, dateIncorrectday,
  happyDateSingleDigit, happyDateDoubleDigit;

  @Before
  public void setUp() throws Exception {
    dateWrongLength = "08/20";
    dateInvalidYear = "08/20/20";
    dateInvalidMonth = "008/20/2020";
    dateIncorrectday = "08/33/2020";
    happyDateSingleDigit = "8/3/2020";
    happyDateDoubleDigit = "08/03/2020";
  }

  @Test(expected = InvalidDateException.class)
  public void processDateWrongLength() throws InvalidDateException {
    DateParser.processDate(dateWrongLength);
  }

  @Test(expected = InvalidDateException.class)
  public void processDateInvalidYear() throws InvalidDateException {
    DateParser.processDate(dateInvalidYear);
  }

  @Test(expected = InvalidDateException.class)
  public void processDateInvalidMonth() throws InvalidDateException {
    DateParser.processDate(dateInvalidMonth);
  }

  @Test(expected = DateTimeParseException.class)
  public void processDateIncorrectDay() throws InvalidDateException {
    DateParser.processDate(dateIncorrectday);
  }

  @Test
  public void processDateSuccessSingleDigit() throws InvalidDateException {
    LocalDate expected = LocalDate.of(2020, 8, 3);
    LocalDate actual = DateParser.processDate(happyDateSingleDigit);
    assertEquals(expected, actual);
  }

  @Test
  public void processDateSuccessDoubleDigit() throws InvalidDateException {
    LocalDate expected = LocalDate.of(2020, 8, 3);
    LocalDate actual = DateParser.processDate(happyDateDoubleDigit);
    assertEquals(expected, actual);
  }

}