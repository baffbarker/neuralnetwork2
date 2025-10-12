package cave.neuralnetwork.loader.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cave.neuralnetwork.loader.BatchData;
import cave.neuralnetwork.loader.Loader;
import cave.neuralnetwork.loader.MetaData;

public class ImageWriter {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("usage: [app] <MNIST DATA DIRECTORY>");
			return;
		}

		File dir = new File(args[0]);

		if (!dir.isDirectory()) {
			try {
				System.out.println(dir.getCanonicalPath() + " is not a directory.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return;
		}

		String directory = args[0];
		
		new ImageWriter().run(directory);
	}
	
	public void run(String directory) {
		final String trainImages = String.format("%s%s%s", directory, File.separator, "train-images-idx3-ubyte");
		final String trainLabels = String.format("%s%s%s", directory, File.separator, "train-labels-idx1-ubyte");
		final String testImages = String.format("%s%s%s", directory, File.separator, "t10k-images-idx3-ubyte");
		final String testLabels = String.format("%s%s%s", directory, File.separator, "t10k-labels-idx1-ubyte");

		int batchSize = 900;
		
		ImageLoader trainLoader = new ImageLoader(trainImages, trainLabels, batchSize);
		ImageLoader testLoader = new ImageLoader(testImages, testLabels, batchSize);
		
		ImageLoader loader = testLoader;

		ImageMetaData metaData = loader.open();
		
		int imageWidth = metaData.getWidth();
		int imageHeight = metaData.getHeight();
		
		
		for (int i = 0; i < metaData.getNumberBatches(); i++) {
			BatchData batchData = testLoader.readBatch();
			
			var numberImages = metaData.getItemsRead();
			
			int horizontalImages = (int)Math.sqrt(numberImages);
			
			while(numberImages % horizontalImages != 0) {
				++horizontalImages;
			}
			
			int verticalImages = numberImages / horizontalImages;
			
			int canvasWidth = horizontalImages * imageWidth;
			int canvasHeight = verticalImages * imageHeight;
			
			String montagePath = String.format("montage%d.jpg", i);
			System.out.println("Writig " + montagePath);
			
			var montage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_BYTE_GRAY);
			
			double[] pixelData = batchData.getInputBatch();
			
			int imageSize = imageWidth * imageHeight;
			
			for(int pixelIndex = 0; pixelIndex < pixelData.length; pixelIndex++) {
				int imageNumber = pixelIndex/imageSize;
				int pixelNumber = pixelIndex % imageSize;
				
				int montageRow = imageNumber / horizontalImages;
				int montageCol = imageNumber % horizontalImages;
				
				int pixelRow = pixelNumber / imageWidth;
				int pixelCol = pixelNumber % imageWidth;
				
				int x = montageCol * imageWidth + pixelCol;
				int y = montageRow * imageHeight + pixelRow;
				
				double pixelValue = pixelData[pixelIndex];
				int color = (int)(0x100 * pixelValue);
				int pixelColor = (color << 18) + (color << 8) + color;
				
				montage.setRGB(x, y, pixelColor);
			}
			
			try {
				ImageIO.write(montage, "jpg", new File(montagePath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		loader.close();
	}
}
