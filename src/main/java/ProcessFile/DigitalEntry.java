package ProcessFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * This class Digital Entry use to create object of DigitalEntry
 * It with a number of default/optional parameters
 * This class use a builder pattern
 */
public class DigitalEntry {

    private int id;
    private String text;
    private boolean completed;
    private LocalDate date;
    private Integer priority;
    private String category;

    //factory method
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder class nested in the DigitalEntry class
     */
    public static class Builder {
        private static final Integer DEFAULT_PRIORITY = 3;
        private String text = null;         //required
        private boolean completed = false;  //optional, default as false
        private LocalDate date  = LocalDate.now();          // defaults to now
        private Integer priority = DEFAULT_PRIORITY;       //optional, default is 3
        private String category = null;     //optional

        // prevent instantiation.
        private Builder() {}

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setCompleted(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder setDate(LocalDate date) {
          if (date != null) {
              this.date = date;
          }
          return this;
        }

        public Builder setPriority(Integer priority) {
            if (priority != null) {
                this.priority = priority;
            }
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public DigitalEntry build() {
            if (text == null || date == null) {
                throw new IllegalArgumentException();
            }
            return new DigitalEntry(this);
        }
    }

    private DigitalEntry(Builder builder) {
        this.text = builder.text;
        this.completed = builder.completed;
        this.date = builder.date;
        this.priority = builder.priority;
        this.category = builder.category;

    }

    //Setter of ID
    public void setId(int id) {
        this.id = id;
    }

    //setter of Completed
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    //getter of Text
    public String getText() {
        return text;
    }

    //method of checking the completed
    public boolean isCompleted() {
        return completed;
    }

    //getter of Date
    public LocalDate getDate() {
        return date;
    }

    //getter of priority
    public Integer getPriority() {
        return priority;
    }

    //getter of category
    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DigitalEntry that = (DigitalEntry) o;
        return id == that.id &&
                completed == that.completed &&
                Objects.equals(text, that.text) &&
                Objects.equals(date, that.date) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, completed, date, priority, category);
    }

    @Override
    public String toString() {
        String completedString = completed? "True" : "False";
        String categoryString = category == null? "?" : category;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return id + "," + text + "," + completedString + "," + formatter.format(date) + "," +
            priority + "," + categoryString;
    }
}
