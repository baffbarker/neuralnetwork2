package cave;

import cave.neuralnetwork2.NeuralNetwork;
import cave.neuralnetwork2.Transform;
import cave.neuralnetwork2.loader.Loader;
import cave.neuralnetwork2.loader.test.TestLoader;

public class App {

	public static void main(String[] args) {
		
		int inputRows = 10;
		int outputRows = 3;
		NeuralNetwork neuralnetwork = new NeuralNetwork();
		neuralnetwork.add(Transform.DENSE, 100, inputRows);
		neuralnetwork.add(Transform.RELU);
		neuralnetwork.add(Transform.DENSE, 50);
		neuralnetwork.add(Transform.DENSE, outputRows);
		neuralnetwork.add(Transform.SOFTMAX);
		
		neuralnetwork.setThreads(5);
		neuralnetwork.setEpochs(20);
		neuralnetwork.setLearningRates(0.02, 0.001);
		System.out.println(neuralnetwork);
		
		
		Loader trainLoader = new TestLoader(60_000, 32);
		Loader testLoader = new TestLoader(10_000, 32);
		
		neuralnetwork.fit(trainLoader, testLoader);
		
	}

}
