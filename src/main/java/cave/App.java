package cave;

import cave.neuralnetwork2.NeuralNetwork;
import cave.neuralnetwork2.Transform;

public class App {

	public static void main(String[] args) {
		
		int inputRows = 10;
		int outputRows = 3;
		NeuralNetwork neuralnetwork = new NeuralNetwork();
		neuralnetwork.add(Transform.DENSE, 100, inputRows);
		neuralnetwork.add(Transform.RELU);
		neuralnetwork.add(Transform.DENSE, outputRows);
		neuralnetwork.add(Transform.SOFTMAX);
		
		
		System.out.println(neuralnetwork);
	}

}
