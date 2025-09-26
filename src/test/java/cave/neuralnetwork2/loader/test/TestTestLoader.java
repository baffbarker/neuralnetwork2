package cave.neuralnetwork2.loader.test;

import static org.junit.Assert.*;

import org.junit.Test;

import cave.neuralnetwork2.loader.BatchData;
import cave.neuralnetwork2.loader.Loader;
import cave.neuralnetwork2.loader.MetaData;

public class TestTestLoader {

	@Test
	public void test() {

		int batchSize = 32;
		
		Loader testLoader = new TestLoader(60_000, 32);
		
		MetaData metaData = testLoader.open();
		
		for(int i= 0; i < metaData.getNumberBatches(); i++) {
			BatchData batchData = testLoader.readBatch();
			
			assertTrue(batchData != null);
			
			int itemsRead = metaData.getItemsRead();
			
			assertTrue(itemsRead == batchSize);
		}
	}

}
