package MLP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        // TODO: Make client to enter data

        // Initialise data class and read data from url provided
        Data data = new Data("/Users/bendiliberto/IdeaProjects/ANN-MLP/FresnoDataCOC102Student.csv");
        data.Read();

        // Cleanse the data and split into sets
        data.Cleanse();


        Network network = new Network(5, 9, 1);

        double[] result = new double[1];
        double[] trainingAttr = new double[4];
        double[] trainingPred = new double[1];
        double[] completeResult = new double[2];

        ArrayList<double[]> results = new ArrayList<double[]>();

        double RMSE = 0.0;
        double previousRMSE = 0.0;

        Network selectedNetwork;

        for (int j = 0; j <= 10000; j++) {
            previousRMSE = RMSE;
            for (int i = 0; i < data.trainingSet.size(); i++) {
                trainingAttr = Arrays.copyOfRange(data.trainingSet.get(i), 0, data.trainingSet.get(i).length - 1);
                trainingPred = Arrays.copyOfRange(data.trainingSet.get(i), data.trainingSet.get(i).length - 1, data.trainingSet.get(i).length);

                result = network.trainNetwork(trainingAttr, trainingPred);

                RMSE += Math.pow(trainingPred[0] - result[1], 2);
            }
            RMSE = Math.sqrt(RMSE / data.trainingSet.size());
            if(RMSE < previousRMSE){
                selectedNetwork = network;
            }
        }
        System.console();
    }
}
