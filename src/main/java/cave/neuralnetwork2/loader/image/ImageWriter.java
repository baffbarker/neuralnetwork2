package cave.neuralnetwork2.loader.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cave.neuralnetwork2.loader.BatchData;
import cave.neuralnetwork2.loader.Loader;
import cave.neuralnetwork2.loader.MetaData;

public class ImageWriter {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("usage: [app] <MNIST DATA DIRECTORY>");
			return;
		}

		String directory = args[0];

		if (!new File(directory).isDirectory()) {
			System.out.println("'" + directory + "' is not a directory");
			return;
		}

		new ImageWriter().run(directory);

	}

	public void run(String directory) {
		
		final String trainImages = String.format("%s%s%s", directory, File.separator, "train-images-idx3-ubyte");
		final String trainLabels = String.format("%s%s%s", directory, File.separator, "train-labels-idx1-ubyte");
		final String testImages = String.format("%s%s%s", directory, File.separator, "t10k-images-idx3-ubyte");
		final String testLabels = String.format("%s%s%s", directory, File.separator, "t10k-labels-idx1-ubyte");

		int batchSize = 900;
		
		Loader trainLoader = new ImageLoader(trainImages, trainLabels, batchSize);
		Loader testLoader = new ImageLoader(testImages, testLabels, batchSize);

		trainLoader.open();
		MetaData metaData = testLoader.open();

		for (int i = 0; i < metaData.getNumberBatches(); i++) {
			BatchData batchData = testLoader.readBatch();
			
			String montagePath = String.format("montage%d.jpg", i);
			System.out.println("Writing " + montagePath);
			
			var montage = new BufferedImage(900, 900, BufferedImage.TYPE_BYTE_GRAY);
			
			try {
				ImageIO.write(montage, "jpg", new File(montagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		trainLoader.close();
		testLoader.close();
	}

}
