package MLP;

import java.util.Random;

public class Network {
    private int input, hidden, output; // Number of neurons in each layer

    private double[] inputs, hiddens, outputs; // Values for each neuron in each layer

    private double[][] weightsIH; // Weights from input to hidden
    private double[][] weightsHO; // Weights from hidden to output

    private double learningRate = 0.1;

    /**
     * Sets the learning rate for the Network
     *
     * @param learningRate The desired learning rate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Create instance of Network and initialises
     *
     * @param input Number of Input nodes
     * @param hidden Number of Hidden nodes
     * @param output Number of Output nodes
     */
    public Network(int input, int hidden, int output) {
        this.input = input;
        this.hidden = hidden;
        this.output = output;

        this.inputs = new double[input+1];
        this.hiddens = new double[hidden+1];
        this.outputs = new double[output+1];

        this.weightsIH = new double[hidden+1][input+1];
        this.weightsHO = new double[output+1][hidden+1];

        // Set initial weights
        randomiseWeights();
    }

    /**
     * Set the initial weights of the network
     */
    private void randomiseWeights(){
        Random randDouble = new Random();
        double wgtRange = 2/input;


        for(int j = 1; j <= hidden; j++){
            for(int i = 1; i <= input; i++){
                weightsIH[j][i] = randDouble.nextDouble() * wgtRange;
            }
        }

        for(int j = 1; j <= output; j++){
            for(int i = 1; i <= hidden; i++){
                weightsHO[j][i] = randDouble.nextDouble() * wgtRange;
            }
        }
    }
}
