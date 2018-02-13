package MLP;

import java.util.Random;

public class Network {
    private int input, hidden, output; // Number of neurons in each layer

    private double[] inputs, hiddens, outputs; // Values for each neuron in each layer

    private double[][] weightsIH; // Weights from input to hidden
    private double[][] weightsHO; // Weights from hidden to output

    private double learningRate = 0.1;

    /**
     * Create instance of Network and initialises
     *
     * @param input Number of Input nodes
     * @param hidden Number of Hidden nodes
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
        double wgtRange = 2.0/input;


        for(int j = 1; j <= hidden; j++){
            for(int i = 1; i <= input; i++){
                weightsIH[j][i] = randDouble.nextDouble() * (wgtRange * 2) - wgtRange; // Will generate random double between -2/n and 2/n for each weight
            }
        }

        for(int j = 1; j <= output; j++){
            for(int i = 1; i <= hidden; i++){
                weightsHO[j][i] = randDouble.nextDouble() * (wgtRange * 2) - wgtRange; // Will generate random double between -2/n and 2/n for each weight
            }
        }
    }

    /**
     * Sets the learning rate for the Network
     *
     * @param learningRate The desired learning rate
     */
    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    /**
     * Trains the network for one cycle using the attributes supplied.
     * Then back propogation is performed using the desired predictand supplied.
     *
     * @param attributes The attributes in the data used to train
     * @param predictand The desired output from the attributes
     * @return
     */
    public double[] trainNetwork(double[] attributes, double predictand) {
        double[] output = forwardPass(attributes);
        //backPropogation(predictand);

        return output;
    }

    private double[] forwardPass(double[] attributes) {
        for(int i = 0; i < input; i++){
            inputs[i+1] = attributes[i];
        }

        inputs[0] = 1.0;
        hiddens[0] = 1.0;

        for(int j = 1; j <= hidden; j++){
            hiddens[j] = 0.0;

            for(int i = 0; i <= input; i++){
                hiddens[j] += weightsIH[j][i] * inputs[i];
            }

            hiddens[j] = 1.0/(1.0+Math.exp(-hiddens[j]));
        }

        for(int j = 1; j <= output; j++){
            outputs[j] = 0.0;

            for(int i = 0; i <= hidden; i++){
                outputs[j] += weightsHO[j][i] * hiddens[i];
            }

            outputs[j] = 1.0/(1.0+Math.exp(-outputs[j]));
        }

        return outputs;
    }
}
