package ProcessFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a dateParser, which can parse date string with both formats of "7/9/2020" and "07/09/2020",
 * the DateParser will be used both in CmdLineProcessor and ReadJournalData
 */
public class DateParser {

  private static int YEARLENGTH = 4;
  private static int MINLENGTH = 1;
  private static int MAXLENGTH = 2;
  private static int EXPECTED_DATE_LENGTH = 3;
  private static int YEAR_POS = 2;

  /**
   * Constructor for DateParser
   */
  public DateParser() {
  }

  /**
   * Process date from a string format to a correct local date
   *
   * @param date e.g. "07/09/2020" or "7/9/2020"
   * @return a date of LocalDate type
   * @throws InvalidDateException if string date is invalid
   */
  public static LocalDate processDate(String date) throws InvalidDateException {

    String[] arr = date.split("/");

    if (arr.length != EXPECTED_DATE_LENGTH) { // e.g. date is "08/25"
      throw new InvalidDateException("Invalid date format, should be 'MM/dd/yyyy'");
    }
    if (arr[YEAR_POS].length() != YEARLENGTH) { // e.g. date is "08/25/20"
      throw new InvalidDateException("Invalid year: " + arr[YEAR_POS] + ", should be 'yyyy'");
    }
    for (int i = 0; i < arr.length - 1; i++) {
      if (arr[i].length() == MINLENGTH) {
        arr[i] = "0" + arr[i]; // "7" -> "07"
      } else if (arr[i].length() != MAXLENGTH) { // if month and date is not of length 1 or 2, throw exception
        throw new InvalidDateException("Invalid month/date: " + arr[i]);
      }
    }

    String validDate = String.join("/", arr);
    LocalDate localDate = LocalDate.now();
    try {
      localDate = LocalDate.parse(validDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    } catch (DateTimeParseException e) {
      throw e;
    }
    return localDate;
  }
}
