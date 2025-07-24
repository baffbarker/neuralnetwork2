package cave.neuralnetwork2;

import static org.junit.Assert.*;

import org.junit.Test;

import cave.matrix.Matrix;

public class NeuralNetTest {

	@Test
	public void test() {
		
		Matrix input = new Matrix(3, 1, i->(i+1));
		Matrix weights = new Matrix(3, 3, i->(i+1));
		Matrix biases = new Matrix(3, 1, i->(i+1));
		
		Matrix result = weights.multiply(input).modify((row, col, value)->value + biases.get(row));
		
		System.out.println(input);
		System.out.println(weights);
		System.out.println(biases);
		System.out.println(result);
		
	}

}
