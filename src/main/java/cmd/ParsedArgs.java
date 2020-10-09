package cmd;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Models the parsed arguments from the command line
 */
public class ParsedArgs {

  private String csvFile;
  // Record the operations needed to perform. We use a map here because:
  // 1. The 'COMPLETE_ENTRY' operation could be performed multiple times, we use a list to keep track of those.
  // 2. The 'SHOW_CATEGORY' sub command has an argument, we use a list to store it.
  // For the other operations the list would be empty.
  private Map<Operation, Set<String>> operations;
  private String entryText;
  private Boolean completed;
  private LocalDate date;
  private String priority;
  private String category;

  /**
   * Constructor of ParsedArgs. It's empty as We use setters to set the fields
   */
  public ParsedArgs() {
    this.operations = new HashMap<>();
    this.completed = false; // false by default
  }

  /**
   * Get the csvFile
   *
   * @return the csvFile
   */
  public String getCsvFile() {
    return csvFile;
  }

  /**
   * Set the csvFile
   */
  public void setCsvFile(String csvFile) {
    this.csvFile = csvFile;
  }

  /**
   * Get the operations
   *
   * @return the operations
   */
  public Map<Operation, Set<String>> getOperations() {
    return operations;
  }

  /**
   * Set the operations
   */
  public void setOperations(Map<Operation, Set<String>> operations) {
    this.operations = operations;
  }

  /**
   * Get the entryText
   *
   * @return the entryText
   */
  public String getEntryText() {
    return entryText;
  }

  /**
   * Set the text
   */
  public void setEntryText(String text) {
    this.entryText = text;
  }

  /**
   * Get the completed
   *
   * @return the completed
   */
  public Boolean getCompleted() {
    return completed;
  }

  /**
   * Set the completed
   */
  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  /**
   * Get the date
   *
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Set the date
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Get the priority
   *
   * @return the priority
   */
  public String getPriority() {
    return priority;
  }

  /**
   * Set the priority
   */
  public void setPriority(String priority) {
    this.priority = priority;
  }

  /**
   * Get the category
   *
   * @return the category
   */
  public String getCategory() {
    return category;
  }

  /**
   * Set the category
   */
  public void setCategory(String category) {
    this.category = category;
  }
}
