package temp;

import com.rinearn.graph3d.RinearnGraph3D;
import com.rinearn.graph3d.renderer.simple.SimpleRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;
import com.rinearn.graph3d.renderer.RinearnGraph3DDrawingParameter;
import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.RangeConfiguration;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;


/**
 * A temporary main class in the development of this application.
 */
public class TempMain {
	private static final int SCREEN_WIDTH = 1000;
	private static final int SCREEN_HEIGHT = 860;
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;

	public static void main(String[] args) {
		System.out.println("Hello RINEARN Graph 3D Ver.6!");

		// Launch a new RINEARN Graph 3D window (to be implemented).
		RinearnGraph3D graph3D = new RinearnGraph3D();

		// Gets the rendering engine of 3D graphs.
		RinearnGraph3DRenderer renderer = graph3D.getRenderer();

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
			//param.setRangeScalingEnabled(false);
			param.setAutoColoringEnabled(false);
			param.setColor(color);
			renderer.drawPoint(x, y, z, 8.0, param);
		}

		// Draw a membrane
		MeshData meshData = generateExamMeshData();
		for (int ix=0; ix<meshData.xCount - 1; ix++) {
			for (int iy=0; iy<meshData.yCount - 1; iy++) {
				double aX = meshData.x[ix][iy];
				double aY = meshData.y[ix][iy];
				double aZ = meshData.z[ix][iy];

				double bX = meshData.x[ix + 1][iy];
				double bY = meshData.y[ix + 1][iy];
				double bZ = meshData.z[ix + 1][iy];

				double cX = meshData.x[ix + 1][iy + 1];
				double cY = meshData.y[ix + 1][iy + 1];
				double cZ = meshData.z[ix + 1][iy + 1];

				double dX = meshData.x[ix][iy + 1];
				double dY = meshData.y[ix][iy + 1];
				double dZ = meshData.z[ix][iy + 1];

				RinearnGraph3DDrawingParameter param = new RinearnGraph3DDrawingParameter();
				//param.setRangeScalingEnabled(false);
				param.setAutoColoringEnabled(false);
				float colorScalarValue = (float)((1.0 - aZ) / 2.5);
				Color color = Color.getHSBColor(colorScalarValue, 1.0f, 1.0f);
				param.setColor(color);
				renderer.drawQuadrangle(aX,aY,aZ, bX,bY,bZ, cX,cY,cZ, dX,dY,dZ, param);
			}
		}

		// Render the 3D graph.
		renderer.render();

		// !!!!!!!!!!!!!!!!!!!!
		// On the current stage, it requires to hide and re-show the graph window manually
		// to update the content of the screen (3D graph rendered the above).
		// Because RinearnGraph3D does not have screen rendering/updating thread yet.
		// -----
		// TODO: Implement the screen rendering/updating thread.
		// !!!!!!!!!!!!!!!!!!!!


		// Old code, creating a rendering engine directly and drawing graphs on a temporary window.
		oldMain1();
	}

	/**
	 * Old code, creating a rendering engine directly and drawing graphs on a temporary window.
	 */
	private static void oldMain1() {

		// Launch a temporary window for previewing the rendered images.
		TemporaryPreviewWindow tempWindow = new TemporaryPreviewWindow();

		// Instantiate a simple implementation of the rendering engine of RINEARN Graph 3D.
		SimpleRenderer renderer = new SimpleRenderer(SCREEN_WIDTH, SCREEN_HEIGHT);

		// Prepare configuration of X/Y/Z ranges.
		RangeConfiguration rangeConfig = new RangeConfiguration();
		rangeConfig.getXRangeConfiguration().setMaximum(new BigDecimal("1.0"));
		rangeConfig.getXRangeConfiguration().setMinimum(new BigDecimal("-1.0"));
		rangeConfig.getYRangeConfiguration().setMaximum(new BigDecimal("1.2"));
		rangeConfig.getYRangeConfiguration().setMinimum(new BigDecimal("-1.2"));
		rangeConfig.getZRangeConfiguration().setMaximum(new BigDecimal("1.4"));
		rangeConfig.getZRangeConfiguration().setMinimum(new BigDecimal("-1.4"));

		// Reflect the configuration.
		RinearnGraph3DConfiguration config = RinearnGraph3DConfiguration.createEmptyConfiguration();
		config.setRangeConfiguration(rangeConfig);
		renderer.setConfiguration(config);

		// Draw scales (ticks) of X/Y/Z axes.
		renderer.drawScale();

		// Draw the grid lines on the backwalls of the graph.
		renderer.drawGrid();

		// Draw the outer frame of the graph.
		renderer.drawFrame();

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
			//param.setRangeScalingEnabled(false);
			param.setAutoColoringEnabled(false);
			param.setColor(color);
			renderer.drawPoint(x, y, z, 8.0, param);
		}


		// Draw a membrane
		MeshData meshData = generateExamMeshData();
		for (int ix=0; ix<meshData.xCount - 1; ix++) {
			for (int iy=0; iy<meshData.yCount - 1; iy++) {
				double aX = meshData.x[ix][iy];
				double aY = meshData.y[ix][iy];
				double aZ = meshData.z[ix][iy];

				double bX = meshData.x[ix + 1][iy];
				double bY = meshData.y[ix + 1][iy];
				double bZ = meshData.z[ix + 1][iy];

				double cX = meshData.x[ix + 1][iy + 1];
				double cY = meshData.y[ix + 1][iy + 1];
				double cZ = meshData.z[ix + 1][iy + 1];

				double dX = meshData.x[ix][iy + 1];
				double dY = meshData.y[ix][iy + 1];
				double dZ = meshData.z[ix][iy + 1];

				RinearnGraph3DDrawingParameter param = new RinearnGraph3DDrawingParameter();
				//param.setRangeScalingEnabled(false);
				param.setAutoColoringEnabled(false);
				float colorScalarValue = (float)((1.0 - aZ) / 2.5);
				Color color = Color.getHSBColor(colorScalarValue, 1.0f, 1.0f);
				param.setColor(color);
				renderer.drawQuadrangle(aX,aY,aZ, bX,bY,bZ, cX,cY,cZ, dX,dY,dZ, param);
			}
		}

		// Rotate the graph.
		renderer.rotateAroundZ(0.24);
		renderer.rotateAroundX(-1.0);

		// Perform the rendering process.
		renderer.render();

		// Preview the rendered image.
		Image renderedImage = renderer.getScreenImage();
		tempWindow.preview(renderedImage);

		RenderingLoop renderingLoop = new RenderingLoop(renderer, tempWindow);
		Thread renderingThread = new Thread(renderingLoop);
		renderingThread.start();

		ScreenMouseListener screenMouseListener = new ScreenMouseListener(renderer, renderingLoop);
		tempWindow.addScreenMouseListener(screenMouseListener);
	}


	private static class MeshData {
		public int xCount;
		public int yCount;
		public double[][] x;
		public double[][] y;
		public double[][] z;
	}
	private static MeshData generateExamMeshData() {
		int xCount = 80 + 1;
		int yCount = 80 + 1;
		double xMin = -1.0;
		double yMin = -1.0;
		double xDelta = 2.0 / (double)(xCount - 1);
		double yDelta = 2.0 / (double)(yCount - 1);
		
		double[][] x = new double[xCount][yCount];
		double[][] y = new double[xCount][yCount];
		double[][] z = new double[xCount][yCount];

		for (int ix=0; ix<xCount; ix++) {
			for (int iy=0; iy<yCount; iy++) {
				x[ix][iy] = xMin + ix * xDelta;
				y[ix][iy] = yMin + iy * yDelta;
				z[ix][iy] = 0.5 * Math.sin(3.0 * x[ix][iy]) + 0.5 * Math.cos(2.0 * y[ix][iy]);
			}
		}
		
		MeshData meshData = new MeshData();
		meshData.xCount = xCount;
		meshData.yCount = yCount;
		meshData.x = x;
		meshData.y = y;
		meshData.z = z;
		return meshData;
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

		public void addScreenMouseListener(ScreenMouseListener screenMouseListener) {
			this.screenLabel.addMouseListener(screenMouseListener);
			this.screenLabel.addMouseMotionListener(screenMouseListener);
		}

		// Previews the specified image.
		public void preview(Image image) {
			this.screenIcon.setImage(image);
			this.screenLabel.repaint();
		}

		// Repaint the screen.
		public void repaint() {
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

	private static class RenderingLoop implements Runnable {
		private final SimpleRenderer renderer;
		private final TemporaryPreviewWindow tempWindow;
		private volatile boolean continues = true;
		private volatile boolean shouldRender = false;

		public RenderingLoop(SimpleRenderer renderer, TemporaryPreviewWindow tempWindow) {
			this.renderer = renderer;
			this.tempWindow = tempWindow;
		}

		public synchronized void renderNextTime() {
			this.shouldRender = true;
		}

		@Override
		public void run() {
			while(continues) {
				if (this.shouldRender) {
					synchronized (this) {
						this.renderer.render();
						this.tempWindow.repaint();
						this.shouldRender = false;
					}
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				}
			}
		}
	}

	private static class ScreenMouseListener extends MouseAdapter {
		private final SimpleRenderer renderer;
		private final RenderingLoop renderingLoop;

		private volatile int lastMouseX = -1;
		private volatile int lastMouseY = -1;

		public ScreenMouseListener(SimpleRenderer renderer, RenderingLoop renderingLoop) {
			this.renderer = renderer;
			this.renderingLoop = renderingLoop;
		}

		@Override
		public void mousePressed(MouseEvent me) {
			this.lastMouseX = me.getX();
			this.lastMouseY = me.getY();
		}

		@Override
		public void mouseDragged(MouseEvent me) {
			int currentMouseX = me.getX();
			int currentMouseY = me.getY();
			int dx = lastMouseX - currentMouseX;
			int dy = lastMouseY - currentMouseY;

			BufferedImage screenImage = BufferedImage.class.cast(renderer.getScreenImage());
			int rotCenterX = screenImage.getWidth()/2;
			int rotCenterY = screenImage.getHeight()/2;
			double distanceFromCenter = Math.sqrt(
					(currentMouseX - rotCenterX) * (currentMouseX - rotCenterX)
					+
					(currentMouseY - rotCenterY) * (currentMouseY - rotCenterY)					
			);

			boolean existsRadialUnitVecotr = this.existsRadialUnitVector(lastMouseX, lastMouseY, rotCenterX, rotCenterY);
			double[] radialUnitVector = null;
			if (existsRadialUnitVecotr) {
				radialUnitVector = this.computeRadialUnitVector(lastMouseX, lastMouseY, rotCenterX, rotCenterY);
			}

			double[] radialDeltaVector = this.computeRadialDeltaVector(dx, dy, existsRadialUnitVecotr, radialUnitVector);
			double   spinnerDeltaVectorLength = this.computeSpinnerDeltaVectorLength(dx, dy, existsRadialUnitVecotr, radialUnitVector, radialDeltaVector);

			// Near the rotational center, rotations by radial/spinner vectors computed above gives a little "hanged up" feelings.
			// Hence, apply simple 2-axes rotation algorithm when the mouse is near the center.
			if (distanceFromCenter < 100) {
				this.renderer.rotateAroundX(-dy * 0.005);
				this.renderer.rotateAroundY(-dx * 0.005);

			// When the mouse is far enough from the center, apply 3-axes rotation algorithm based on radial/spinner vectors.
			} else {
				this.renderer.rotateAroundX(-radialDeltaVector[Y] * 0.005);
				this.renderer.rotateAroundY(-radialDeltaVector[X] * 0.005);
				this.renderer.rotateAroundZ(spinnerDeltaVectorLength * 0.003);								
			}
			this.renderingLoop.renderNextTime();

			this.lastMouseX = currentMouseX;
			this.lastMouseY = currentMouseY;
		}

		private boolean existsRadialUnitVector(int mouseBeginX, int mouseBeginY, int rotationCenterX, int rotationCenterY) {
			boolean exists = (mouseBeginX != rotationCenterX || mouseBeginY != rotationCenterY);
			return exists;
		}

		private double[] computeRadialUnitVector(int mouseBeginX, int mouseBeginY, int rotationCenterX, int rotationCenterY) {
			if (mouseBeginX == rotationCenterX && mouseBeginY == rotationCenterY) {
				return null;
			}

			double[] radialUnitVector = new double[2];
			radialUnitVector[X] = mouseBeginX - rotationCenterX;
			radialUnitVector[Y] = mouseBeginY - rotationCenterY;
			double vectorLength =  Math.sqrt(radialUnitVector[X] * radialUnitVector[X] + radialUnitVector[Y] * radialUnitVector[Y]);
			radialUnitVector[X] /= vectorLength;
			radialUnitVector[Y] /= vectorLength;
			return radialUnitVector;
		}

		private double[] computeRadialDeltaVector(int mouseDeltaX, int mouseDeltaY, boolean existsRadialUnitVector, double[] radialUnitVector) {
			if (existsRadialUnitVector) {
				double radialInnerProduct = mouseDeltaX * radialUnitVector[X] + mouseDeltaY * radialUnitVector[Y];
				double[] radialDeltaVector = { radialInnerProduct * radialUnitVector[X], radialInnerProduct * radialUnitVector[Y] };
				return radialDeltaVector;
			} else {
				double[] radialDeltaVector = { mouseDeltaX, mouseDeltaY };
				return radialDeltaVector;
			}
		}

		private double computeSpinnerDeltaVectorLength(int mouseDeltaX, int mouseDeltaY, boolean existsRadialUnitVector, double[] radialUnitVector, double[] radialDeltaVector) {
			if (!existsRadialUnitVector) {
				return 0.0;
			}
			double[] spinnerDeltaVector = { mouseDeltaX - radialDeltaVector[X], mouseDeltaY - radialDeltaVector[Y] };
			double spinnerDeltaVectorLength = Math.sqrt(spinnerDeltaVector[X] * spinnerDeltaVector[X] + spinnerDeltaVector[Y] * spinnerDeltaVector[Y]);
			double crossProductZ = radialUnitVector[X] * spinnerDeltaVector[Y] - radialUnitVector[Y] * spinnerDeltaVector[X];
			if (crossProductZ < 0) {
				spinnerDeltaVectorLength = -spinnerDeltaVectorLength;
			}
			return spinnerDeltaVectorLength;
		}
	}
}
