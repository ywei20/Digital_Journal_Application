package functionality;

import ProcessFile.DigitalEntry;
import java.util.Set;

/**
 * Represent interface of DigitalEntryManager
 */
public interface DigitalEntryManager {

  /**
   * Add a new entry to the system
   *
   * @param digitalEntry an object of digitalEntry
   */
  void addEntry(DigitalEntry digitalEntry);

  /**
   * Update completed status of existing digital entries
   *
   * @param ids set of ids of existing digital entries that need to update
   * @throws IDNotFoundException if ids not in the system
   */
  void completeDigitalEntry(Set<Integer> ids) throws IDNotFoundException;

  /**
   * Display list of digital entries according to filtering options
   *
   * @param showIncomplete, indicate displaying incomplete digital entries
   * @param category, represent category of digial entries that will be displayed
   * @param sortByDate, indicate we want to sort by date(ascending)
   * @param sortByPriority, indicate we want to sort by priority(descending)
   */
  void displayDigitalEntries(boolean showIncomplete, String category, boolean sortByDate,
      boolean sortByPriority) throws ConflictFilterArgumentException;
}
