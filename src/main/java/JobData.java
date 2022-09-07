import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        // ***** ER NOTES *****
        // 1. Creates an ArrayList of HashMaps (job entries) called "jobs"
        // 2. Loops over allJobs (an ArrayList of HashMaps) using a for/each loop
        // 3. Creates a variable to get the SELECTED COLUMN'S value in a single row (aValue)
        // 4. Checks that the variable contains the search term
        // 5. If it does contain a value, it adds the row to the ArrayList "jobs"
        // 6. Returns "jobs" (ArrayList)

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        // TODO - implement this method

        // ***** ER NOTES *****
        // GOAL: SEARCH FOR A TERM FROM ALL OF THE COLUMNS
        // • Should not contain duplicate jobs.
        // • If a new column is added, it'll automatically search the new column as well
        // • Don't use findByColumnAndValue()
        // • Will look similar to findByColumnAndValue()

        // 1. Creates an ArrayList of HashMaps (job entries) called "jobs"
        // 2. Loops over allJobs (an ArrayList of HashMaps) using a for/each loop
        // 3. Creates a variable to get the SELECTED COLUMN'S value in a single row (aValue)
        // 4. Checks that the variable contains the search term
        // 5. If it does contain a value, it adds the row to the ArrayList "jobs"
        // 6. Returns "jobs" (ArrayList)

        // 1. Create an ArrayList of HashMaps (job entries) called [jobs ArrayList]
        // 2. Loop over each entry [row] of allJobs
        // 3. Loop over each column [detail] in the entry
        // 4. If the column contains the search term, add it to [jobs ArrayList]
        // 5. If the row has already been added, don't add it again

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs ) {
            for (Map.Entry<String, String> detail : row.entrySet()) {

//                char firstCharacter = value.charAt(0);
//                char lastCharacter = value.charAt(value.length() - 1);
//                String searchedTerm = detail.getValue().substring(firstCharacter, lastCharacter);

                if (detail.getValue().toLowerCase().contains(value.toLowerCase())) {

                    if (!jobs.contains(row)) {
                        jobs.add(row);
                    }
                }
            }
        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
