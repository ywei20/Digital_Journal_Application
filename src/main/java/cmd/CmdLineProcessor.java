package cmd;

import ProcessFile.DateParser;
import ProcessFile.InvalidDateException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class to parse the cmd line options and arguments
 */
public class CmdLineProcessor {

  public static final int OP_NAME_START_INDEX = 2;
  public static final List<String> REQUIRED_OPTIONS = Collections
      .unmodifiableList(Arrays.asList("--csv-file"));
  public static final Set<String> ALLOWED_PRIORITIES = Collections
      .unmodifiableSet(new HashSet<>(Arrays.asList("1", "2", "3")));

  private static CmdLineProcessor instance;

  /**
   * Constructor of CmdLineProcessor
   */
  private CmdLineProcessor() {
  }

  /**
   * Gets the singleton instance
   *
   * @return the instance
   */
  public static CmdLineProcessor getInstance() {
    if (instance == null) {
      instance = new CmdLineProcessor();
    }
    return instance;
  }

  /**
   * Main function to process the command line arguments
   *
   * @param args arguments to process
   */
  public ParsedArgs processArgument(String[] args) {
    ParsedArgs parsedArgs = new ParsedArgs();
    Map<Operation, Set<String>> operations = new HashMap<>();

    // Create a hashset for all the provided options and arguments for easier query
    Set<String> providedOptions = new HashSet<>(Arrays.asList(args));

    // Check if all the required options are provided
    for (String option : REQUIRED_OPTIONS) {
      if (!providedOptions.contains(option)) {
        throw new CmdLineException("Error: required option is not provided: " + option);
      }
    }

    int n = args.length;
    for (int i = 0; i < n; i++) {
      switch (args[i]) {
        case "--csv-file":
          // Update the path of the csv-file
          checkIfArgumentProvidedNext(args, i);

          // If the next argument is not a .csv file
          String csvPath = args[i + 1];
          if (!csvPath.contains(".csv")) {
            throw new CmdLineException("Error: --csv--file should follow the path of a .csv file");
          }
          parsedArgs.setCsvFile(args[i + 1]);
          i++;
          break;

        case "--add-entry":
          if (!providedOptions.contains("--entry-text")) {
            throw new CmdLineException(
                "Error: --add-entry provided but no --entry-text was given.");
          }
          if (!providedOptions.contains("--date")) {
            throw new CmdLineException(
                "Error: --add-entry provided but no --date was given.");
          }
          if (operations.containsKey(Operation.ADD_ENTRY)) {
            throw new CmdLineException(
                "Error: --add-entry can only be performed once");
          }
          operations.put(Operation.ADD_ENTRY, new HashSet<>());
          break;

        case "--entry-text":
          checkIfArgumentProvidedNext(args, i);
          parsedArgs.setEntryText(args[i + 1]);
          i++;
          break;

        case "--completed":
          parsedArgs.setCompleted(true);
          break;

        case "--date":
          checkIfArgumentProvidedNext(args, i);
          try {
            parsedArgs.setDate(DateParser.processDate(args[i+1]));
          } catch (DateTimeParseException | InvalidDateException e) {
            throw new CmdLineException("Error: cannot parse the specified date: " + args[i + 1]);
          }
          i++;
          break;

        case "--priority":
          checkIfArgumentProvidedNext(args, i);
          if (!ALLOWED_PRIORITIES.contains(args[i + 1])) {
            throw new CmdLineException("Error: specified priority is not valid: " + args[i + 1]);
          }
          parsedArgs.setPriority(args[i + 1]);
          i++;
          break;

        case "--category":
          checkIfArgumentProvidedNext(args, i);
          parsedArgs.setCategory(args[i + 1]);
          i++;
          break;

        // Process the following commands
        case "--complete-entry":
        case "--display":
        case "--show-incomplete":
        case "--show-category":
        case "--sort-by-date":
        case "--sort-by-priority":
          // Get the corresponding Operation enum
          Operation op = Operation.valueOf(cmdToOperationName(args[i]));
          operations.putIfAbsent(op, new HashSet<>());
          if (args[i].equals("--show-category") || args[i].equals("--complete-entry")) {
            checkIfArgumentProvidedNext(args, i);

            // --show-category can only be specified once while --complete-entry can be
            // specified multiple times
            if (args[i].equals("--show-category") && operations.get(op).size() > 0) {
              throw new CmdLineException("Error: --show-category can only be specified once");
            }
            operations.get(op).add(args[i + 1]);
            i++;
          }
          break;

        default:
          // If none of the above options matches, it's not a valid option
          throw new CmdLineException("Error: option not recognized: " + args[i]);
      }
    }
    parsedArgs.setOperations(operations);
    return parsedArgs;
  }

  /**
   * Map the command line to the operation name, e.g. from '--complete-entry' to 'COMPLETE_ENTRY'
   *
   * @return the Operation enum name
   */
  private String cmdToOperationName(String cmd) {
    return cmd.substring(OP_NAME_START_INDEX)
        .toUpperCase()
        .replace('-', '_');
  }

  /**
   * For some options, immediate argument is needed following the option. This helper funtion checks
   * if the next argument exists and if it's valid (Shouldn't be starting with "--" as it's a
   * argument not an option).
   *
   * @param args the list of provided args
   * @param i    the current index of the option in the list
   */
  private void checkIfArgumentProvidedNext(String[] args, int i) {
    if (i == args.length - 1 || args[i + 1].startsWith("--")) {
      throw new CmdLineException(
          "Error: option provided but no argument provided following the option: " + args[i]);
    }
  }
}
