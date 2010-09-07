package main_package;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.Animator;

public class OpenGLManager implements GLEventListener, KeyListener, MouseListener,  MouseMotionListener {
	private float x_angle = 0.0f;
	private float y_angle = 0.0f;
	private float z_angle = 0.0f;

	private float zoom = 10.0f;
	protected final float backgroundIntensity = 1.0f;
	
	private final ObjectRenderer renderer;

	private final GLU glu = new GLU();
	private final GLCanvas canvas = new GLCanvas();
	private final Frame frame = new Frame("Jogl 3D Shape/Rotation");
	private final Animator animator = new Animator(canvas);

	public OpenGLManager(ObjectRenderer renderer) {
		this.renderer = renderer;
	}
	
	public void display(GLAutoDrawable gLDrawable) {
		final GL gl = gLDrawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -zoom);

		gl.glRotatef(x_angle, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(y_angle, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(z_angle, 0.0f, 0.0f, 1.0f);

		this.renderShape(gl);
	}

	public void renderShape(GL gl) {
		this.renderer.renderShape(gl);
	}

	public void displayChanged(GLAutoDrawable gLDrawable, boolean modeChanged, boolean deviceChanged) {}

	public void init(GLAutoDrawable gLDrawable) {
		GL gl = gLDrawable.getGL();
		gl.glShadeModel(GL.GL_SMOOTH);
		gl.glClearColor(this.backgroundIntensity, this.backgroundIntensity, this.backgroundIntensity, this.backgroundIntensity);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
		
		// call init on renderer
		this.renderer.init(gl);
		
		gLDrawable.addKeyListener(this);
		
	}

	public void reshape(GLAutoDrawable gLDrawable, int x, int y, int width, int height) {
		GL gl = gLDrawable.getGL();
		if(height <= 0) {
			height = 1;
		}
		float h = (float)width / (float)height;
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(50.0f, h, 1.0, 1000.0);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	public void exit(){
		animator.stop();
		frame.dispose();
		System.exit(0);
	}

	public void initialize() {
		canvas.addGLEventListener(this);
		frame.add(canvas);
		frame.setSize(640, 480);
		frame.setUndecorated(true);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		frame.setVisible(true);
		animator.start();
		canvas.requestFocus();
	}

	public void keyPressed(KeyEvent e) {
		this.processDefaultKeys(e);
		this.processSpecializedKeys(e);
	}

	private void processDefaultKeys(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			exit();
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			this.x_angle += 4.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			this.x_angle -= 4.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			this.y_angle += 4.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			this.y_angle -= 4.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			this.zoom -= 0.5f;
		} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
			this.zoom += 0.5f;
		}
	}

	protected void processSpecializedKeys(KeyEvent e) {
		this.renderer.processSpecializedKeys(e);
	}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		this.x_angle = arg0.getPoint().x;
		this.y_angle = arg0.getPoint().y;
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		this.x_angle = arg0.getPoint().x;
		this.y_angle = arg0.getPoint().y;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		this.x_angle = arg0.getPoint().x;
		this.y_angle = arg0.getPoint().y;
	}
}
