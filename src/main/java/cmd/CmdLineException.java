package cmd;

/**
 * Model general command line exception.
 */
public class CmdLineException extends RuntimeException {

  private static String usageHelp;

  //Used to represent USAGE help when it encounters an exception
  static {
    usageHelp = "Usage:\n";
    usageHelp +=
        "--csv-file <path/to/file>: The CSV file containing the digital entries. This option is required.\n"
            + "--add-entry <path/to/file>: Add a new digital entry. If this option is provided, then --entry-text must also be provided\n"
            + "--entry-text <description of entry>: A description of the digital entry.\n"
            + "--completed: (Optional) Sets the completed status of a new digital entry to true. "
            + "--date <date>:(Optional) Sets the date of a new digital entry. You may choose how the date should be formatted.\n"
            + "--priority <1, 2, or 3>: (Optional) Sets the priority of a new digital entry. The value can be 1, 2, or 3.\n"
            + "--category <a category name>: (Optional) Sets the category of a new digital entry. The value can be any String. Categories do not need to be pre-defined.\n"
            + "--complete-entry <id>: Mark the DigitalEntry with the provided ID as complete.\n"
            + "--display: Display digital entry. If none of the following optional arguments are provided, displays all digital entries.\n"
            + "--show-incomplete: (Optional) If --display is provided, only incomplete digital entries should be displayed.\n"
            + "--show-category <category>: (Optional) If --display is provided, only digital entries with the given category should be displayed.\n"
            + "--sort-by-date: (Optional) If --display is provided, sort the list of digital entries by date order (ascending). Cannot be combined with --sort-by-priority.\n"
            + "--sort-by-priority: (Optional) If --display is provided, sort the list of digital entries by priority (ascending). Cannot be combined with --sort-by-date.\n";
  }

  /**
   * This constructor prints the error message on command line and exits successfully.
   *
   * @param msg Error message
   */
  public CmdLineException(String msg) {
    super(msg + "\n\n" + usageHelp);
  }
}
