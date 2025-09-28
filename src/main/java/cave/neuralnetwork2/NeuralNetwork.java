package cave.neuralnetwork2;

import cave.neuralnetwork2.loader.Loader;

public class NeuralNetwork {
	private Engine engine;
	
	private int epochs = 20;
	private double learningRate;
	private double initialLearningRate = 0.01;
	private double finalLearningRate = 0;
	
	public NeuralNetwork() {
		engine = new Engine();
	}
	
	public void add(Transform transform, double... params) {
		engine.add(transform, params);
	}
	
	public void setLearningRates(double initialLearningRate, double finalLearningRate) {
		this.initialLearningRate = initialLearningRate;
		this.finalLearningRate = finalLearningRate;
	}
	
	public void setEpochs(int Epochs) {
		this.epochs = epochs;
	}
	
	public void fit(Loader trainLoader, Loader evalLoader) {
		
		learningRate = initialLearningRate;
		
		for(int epoch = 0; epoch < epochs; epoch++) {
			
			System.out.printf("Epoch %3d ", epoch);
			
			runEpoch(trainLoader, true);
			
			if(evalLoader != null) {
				runEpoch(evalLoader, false);
			}
			
			learningRate -= (initialLearningRate -finalLearningRate)/epochs;
		}
	}

	private void runEpoch(Loader loader, boolean trainingMode) {
		
		loader.open();
		
		var queue = createBatchTasks(loader, trainingMode);
		consumeBatchTasks(queue, trainingMode);
		
		loader.close();
	}

	private void consumeBatchTasks(Object queue, boolean trainingMode) {
		// TODO Auto-generated method stub
		
	}

	private Object createBatchTasks(Loader loader, boolean trainingMode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return engine.toString();
	}
	
	
}
