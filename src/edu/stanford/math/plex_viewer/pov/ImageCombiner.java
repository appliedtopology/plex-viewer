package edu.stanford.math.plex_viewer.pov;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;

/**
 * 
 * 
 * @author Tim Harrington
 * @date Apr 30, 2009
 */
public class ImageCombiner {

	/**
	 * Don't allow this object to be created.
	 */
	private ImageCombiner() {
	}

	protected static BufferedImage loadImage(String filepath) throws IOException {
		ImageIO.setUseCache(false);
		int tryCount = 0;
		BufferedImage img = null;
		while (tryCount < 3) {
			img = ImageIO.read(new File(filepath));
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (img != null) {
				break;
			}
			tryCount++;
		}
		if (img == null) {
			throw new IOException("there was a problem loading " + filepath);
		}
		return img;
	}

	public static void combineImages(String[] filepaths, String filepath,
			String title) {
		try {
			if (filepaths.length == 0)
				return;
			BufferedImage image = combineVertically(filepaths, title);
			try {
				saveImage(image, filepath);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected static BufferedImage combineVertically(String[] filepaths,
			String title) throws IOException {
		BufferedImage[] images = new BufferedImage[filepaths.length];

		// an array of y positions to place the images
		int[] ypos = new int[filepaths.length];
		// dimensions
		int titleOffset = 20;
		int totalHeight = titleOffset;
		int totalWidth = 0;
		// housekeeping
		int counter = 0;
		BufferedImage cur;

		// load all the images and compute positions
		for (String filepath : filepaths) {
			// load the next image
			try {
				cur = loadImage(filepath);
			} catch (IOException e) {
				throw new RuntimeException("there was a problem loading "
						+ filepath);
			}

			// place the next image at the end position
			ypos[counter] = totalHeight;

			// update the total sizes
			if (cur == null) {
				throw new IllegalStateException("why does this happen?!");
			}
			if (cur.getWidth() > totalWidth) {
				totalWidth = cur.getWidth();
			}
			totalHeight += cur.getHeight();

			// save the image
			images[counter] = cur;
			counter++;
		}

		totalWidth = Math.max(500, totalWidth);
		BufferedImage combinedImage = new BufferedImage(totalWidth,
				totalHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = combinedImage.createGraphics();
		graphics2D.setColor(new Color(255, 255, 255));
		graphics2D.fillRect(0, 0, totalWidth, totalHeight);
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for (int i = 0; i < images.length; i++) {
			graphics2D.drawImage(images[i], 0, ypos[i], images[i].getWidth(),
					images[i].getHeight(), null);
		}
		graphics2D.setColor(new Color(0, 0, 0));
		graphics2D.setFont(new Font("Arial", Font.PLAIN, 14));
		graphics2D.drawString(title, titleOffset, titleOffset);
		return combinedImage;
	}

	protected static void saveImage(BufferedImage image, String filepath)
	throws IOException {
		BufferedOutputStream out;
		out = new BufferedOutputStream(new FileOutputStream(filepath));
		ImageEncoder encoder;
		if (System.getProperty("os.name").equals("Linux")) {
			encoder = ImageCodec.createImageEncoder("PNG",out,null);
		} else {
			encoder = ImageCodec.createImageEncoder("JPEG",out,null);
		}
		try {
			encoder.encode(image);
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			out.close();
		}
	}

}