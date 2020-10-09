package ProcessFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * The class is responsible for read and store data from csv file
 */
public class ReadJournalData {

    //Singleton
    private static ReadJournalData instance = null;

    /**
     * Get a ReadJournalData instance
     *
     * @return a ReadJournalData instance
     */
    public static ReadJournalData getInstance() {
        if (instance == null) {
            instance = new ReadJournalData();
        }
        return instance;
    }

    /**
     * Constructor of ReadJournalData
     */
    private ReadJournalData() {}

    //field of header
    private List<String> header = null;

    /**
     * Getter of header
     *
     * @return header list
     */
    public List<String> getHeader() {
        if (header == null) {
            throw new IllegalStateException("readData() must be called first before headers are available.");
        }
        return header;
    }

    /**
     * Method of file reading
     *
     * @param fileName String
     * @return List<DigitalEntry></DigitalEntry>
     * @throws Exception IO exception, file not find exception
     */
    public List<DigitalEntry> readData(String fileName) throws Exception {
        //singleton
        synchronized (this) {
            List<DigitalEntry> dataMap = new ArrayList<>();           //set up the returned object
            Scanner scanIn;                                           //temp save the readfile
            List<String> headers = new ArrayList<>();                 //save the header information


            //set up a scanner
            try {
                scanIn = new Scanner(new BufferedReader(new FileReader(fileName)));
            } catch (IOException e) {
                System.out.println(String.format("%s was not found.", fileName));
                throw e;
            }

            for (int rowCounter = 0; scanIn.hasNextLine(); rowCounter++) {
                String InputLine = scanIn.nextLine();
                //the comma followed by non/paired quotation marks will be counted as separator
                List<String> lineContent = Arrays
                        .asList(InputLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                //save information based on header
                if (rowCounter == 0) {
                    headers = lineContent;
                } else {
                    Map<String, String> map = mapValues(headers, lineContent);
                    buildObject(map, dataMap, rowCounter);
                }
            }
            scanIn.close();
            this.header = headers;
            return dataMap;
        }
    }

    /**
     * Create a digital entry object
     *
     * @param map key of map is field and value is the data
     * @param dataMap a list storing all digital entry value
     * @param id of digital entry
     * @throws Exception if date format is invalid
     */
    private void buildObject(Map<String, String> map, List<DigitalEntry> dataMap, int id)  throws Exception {
        DigitalEntry.Builder builder = DigitalEntry.builder().setDate(DateParser.processDate(map.get("due")))
            .setText(map.get("text"));

        if(!map.get("priority").equals("?")) {
            builder.setPriority(Integer.valueOf(map.get("priority")));
        }

        if(!map.get("category").equals("?")) {
            builder.setCategory(map.get("category"));
        }

        if(!map.get("completed").isEmpty()) {
            builder.setCompleted(Boolean.valueOf(map.get("completed")));
        }

        DigitalEntry newEntry = builder.build();
        newEntry.setId(id);

        dataMap.add(newEntry);
    }

    /**
     * helper function for reading data by entry
     *
     * @param headers List<String></String>
     * @param values List<String></String>
     * @return Map<String, String></String,>
     */
    private Map<String, String> mapValues(List<String> headers, List<String> values) {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < headers.size(); i++) {
            result.put(headers.get(i), values.get(i));
        }
        return result;
    }


    @Override
    public String toString() {
        return "ReadJournalData{" +
                "header=" + header +
                '}';
    }
}

