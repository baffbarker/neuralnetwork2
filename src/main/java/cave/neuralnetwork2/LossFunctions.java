package cave.neuralnetwork2;

import cave.matrix.Matrix;

public class LossFunctions {

	public static Matrix crossEntropy(Matrix expected, Matrix actual) {
		return actual.apply((index, value)->{
			return -expected.get(index) * Math.log(value);
		}).sumColumns();
		
	}
}
