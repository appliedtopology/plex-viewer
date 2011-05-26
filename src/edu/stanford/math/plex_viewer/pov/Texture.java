package edu.stanford.math.plex_viewer.pov;


public class Texture {

	public class Pigment {
		float[] rgb = new float[]{0.0f, 0.0f, 0.0f};

		@Override
		public String toString() {
			return "pigment { color rgb" + PovUtility.toPovString(rgb) + "}";
		}
	}

	public class Finish {
		double phong = 0.7;
		double phong_size = 250;
		
		@Override
		public String toString() {
			return "finish { phong " + phong + " phong_size " + phong_size + "}";
		}
	}

	Pigment pigment = new Pigment();
	Finish finish = new Finish();

	@Override
	public String toString() {
		return "texture { " + pigment + " " + finish + "}";
	}

	public static Texture getFromRGB(float[] rgb) {
		Texture texture = new Texture();
		texture.pigment.rgb = rgb;
		return texture;
	}
}
