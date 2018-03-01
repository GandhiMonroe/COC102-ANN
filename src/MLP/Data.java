package MLP;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Data {
    private String url;
    public ArrayList<String[]> rawData = new ArrayList<String[]>();
    public ArrayList<double[]> cleanData = new ArrayList<double[]>();

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

    public void Cleanse() {
        String[] previousData = null;
        double entry;

        for (String[] dataEntry: rawData) {
            for(int i = 0; i < dataEntry.length; i++) {

                // Replace any empty data with previous value
                if (dataEntry[i].equals("")){
                    if(previousData != null) {
                        dataEntry[i] = previousData[i];
                    }
                }

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
                    case 1:
                        if(entry > 56 || Math.abs(entry - Double.parseDouble(previousData[i])) > 20 || entry < -43) {
                            dataEntry[i] = previousData[i];
                        }
                        break;

                    case 2:
                        // Fastest wind speed recorded is 253mph, probably not gonna be that fast...
                        if(entry > 250 || entry < 0) {
                            dataEntry[i] = previousData[i];
                        }

                    case 3:
                        if(entry > 0 || entry < 500) {
                            dataEntry[i] = previousData[i];
                        }

                    case 4:
                        if(entry > 110 || entry < 90) {
                            dataEntry[i] = previousData[i];
                        }

                    case 5:
                        if(entry > 100 || entry < 0) {
                            dataEntry[i] = previousData[i];
                        }
                }

                // Standardisation

                // Split
            }

            previousData = dataEntry;

            cleanData.add(Arrays.stream(dataEntry).mapToDouble(Double::parseDouble).toArray());
        }
    }
}
