package cave.neuralnetwork2;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import cave.matrix.Matrix;

public class NeuralNetTest {
	private Random random = new Random();

	@Test
	void testTemp() {
		
		int inputSize = 5;
		int layer1Size = 6;
		int layer2Size = 4;
		
		Matrix input = new Matrix(inputSize, 1, i->random.nextGaussian());
		
		Matrix layer1Weights = new Matrix(layer1Size, input.getRows(), i->random.nextGaussian());
		Matrix layer1biases = new Matrix(layer1Size, 1, i->random.nextGaussian());

		Matrix layer2Weights = new Matrix(layer2Size, layer1Weights.getRows(), i->random.nextGaussian());
		Matrix layer2biases = new Matrix(layer2Size, 1, i->random.nextGaussian());
	}
	
	@Test
	public void testAddBias() {

		Matrix input = new Matrix(3, 3, i -> (i + 1));
		Matrix weights = new Matrix(3, 3, i -> (i + 1));
		Matrix biases = new Matrix(3, 1, i -> (i + 1));

		Matrix result = weights.multiply(input).modify((row, col, value) -> value + biases.get(row));

		double[] expectedValues = { +31.00000, +37.00000, +43.00000, +68.00000, +83.00000, +98.00000, +105.00000,
				+129.00000, +153.00000 };

		Matrix expected = new Matrix(3, 3, i->expectedValues[i]);
		
		assertTrue(expected.equals(result));
	}

	@Test
	public void testRelu() {
		
		final int numberNeurons = 5;
		final int numberInputs = 6;
		final int inputSize = 4;

		Matrix input = new Matrix(inputSize, numberInputs, i -> random.nextDouble());
		Matrix weights = new Matrix(numberNeurons, inputSize, i -> random.nextGaussian());
		Matrix biases = new Matrix(numberNeurons, 1, i -> random.nextGaussian());

		Matrix result1 = weights.multiply(input).modify((row, col, value) -> value + biases.get(row));
		Matrix result2 = weights.multiply(input).modify((row, col, value) -> value + biases.get(row)).modify(value -> value > 0 ? value: 0);

		result2.forEach((index, value)->{
			double originalValue = result1.get(index);
			
			if(originalValue > 0) {
				assertTrue(Math.abs(originalValue - value) < 0.000001);
			}
			else {
				assertTrue(Math.abs(value) < 0.000001);
			}
		});
		
	}
	
	
	
}
