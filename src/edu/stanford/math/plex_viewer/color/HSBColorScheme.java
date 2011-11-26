package edu.stanford.math.plex_viewer.color;

import java.awt.Color;

/**
 * This class implements a color scheme on Euclidean space which uses the HSB (hue, saturation,
 * brightness) color system. 
 * 
 * @author Andrew Tausz
 *
 */
public class HSBColorScheme extends ColorScheme<double[]> {
	public HSBColorScheme() {}
	
	private static HSBColorScheme instance = new HSBColorScheme();
	
	public static HSBColorScheme getInstance() {
		return instance;
	}
	
	
	public float[] computeColor(double[] point) {

		assert(point.length == 2 || point.length == 3);

		if (point.length == 2) {
			double theta = Math.atan2(point[1], point[0]);
			double r = point[0]*point[0] + point[1]*point[1];

			float hue = (float) (theta * (0.5 / Math.PI));
			float saturation = (float) (0.5 + 0.25 * (1 + Math.cos(r)));;
			float brightness = 0.9f;

			return toRGB(hue, saturation, brightness);
			
		} else {
			double[] sphericalCoords = this.toSphericalCoordinates(point);

			float hue = ((float)(sphericalCoords[2] * (0.5 / Math.PI)) + (float)(sphericalCoords[1] * (0.7 / Math.PI)));
			float saturation = (float) (0.5 + 0.25 * (1 + Math.cos(sphericalCoords[0])));
			float brightness = (float) (0.5 + 0.25 * (1 + Math.sin(sphericalCoords[1])));

			return toRGB(hue, saturation, brightness);
		}
	}

	/**
	 * This function converts from HSB format to RGB format.
	 * 
	 * @param hue
	 * @param saturation
	 * @param brightness
	 * @return
	 */
	private float[] toRGB(float hue, float saturation, float brightness) {
		float[] rgb = new float[3];
		int rbg_int_value = Color.HSBtoRGB(hue, saturation, brightness);
		Color c = new Color(rbg_int_value);
		rgb = c.getRGBColorComponents(rgb);
		return rgb;
	}

	private double[] toSphericalCoordinates(double[] point) {
		double[] sphericalCoordinates = new double[3];
		double[] p = new double[3];
		for (int i = 0; i < point.length; i++) {
			p[i] = point[i];
		}
		sphericalCoordinates[0] = Math.sqrt(p[0] * p[0] + p[1] * p[1] + p[2] * p[2]);
		sphericalCoordinates[1] = Math.acos(p[2] / sphericalCoordinates[0]);
		sphericalCoordinates[2] = Math.atan2(p[1], p[0]);
		return sphericalCoordinates;
	}

}
