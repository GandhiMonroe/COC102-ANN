package MLP;

public class Main {

    public static void main(String[] args) {
        // TODO: Make client to enter data

        // Initialise data class and read data from url provided
        Data data = new Data("/Users/bendiliberto/IdeaProjects/ANN-MLP/FresnoDataCOC102Student.csv");
        data.Read();

        // Cleanse the data and split into sets
        data.Cleanse();


        Network network = new Network(5, 3, 1);

        double[] attributes = new double[5];
        attributes[0] = 7.4;
        attributes[1] = 2.127;
        attributes[2] = 121.4;
        attributes[3] = 101.0;
        attributes[4] = 81.0;

        double[] predictand = new double[1];
        predictand[0] = 0.8;

        network.trainNetwork(attributes, predictand);
    }
}
