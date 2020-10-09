import ProcessFile.DigitalEntry;
import ProcessFile.DigitalEntry.Builder;
import ProcessFile.ReadJournalData;
import cmd.CmdLineProcessor;
import cmd.Operation;
import cmd.ParsedArgs;
import functionality.DigitalEntryManager;
import functionality.DigitalEntryManagerImpl;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main class of the command line tool to manage digital entries
 */
public class Main {

    /**
     * The main function
     *
     * @param args args from command line
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception {
        CmdLineProcessor clProcessor = CmdLineProcessor.getInstance();  // instantiate a new CmdLineProcessor object
        ParsedArgs parsedArgs = clProcessor.processArgument(args); // Check whether args is valid argument string array

        ReadJournalData csvReader = ReadJournalData.getInstance(); // instantiate a new ReadJournalData object
        List<DigitalEntry> data = csvReader.readData(parsedArgs.getCsvFile()); //read in data from csv file

        DigitalEntryManager digitalEntryManager = new DigitalEntryManagerImpl(data,
            csvReader.getHeader(), parsedArgs.getCsvFile()); // instantiate a new DigitalEntryManager object

        Map<Operation, Set<String>> operations = parsedArgs.getOperations(); // Get all command line operations

        // Do adding entry
        if (operations.containsKey(Operation.ADD_ENTRY)) {
            digitalEntryManager.addEntry(DigitalEntry.builder()
                .setText(parsedArgs.getEntryText())
                .setCategory(parsedArgs.getCategory())
                .setCompleted(parsedArgs.getCompleted())
                .setPriority(parsedArgs.getPriority() != null ? Integer.parseInt(parsedArgs.getPriority()) : null)
                .setDate(parsedArgs.getDate())
                .build());
        }

        // Do updating complete status
        if (operations.containsKey(Operation.COMPLETE_ENTRY)) {
            Set<Integer> ids = new HashSet<>();
            operations.get(Operation.COMPLETE_ENTRY).forEach(id -> ids.add(Integer.parseInt(id)));
            digitalEntryManager.completeDigitalEntry(ids);
        }

        // do displaying
        if (operations.containsKey(Operation.DISPLAY)) {
            boolean showIncomplete = false;
            String category = null;
            boolean sortByDate = false;
            boolean sortByPriority = false;

            if (parsedArgs.getOperations().containsKey(Operation.SHOW_CATEGORY)) {
                category = operations.get(Operation.SHOW_CATEGORY).iterator().next();
            }

            if (parsedArgs.getOperations().containsKey(Operation.SHOW_INCOMPLETE)) {
                showIncomplete = true;
            }

            if (parsedArgs.getOperations().containsKey(Operation.SORT_BY_DATE)) {
                sortByDate = true;
            }

            if (parsedArgs.getOperations().containsKey(Operation.SORT_BY_PRIORITY)) {
                sortByPriority = true;
            }

            digitalEntryManager.displayDigitalEntries(showIncomplete, category, sortByDate, sortByPriority);
        }
    }
}
