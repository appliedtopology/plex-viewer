package edu.stanford.math.plex_viewer.pov;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import edu.stanford.math.plex4.io.ObjectWriter;
import edu.stanford.math.plex4.metric.landmark.LandmarkSelector;
import edu.stanford.math.plex_viewer.color.ColorScheme;
import edu.stanford.math.plex_viewer.color.HSBColorScheme;
import gnu.trove.TIntHashSet;

public class LandmarkSelectorPovWriter implements ObjectWriter<LandmarkSelector<double[]>> {
	protected ColorScheme<double[]> colorScheme = null;
	protected PovScene povScene = new PovScene();
	
	@Override
	public String getExtension() {
		return "png";
	}

	@Override
	public void writeToFile(LandmarkSelector<double[]> selector, String path) throws IOException {
		this.colorScheme = new HSBColorScheme();
		
		double[][] points = selector.getUnderlyingMetricSpace().getPoints();

		TIntHashSet landmarkSet = new TIntHashSet();
		for (int index: selector.getLandmarkPoints()) {
			landmarkSet.add(index);
		}
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(path, false));

			writer.write(povScene.toString());
			PovUtility.writeDefaults(writer);
			
			for (int i = 0; i < points.length; i++) {
				PovUtility.writePointDeclaration(writer, points[i], PovUtility.getPointLabel(i));
			}
			
			for (int i = 0; i < points.length; i++) {
				if (landmarkSet.contains(i)) {
					Texture texture = Texture.getFromRGB(PovUtility.defaultPointColor);
					texture.finish.phong = 0.0;
					PovUtility.writeBox(writer, i, texture);
				} else {
					PovUtility.writeSphere(writer, i,Texture.getFromRGB(this.colorScheme.computeColor(points[i])));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
