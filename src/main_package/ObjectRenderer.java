package main_package;

import java.awt.event.KeyEvent;

import javax.media.opengl.GL;

public interface ObjectRenderer {
	void init(GL gl);
	void renderShape(GL gl);
	void processSpecializedKeys(KeyEvent e);
}
