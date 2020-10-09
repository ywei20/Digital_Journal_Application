package functionality;

import ProcessFile.DigitalEntry;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a digital entry manager implementation
 */
public class DigitalEntryManagerImpl implements DigitalEntryManager{

  List<DigitalEntry> data;
  List<String> header;
  String fileName;

  /**
   * Instantiate a new DigitalEntryManagerImpl instance
   *
   * @param data represents all data entry
   * @param header represents header of csv file
   * @param fileName represents outfile name
   */
  public DigitalEntryManagerImpl(List<DigitalEntry> data, List<String> header, String fileName) {
    this.data = data;
    this.header = header;
    this.fileName = fileName;
  }

  /**
   * Add a new entry to the system
   *
   * @param digitalEntry an object of digitalEntry
   */
  @Override
  public void addEntry(DigitalEntry digitalEntry) {
    this.data.add(digitalEntry);
    digitalEntry.setId(this.data.size());
    String text = digitalEntry.toString();
    this.WriteALineToFile("\n" + text);
  }

  /**
   * Append a new line to the end of an existing file
   *
   * @param text, text file that will be added to the file
   */
  private void WriteALineToFile(String text) {
    try (BufferedWriter outFile = new BufferedWriter(new FileWriter(this.fileName, true))) {
      outFile.write(text);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update completed status of existing digital entries
   *
   * @param ids set of ids of existing digital entries that need to update
   * @throws IDNotFoundException if ids not in the system
   */
  @Override
  public void completeDigitalEntry(Set<Integer> ids) throws IDNotFoundException {
    for (Integer id: ids) {
      if (id <= 0 || id > this.data.size()) {
        throw new IDNotFoundException("id" + id + "is not valid");
      }
      this.data.get(id-1).setCompleted(true);
    }
    this.writeAllToFile();
  }

  /**
   * Overwrite the csv file
   */
  private void writeAllToFile() {
    try (BufferedWriter outFile = new BufferedWriter(new FileWriter(this.fileName))) {
      outFile.write(String.join(",", this.header));
      for (DigitalEntry d: this.data) {
        outFile.write("\n" + d.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Display list of digital entries according to filtering options
   *
   * @param showIncomplete, indicate displaying incomplete digital entries
   * @param category, represent category of digial entries that will be displayed
   * @param sortByDate, indicate we want to sort by date(ascending)
   * @param sortByPriority, indicate we want to sort by priority(descending)
   */
  @Override
  public void displayDigitalEntries(boolean showIncomplete, String category, boolean sortByDate,
      boolean sortByPriority) throws ConflictFilterArgumentException {
    if (sortByDate && sortByPriority) {
      throw new ConflictFilterArgumentException("Cannot combine sorted by date AND priority");
    }

    List<DigitalEntry> filtered = new ArrayList<>(this.data);
    if (showIncomplete) {
      filtered = filtered.stream()
          .filter(x -> !x.isCompleted())
          .collect(Collectors.toList());
    }
    if (category != null) {
      filtered = filtered.stream()
          .filter(x -> x.getCategory() != null && x.getCategory().equals(category))
          .collect(Collectors.toList());
    }
    if (sortByDate) {
      filtered = filtered.stream()
          .sorted(Comparator.comparing(DigitalEntry::getDate))
          .collect(Collectors.toList());
    }
    if (sortByPriority) {
      filtered = filtered.stream().
          sorted(Comparator.comparing(DigitalEntry::getPriority)).
          collect(Collectors.toList());
    }

    this.displayHelper(filtered);

  }

  /**
   * Helper method used by displayDigitalEntries(), print out filtered data results
   *
   * @param data a list of DigitalEntry that will be displayed
   */
  private void displayHelper(List<DigitalEntry> data) {
    if (data.size() == 0) {
      System.out.println("No matching results");
    } else {
      System.out.println("Showing " + data.size() + " results:");
      System.out.println(String.join(",", this.header));
    }
    for (DigitalEntry digitalEntry: data) {
      System.out.println(digitalEntry.toString());
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DigitalEntryManagerImpl that = (DigitalEntryManagerImpl) o;
    return Objects.equals(data, that.data) &&
        Objects.equals(header, that.header) &&
        Objects.equals(fileName, that.fileName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, header, fileName);
  }

  @Override
  public String toString() {
    return "DigitalEntryManagerImpl{" +
        "data=" + data +
        ", header=" + header +
        ", fileName='" + fileName + '\'' +
        '}';
  }
}
