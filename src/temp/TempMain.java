package temp;

import com.rinearn.graph3d.renderer.simple.SimpleRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;


/**
 * A temporary main class in the development of this application.
 */
public class TempMain {
	private static final int SCREEN_WIDTH = 1000;
	private static final int SCREEN_HEIGHT = 800;

	public static void main(String[] args) {
		System.out.println("Hello RINEARN Graph 3D Ver.6!");

		// Launch a temporary window for previewing the rendered images.
		TemporaryPreviewWindow tempWindow = new TemporaryPreviewWindow();

		// Instantiate a simple implementation of the rendering engine of RINEARN Graph 3D.
		SimpleRenderer renderer = new SimpleRenderer(SCREEN_WIDTH, SCREEN_HEIGHT);

		// Draw lines composing a box frame.
		{
			// Create an object storing the value of the drawing parameters.
			RinearnGraph3DDrawingParameter param = new RinearnGraph3DDrawingParameter();
			param.setRangeScalingEnabled(false);
			param.setAutoColoringEnabled(false);
			param.setColor(Color.LIGHT_GRAY);
			double lineWidth = 2.0; // [px]

			// Draw the upper rectangle.
			renderer.drawLine(1.0, 1.0, 1.0,   -1.0, 1.0, 1.0, lineWidth, param);
			renderer.drawLine(-1.0, 1.0, 1.0,   -1.0, -1.0, 1.0, lineWidth, param);
			renderer.drawLine(-1.0, -1.0, 1.0,   1.0, -1.0, 1.0, lineWidth, param);
			renderer.drawLine(1.0, -1.0, 1.0,   1.0, 1.0, 1.0, lineWidth, param);

			// Draw the lower rectangle.
			renderer.drawLine(1.0, 1.0, -1.0,   -1.0, 1.0, -1.0, lineWidth, param);
			renderer.drawLine(-1.0, 1.0, -1.0,   -1.0, -1.0, -1.0, lineWidth, param);
			renderer.drawLine(-1.0, -1.0, -1.0,   1.0, -1.0, -1.0, lineWidth, param);
			renderer.drawLine(1.0, -1.0, -1.0,   1.0, 1.0, -1.0, lineWidth, param);

			// Draw the "pillars" between the upper rectangle and the lower rectangle.
			renderer.drawLine(1.0, 1.0, 1.0,   1.0, 1.0, -1.0, lineWidth, param);
			renderer.drawLine(-1.0, 1.0, 1.0,   -1.0, 1.0, -1.0, lineWidth, param);
			renderer.drawLine(-1.0, -1.0, 1.0,   -1.0, -1.0, -1.0, lineWidth, param);
			renderer.drawLine(1.0, -1.0, 1.0,   1.0, -1.0, -1.0, lineWidth, param);
		}
	
	
		// Draw many points.
		int n = 100;
		for (int i=0; i<n; i++) {

			// Get a color from the HSB color gradient.
			float colorScalarValue = i / (float)(n - 1);
			Color color = Color.getHSBColor(colorScalarValue, 1.0f, 1.0f);

			// Prepare he coordinate values of the point.
			double theta = 6.0 * Math.PI * i / (double)(n - 1);
			double x = Math.cos(theta);
			double y = Math.sin(theta);
			double z = 2.0 * i / (double)(n - 1) - 1.0;

			// Draw the point.
			RinearnGraph3DDrawingParameter param = new RinearnGraph3DDrawingParameter();
			param.setRangeScalingEnabled(false);
			param.setAutoColoringEnabled(false);
			param.setColor(color);
			renderer.drawPoint(x, y, z, 8.0, param);
		}

		// Perform the rendering process.
		renderer.render();

		// Preview the rendered image.
		Image renderedImage = renderer.getScreenImage();
		tempWindow.preview(renderedImage);
	}


	// A temporary window class for previewing the rendered images.
	private static class TemporaryPreviewWindow {
		private JFrame frame;
		private JLabel screenLabel;
		private ImageIcon screenIcon;

		public TemporaryPreviewWindow() {

			// Initialize GUI components on the event dispatcher thread.
			try {
				SwingUtilities.invokeAndWait(new Initializer());
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

		// Previews the specified image.
		public void preview(Image image) {
			this.screenIcon.setImage(image);
			this.screenLabel.repaint();
		}

		// The Runnable impl for initializing GUI components on the event dispatcher thread.
		private class Initializer implements Runnable {
			@Override
			public void run() {
				
				// Create the frame of the window.
				frame = new JFrame();
				frame.setTitle("Temporary Preview Window");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT + 20);

				// Create the screen.
				screenLabel = new JLabel();
				screenIcon = new ImageIcon();
				screenLabel.setIcon(screenIcon);
				frame.getContentPane().add(screenLabel);

				// Show the window.
				screenLabel.setVisible(true);
				frame.setVisible(true);
			}
		}
	}
}