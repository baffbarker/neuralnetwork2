package cave.neuralnetwork2;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import cave.matrix.Matrix;
import cave.neuralnetwork2.loader.BatchData;
import cave.neuralnetwork2.loader.Loader;
import cave.neuralnetwork2.loader.MetaData;

public class NeuralNetwork {
	private Engine engine;

	private int epochs = 20;
	private double learningRate;
	private double initialLearningRate = 0.01;
	private double finalLearningRate = 0;
	private Object lock = new Object();

	private int threads = 2;

	public NeuralNetwork() {
		engine = new Engine();
	}
	
	public void setThreads(int threads) {
		this.threads = threads;
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

		for (int epoch = 0; epoch < epochs; epoch++) {

			System.out.printf("Epoch %3d ", epoch);

			runEpoch(trainLoader, true);

			if (evalLoader != null) {
				runEpoch(evalLoader, false);
			}
			
			System.out.println();

			learningRate -= (initialLearningRate - finalLearningRate) / epochs;
		}
	}

	private void runEpoch(Loader loader, boolean trainingMode) {

		loader.open();

		var queue = createBatchTasks(loader, trainingMode);
		consumeBatchTasks(queue, trainingMode);

		loader.close();
	}

	private void consumeBatchTasks(LinkedList<Future<BatchResult>> batches, boolean trainingMode) {
		
		var numberBatches = batches.size();
		
		int index = 0;
		
		for(var batch: batches) {
			try {
				var batchResult = batch.get();
			} catch (Exception e) {
				throw new RuntimeException("Execution error: ", e);
			} 
			
			int printDot = numberBatches/30;
			
			if(trainingMode && index++ % printDot == 0) {
				System.out.print(".");
			}
		}

		
	}

	private LinkedList<Future<BatchResult>> createBatchTasks(Loader loader, boolean trainingMode) {

		LinkedList<Future<BatchResult>> batches = new LinkedList<>();
		
		MetaData metaData = loader.getMetaData();
		int numberBatches = metaData.getNumberBatches();
		
		var executor = Executors.newFixedThreadPool(threads);

		for (int i = 0; i < numberBatches; i++) {
			batches.add(executor.submit(()->runBatch(loader, trainingMode)));
		}
		
		executor.shutdown();
		
		return batches;
	}

	private BatchResult runBatch(Loader loader, boolean trainingMode) {

		MetaData metaData = loader.open();

		BatchData batchData = loader.readBatch();

		int itemsRead = metaData.getItemsRead();

		int inputSize = metaData.getInputSize();
		int expectedSize = metaData.getExpectedSize();

		Matrix input = new Matrix(inputSize, itemsRead, batchData.getInputBatch());
		Matrix expected = new Matrix(expectedSize, itemsRead, batchData.getExpectedBatch());

		BatchResult batchResult = engine.runForwards(input);

		if (trainingMode) {
			engine.runBackwards(batchResult, expected);

			synchronized (lock) {
				engine.adjust(batchResult, learningRate);
			}
		} 
		else {
			engine.evaluate(batchResult, expected);
		}

		return batchResult;

	}

	@Override
	public String toString() {
		return engine.toString();
	}

}
