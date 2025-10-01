package cave;

import cave.neuralnetwork2.NeuralNetwork;
import cave.neuralnetwork2.Transform;
import cave.neuralnetwork2.loader.Loader;
import cave.neuralnetwork2.loader.test.TestLoader;

public class App {

	public static void main(String[] args) {
		
		String filename = "neural1.net";
		
		NeuralNetwork neuralnetwork = NeuralNetwork.load(filename);
		
		if(neuralnetwork == null) {
			System.out.println("Unable to load neural network from saved. Creating from scratch.");
			
			int inputRows = 10;
			int outputRows = 3;
			neuralnetwork = new NeuralNetwork();
			neuralnetwork.add(Transform.DENSE, 100, inputRows);
			neuralnetwork.add(Transform.RELU);
			neuralnetwork.add(Transform.DENSE, 50);
			neuralnetwork.add(Transform.DENSE, outputRows);
			neuralnetwork.add(Transform.SOFTMAX);
			
			neuralnetwork.setThreads(5);
			neuralnetwork.setEpochs(50);
			neuralnetwork.setLearningRates(0.02, 0.001);
		}
		else {
			System.out.println("Loaded from " + filename);
		}
		

		System.out.println(neuralnetwork);
		
		Loader trainLoader = new TestLoader(60_000, 32);
		Loader testLoader = new TestLoader(10_000, 32);
		
		neuralnetwork.fit(trainLoader, testLoader);
	
		if(neuralnetwork.save(filename)) {
			System.out.println("Saved to " + filename);
		}
		else {
			System.out.println("Unable to save to " + filename);
		}
		
	}

}
