package matrix;


import org.junit.Test;

import cave.matrix.Matrix;

public class MatrixTest {

	@Test
	public void constructionTest() {
		Matrix m = new Matrix(2, 4, i->i*2);
	}
	

}
