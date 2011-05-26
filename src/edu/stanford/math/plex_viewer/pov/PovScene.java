package edu.stanford.math.plex_viewer.pov;

import java.util.ArrayList;
import java.util.List;

public class PovScene {
	class Camera {
		int[] location = new int[]{0, 0, -4};
		int[] look_at = new int[]{0, 0, 0};

		@Override
		public String toString() {
			return "camera { location " + PovUtility.toPovString(location)	+ " look_at " + PovUtility.toPovString(look_at) + "}";
		}
	}
	
	class LightSource {
		int[] location = new int[]{1, 1, -10};
		String color = "White";
		@Override
		public String toString() {
			return "light_source {" + PovUtility.toPovString(location) + " color " + color + " }";
		}
	}
	
	class Background {
		String color = "White";

		@Override
		public String toString() {
			return "background { color rgb" + PovUtility.toPovString(PovUtility.defaultBackgroundColor) + " }";
		}
	}
	
	List<String> includes = new ArrayList<String>();
	Camera camera = new Camera();
	List<LightSource> lightSources = new ArrayList<LightSource>();
	Background background = new Background();
	
	public PovScene() {
		this.lightSources.add(new LightSource());
		includes.add("colors.inc");
		includes.add("textures.inc");
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		for (String include: includes) {
			builder.append("#include \"");
			builder.append(include);
			builder.append("\"\n");
		}
		
		builder.append(camera.toString());
		builder.append("\n");
		builder.append(background.toString());
		builder.append("\n");
		
		for (LightSource lightSource: lightSources) {
			builder.append(lightSource.toString());
			builder.append("\n");
		}
		
		return builder.toString();
	}
	
}
