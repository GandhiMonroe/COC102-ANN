package MLP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // Initialise data class and read data from url provided
        Data data = new Data("/Users/bendiliberto/IdeaProjects/ANN-MLP/FresnoDataCOC102Student.csv");
        data.Read();

        // Cleanse the data and split into sets
        data.Cleanse();

        // Initialise Network
        Network network = new Network(5, 9, 1);

        double[] result = new double[1];
        double[] trainingAttr = new double[4];
        double[] trainingPred = new double[1];
        double[] validationAttr = new double[4];
        double[] completeResult = new double[2];

        ArrayList<double[]> results = new ArrayList<double[]>();

        double RMSE = 0.0;
        double bestRMSE = 100.0;
        double selectedAt = 0;
        double lastModification = 0;

        Network selectedNetwork = null;

        // Run for the specified number of epochs
        for (int j = 0; j <= 20000; j++) {
            RMSE = 0.0;

            // Train network on training set
            for (int i = 0; i < data.trainingSet.size(); i++) {
                trainingAttr = Arrays.copyOfRange(data.trainingSet.get(i), 0, data.trainingSet.get(i).length - 1);
                trainingPred = Arrays.copyOfRange(data.trainingSet.get(i), data.trainingSet.get(i).length - 1, data.trainingSet.get(i).length);

                network.trainNetwork(trainingAttr, trainingPred);
            }

            // Use validation set to check for overfitting
            for (int k = 0; k < data.validationSet.size(); k++) {
                validationAttr = Arrays.copyOfRange(data.validationSet.get(k), 0, data.validationSet.get(k).length - 1);
                double[] realResult = Arrays.copyOfRange(data.validationSet.get(k), data.validationSet.get(k).length - 1, data.validationSet.get(k).length);

                double valResult = network.test(validationAttr);

                double destandard = ((valResult - 0.1) / 0.8) * (data.max.get(5) - data.min.get(5)) + data.min.get(5);

                RMSE += Math.pow(realResult[0] - destandard, 2);
            }

            // Calculate RMSE to validate performance of network
            RMSE = Math.sqrt(RMSE / data.validationSet.size());

            // If current network has a lower RMSE than previous best, use this network
            if(RMSE < bestRMSE){
                selectedNetwork = network;
                selectedAt = j;
                bestRMSE = RMSE;
            }
            // If error goes up and its been atleast 1000 epochs since last change, apply Bold Driver
            else if (RMSE > bestRMSE && (j - lastModification) > 1000){
                network.boldDriver(0.5);
                lastModification = j;
            }
        }

        // Run the test set on the network
        double testRMSE = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < data.testSet.size(); i++) {
            double[] testAttr = Arrays.copyOfRange(data.testSet.get(i), 0, data.testSet.get(i).length - 1);
            double[] realResult = Arrays.copyOfRange(data.testSet.get(i), data.testSet.get(i).length - 1, data.testSet.get(i).length);

            double predicted = selectedNetwork.test(testAttr);

            // Destandarise the output so that it is helpful
            double destandard = (((predicted - 0.1) / 0.8) * (data.max.get(5) - data.min.get(5)) + data.min.get(5));

            System.out.println(df.format(destandard) + "," + realResult[0]);

            testRMSE += Math.pow(realResult[0] - destandard, 2);
        }
        // Calculate RMSE for test set
        testRMSE = Math.sqrt(testRMSE / data.testSet.size());
        System.console();
    }
}
