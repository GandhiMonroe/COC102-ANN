package MLP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Data {
    private String url;
    private ArrayList<String[]> rawData = new ArrayList<String[]>();
    private ArrayList<double[]> cleanData = new ArrayList<double[]>();

    public List<double[]> trainingSet = new ArrayList<double[]>();
    public List<double[]> validationSet = new ArrayList<double[]>();
    public List<double[]> testSet = new ArrayList<double[]>();

    public HashMap<Integer, Double> max = new HashMap<>();
    public HashMap<Integer, Double> min = new HashMap<>();

    /**
     * Initialises data file url
     *
     * @param url The url of the data file
     */
    public Data(String url) {
        this.url = url;
    }

    /**
     * Reads the data file passed to the class and outputs to ArrayList data
     */
    public void Read() {

        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(this.url));

            // Reads each line of the file and splits by comma
            while((line = br.readLine()) != null) {
                String[] dataPoints = line.split(",");

                rawData.add(Arrays.copyOfRange(dataPoints, 1, dataPoints.length));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // Closes BufferedReader if open
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            rawData.remove(0);
        }
    }

    /**
     * Clean the data (remove strings, fill missing data, remove spurious data)
     * Standardise data
     * Split data
     */
    public void Cleanse() {
        String[] previousData = new String[]{"17", "1.6", "433", "100", "6"};
        double entry;

        min.put(0, 99999.9); min.put(1, 99999.9); min.put(2, 99999.9); min.put(3, 99999.9); min.put(4, 99999.9); min.put(5, 99999.9);
        max.put(0, 0.0); max.put(1, 0.0); max.put(2, 0.0); max.put(3, 0.0); max.put(4, 0.0); max.put(5, 0.0);

        for (String[] dataEntry: rawData) {
            for(int i = 0; i < dataEntry.length; i++) {

                // Replace any empty data with previous value
                if (dataEntry[i].equals("")){
                    dataEntry[i] = previousData[i];
                }

                // Attempt to parse element as double, if fail take previous
                try {
                    entry = Double.parseDouble(dataEntry[i]);
                }
                catch (NumberFormatException e) {
                    dataEntry[i] = previousData[i];
                    entry = Double.parseDouble(dataEntry[i]);
                    continue;
                }


                // Remove spurious values
                switch (i) {
                    case 0:
                        if(entry > 56 || Math.abs(entry - Double.parseDouble(previousData[i])) > 20 || entry < -43) {
                            dataEntry[i] = previousData[i];
                            break;
                        }
                        if(entry < min.get(0)) {
                            min.replace(0, entry);
                        }
                        if(entry > max.get(0)) {
                            max.replace(0, entry);
                        }
                        break;

                    case 1:
                        // Fastest wind speed recorded is 253mph, probably not gonna be that fast...
                        if(entry > 250 || entry < 0) {
                            dataEntry[i] = previousData[i];
                            break;
                        }
                        if(entry < min.get(1)) {
                            min.replace(1, entry);
                        }
                        if(entry > max.get(1)) {
                            max.replace(1, entry);
                        }
                        break;

                    case 2:
                        if(entry < 0 || entry > 800) {
                            dataEntry[i] = previousData[i];
                            break;
                        }
                        if(entry < min.get(2)) {
                            min.replace(2, entry);
                        }
                        if(entry > max.get(2)) {
                            max.replace(2, entry);
                        }
                        break;

                    case 3:
                        if(entry > 110 || entry < 90) {
                            dataEntry[i] = previousData[i];
                            break;
                        }
                        if(entry < min.get(3)) {
                            min.replace(3, entry);
                        }
                        if(entry > max.get(3)) {
                            max.replace(3, entry);
                        }
                        break;

                    case 4:
                        if(entry > 100 || entry < 0) {
                            dataEntry[i] = previousData[i];
                            break;
                        }
                        if(entry < min.get(4)) {
                            min.replace(4, entry);
                        }
                        if(entry > max.get(4)) {
                            max.replace(4, entry);
                        }
                        break;

                    case 5:
                        if(entry < min.get(5)) {
                            min.replace(5, entry);
                        }
                        if(entry > max.get(5)) {
                            max.replace(5, entry);
                        }
                        break;
                }
            }

            previousData = dataEntry;

            cleanData.add(Arrays.stream(dataEntry).mapToDouble(Double::parseDouble).toArray());
        }

        // Split
        int split = Math.round(cleanData.size() / 5);
        trainingSet = cleanData.subList(0, split * 3);
        validationSet = cleanData.subList(trainingSet.size(), trainingSet.size() + split);
        testSet = cleanData.subList(trainingSet.size() + validationSet.size(), trainingSet.size() + validationSet.size() + split + 1);

        // Standardisation between 0.1 and 0.9
        for (double[] data: trainingSet) {
            for (int i = 0; i < data.length; i++) {
                data[i] = 0.8 * (
                            (data[i] - min.get(i)) / (max.get(i) - min.get(i))
                        )
                        + 0.1;
            }
        }
        for (double[] data: validationSet) {
            for (int i = 0; i < data.length - 1; i++) {
                data[i] = 0.8 * (
                        (data[i] - min.get(i)) / (max.get(i) - min.get(i))
                )
                        + 0.1;
            }
        }
        for (double[] data: testSet) {
            for (int i = 0; i < data.length - 1; i++) {
                data[i] = 0.8 * (
                        (data[i] - min.get(i)) / (max.get(i) - min.get(i))
                )
                        + 0.1;
            }
        }
    }
}
