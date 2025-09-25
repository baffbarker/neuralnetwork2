package cave.neuralnetwork2;

import java.util.Random;

import cave.matrix.Matrix;

public class Util {
	
	private static Random random = new Random();
	public static Matrix generateInputMatrix(int rows, int numberItems) {
		return new Matrix(rows, numberItems, i->random.nextGaussian());
	}
	
	public static TrainingMatrixes generateTrainingMatrixes(int inputSize, int outputSize, int cols) {
		
		var io = generateTrainingArrays(inputSize, outputSize, cols);
		
		Matrix input = new Matrix(inputSize, cols, io.getInput());
		Matrix output = new Matrix(outputSize, cols, io.getOutput());
		
		
		
		return new TrainingMatrixes(input, output);
	}
	
	

	public static TrainingArrays generateTrainingArrays(int inputRows, int outputRows, int cols) {
		double[] input = new double[inputRows * cols];
		double[] output = new double[outputRows * cols];
		
		int inputPos = 0;
		int outputPos = 0;
		
		for(int col = 0; col < cols; col++) {
			int radius = random.nextInt(outputRows);
			
			double[] values = new double[inputRows];
			
			double initialRadius = 0;
			
			for(int row = 0; row < inputRows; row++) {
				double value = random.nextGaussian();
				values[row] = value;
				initialRadius += value * value;
			}
			
			initialRadius = Math.sqrt(initialRadius);
			
			for(int row = 0; row < inputRows; row++) {
				input[inputPos++] = values[row] * radius/initialRadius;
			}
			output[outputPos + radius] = 1;
			
			outputPos += outputRows;
		}
		
		return new TrainingArrays(input, output);
	}
	
	
	public static Matrix generateExpectedMatrix(int rows, int cols) {
		Matrix expected = new Matrix(rows, cols, i->0);
		
		for(int col = 0; col < cols; col++) {
			int randomRow = random.nextInt(rows);
			
			expected.set(randomRow, col, 1);
		}
		
		return expected;
	}

	public static Matrix generateTrainableExpectedMatrix(int outputRows, Matrix input) {
		Matrix expected = new Matrix(outputRows, input.getCols());
		
		Matrix columnSums = input.sumColumns();
		
		columnSums.forEach((row, col, value)->{
			int rowIndex = (int)(outputRows * (Math.sin(value) + 1.0)/2.0);
			
			expected.set(rowIndex, col, 1);
			
		});
		
		return expected;
	}
}
