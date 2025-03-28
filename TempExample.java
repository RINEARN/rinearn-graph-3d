import com.rinearn.graph3d.RinearnGraph3D;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.range.RangeConfiguration;
import com.rinearn.graph3d.config.color.ColorConfiguration;
import com.rinearn.graph3d.config.color.GradientColor;

import com.rinearn.graph3d.event.RinearnGraph3DPlottingEvent;
import com.rinearn.graph3d.event.RinearnGraph3DPlottingListener;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.math.BigDecimal;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenu;
import javax.swing.JMenuBar;


/**
 * A temporary example code using RINEARN Graph 3D as a library.
 * (For development and debugging)
 */
public class TempExample {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello RINEARN Graph 3D Ver.6!");

		TempExample tempMain = new TempExample();
		tempMain.init();
	}

	public void init() throws Exception {

		// Launch a new RINEARN Graph 3D window (to be implemented).
		RinearnGraph3D graph3D = new RinearnGraph3D();

		// Plot a sample data file: line plot
		File linePlotSampleDataFile = new File("./sample/SampleDataFile/LinePlotSample.txt");
		graph3D.openDataFile(linePlotSampleDataFile);

		/*
		// Plot a sample data file: surface plot
		File surfacePlotSampleDataFile = new File("./sample/SampleDataFile/SurfacePlotSample.txt");
		graph3D.openDataFile(surfacePlotSampleDataFile);
		*/

		// Copy the image to the clipboard.
		try {
			
			// あれ、なんで右クリックメニューからだと ARGB は警告でるのに、ここだと ARGB でも出ないの…？ また後々で要検証
			boolean transfersToClioboard = true;
			//graph3D.copyImage(BufferedImage.TYPE_INT_RGB, transfersToClioboard);
			graph3D.copyImage(BufferedImage.TYPE_INT_ARGB, transfersToClioboard);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		// Save the image as a file.
		try {
			graph3D.exportImageFile(new File("./testimage.png"), 1.0);
			graph3D.exportImageFile(new File("./testimage.jpg"), 1.0);
			graph3D.exportImageFile(new File("./testimage.bmp"), 1.0);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/*
	public void __OLD_init() throws Exception {

		// Launch a new RINEARN Graph 3D window (to be implemented).
		RinearnGraph3D graph3D = new RinearnGraph3D();

		// Hide the menu bar.
		//graph3D.setMenuVisible(false);

		// Hide UI-panel at the left side of the screen.
		graph3D.setScreenSideUIVisible(false);

		// Set the location and size of the graph window.
		graph3D.setWindowBounds(200, 50, 860, 730);

		// Set the screen size.
		graph3D.setScreenSize(1000, 730);

		// Set the ranges of X/Y/Z axes.
		//graph3D.setXRange(-2.0, 2.0);
		//graph3D.setYRange(-1.2, 1.2);
		//graph3D.setZRange(-1.0, 1.5);

		RangeConfiguration rangeConfig = new RangeConfiguration();
		rangeConfig.getXRangeConfiguration().setMinimum(new BigDecimal("-2.0"));
		rangeConfig.getXRangeConfiguration().setMaximum(new BigDecimal("2.0"));
		rangeConfig.getYRangeConfiguration().setMinimum(new BigDecimal("-1.2"));
		rangeConfig.getYRangeConfiguration().setMaximum(new BigDecimal("1.2"));
		rangeConfig.getZRangeConfiguration().setMinimum(new BigDecimal("-1.0"));
		rangeConfig.getZRangeConfiguration().setMaximum(new BigDecimal("1.5"));
		RinearnGraph3DConfiguration config = RinearnGraph3DConfiguration.createEmptyConfiguration();
		config.setRangeConfiguration(rangeConfig);
		graph3D.configure(config);

		// Set the ticks of X axis.
		graph3D.setXTicks(
			new double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0 },
			new String[] { "-2", "-1.5", "-1", "-0.5", "0", "0.5", "1", "1.5", "2" }
		);

		// Set the ticks of Y axis.
		graph3D.setYTicks(
			new double[] { -1.2, -1.0, -0.8, -0.6, -0.4, -0.2, 0.0, 0.2, 0.4, 0.6, 0.8, 1.0, 1.2 },
			new String[] { "-1.2", "-1.0", "-0.8", "-0.6", "-0.4", "-0.2", "0.0", "0.2", "0.4", "0.6", "0.8", "1.0", "1.2" }
		);

		// Set the ticks of Z axis.
		graph3D.setZTicks(
			new double[] { -1.0, -0.5, 0.0, 0.5, 1.0, 1.5 },
			new String[] { "-1.0", "-0.5", "0.0", "0.5", "1.0", "1.5" }
		);

		// Set the axis labels of X/Y/Z axes.
		graph3D.setXLabel("Lx");
		graph3D.setYLabel("Ly");
		graph3D.setZLabel("|E(z)|");

		// Set the camera angle.
		graph3D.setZZenithCameraAngle(0.4, 1.0, 0.0);

		// Set the camera distance and the magnification.
		graph3D.setCameraDistance(5.25);
		graph3D.setCameraMagnification(800.0);

		// Gets the rendering engine of 3D graphs.
		//RinearnGraph3DRenderer renderer = graph3D.getRenderer();

		// Create the plotter, which plot contents using the above renderer.
		//Plotter plotter = new Plotter(renderer);

		// Register the above plotter to the graph, and perform the first plotting.
		//graph3D.addPlottingListener(plotter);
		//graph3D.plot();

		MeshData meshData = this.generateExamMeshData();
		//graph3D.appendData(meshData.x, meshData.y, meshData.z);
		//graph3D.setData(meshData.x, meshData.y, meshData.z);

		LineData lineData = this.generateExamLineData();
		//graph3D.appendData(lineData.x, lineData.y, lineData.z);
		//graph3D.setData(lineData.x, lineData.y, lineData.z);

		double[][][] multiDataSeriesX = new double[][][] { meshData.x, new double[][]{lineData.x} };
		double[][][] multiDataSeriesY = new double[][][] { meshData.y, new double[][]{lineData.y} };
		double[][][] multiDataSeriesZ = new double[][][] { meshData.z, new double[][]{lineData.z} };
		//graph3D.appendData(multiDataSeriesX, multiDataSeriesY, multiDataSeriesZ);
		//graph3D.setData(multiDataSeriesX, multiDataSeriesY, multiDataSeriesZ);

		//graph3D.setAsynchronousPlottingEnabled(true);
		//long beginTime = System.nanoTime();
		//for (int i=0; i<100; i++) {
		//	graph3D.setData(multiDataSeriesX, multiDataSeriesY, multiDataSeriesZ);
		//}
		//long endTime = System.nanoTime();
		//double requiredTime = (endTime - beginTime) * 1.0E-9;
		//System.out.println("requiredTime: " + requiredTime + " [sec]");

		graph3D.setData(multiDataSeriesX, multiDataSeriesY, multiDataSeriesZ);
		//graph3D.clear();

		//graph3D.setAutoExitingEnabled(true);
		graph3D.setAutoExitingEnabled(false);
		graph3D.setAutoDisposingEnabled(true);

		//graph3D.dispose();
		//graph3D.setAutoDisposingEnabled(false);
		//graph3D.setAutoDisposingEnabled(true);

		//graph3D.setDefaultMouseListenerEnabled(false);
		//graph3D.setDefaultMouseMotionListenerEnabled(false);
		//graph3D.setDefaultMouseWheelListenerEnabled(false);

		CustomMouseListener customMouseListener = new CustomMouseListener();
		graph3D.addMouseListener(customMouseListener);
		graph3D.addMouseMotionListener(customMouseListener);
		graph3D.addMouseWheelListener(customMouseListener);

		CustomKeyListener customKeyListener = new CustomKeyListener();
		graph3D.addKeyListener(customKeyListener);

		CustomWindowListener customWindowListener = new CustomWindowListener();
		graph3D.addWindowListener(customWindowListener);

		CustomComponentListener customComponentListener = new CustomComponentListener();
		graph3D.addComponentListener(customComponentListener);

		//BufferedImage screenImage = BufferedImage.class.cast(graph3D.getImage());
		//ImageIO.write(screenImage, "PNG", new File("./ScreenImage1.png"));
		//System.out.println("Saved: ./ScreenImage1.png");

		//graph3D.setZZenithCameraAngle(0.8, 1.2, 0.0);
		//screenImage = BufferedImage.class.cast(graph3D.getImage());
		//ImageIO.write(screenImage, "PNG", new File("./ScreenImage2.png"));
		//System.out.println("Saved: ./ScreenImage2.png");

		//graph3D.setZZenithCameraAngle(1.2, 1.4, 0.0);
		//screenImage = BufferedImage.class.cast(graph3D.getImage());
		//ImageIO.write(screenImage, "PNG", new File("./ScreenImage3.png"));
		//System.out.println("Saved: ./ScreenImage3.png");

		graph3D.exportImageFile(new File("./ScreenImage1.png"), 1.0);
		System.out.println("Saved: ./ScreenImage1.png");

		graph3D.setZZenithCameraAngle(0.8, 1.2, 0.0);
		graph3D.exportImageFile(new File("./ScreenImage2.jpg"), 1.0);
		System.out.println("Saved: ./ScreenImage2.jpg");

		graph3D.setZZenithCameraAngle(1.2, 1.4, 0.0);
		graph3D.exportImageFile(new File("./ScreenImage3.jpg"), 0.3); // 30% quality
		System.out.println("Saved: ./ScreenImage1.jpg");

		//graph3D.setMenuVisible(false);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(new JMenu("This"));
		menuBar.add(new JMenu("Is"));
		menuBar.add(new JMenu("Customizable"));
		menuBar.add(new JMenu("Menu"));
		menuBar.add(new JMenu("Bar"));
		menuBar.add(new JMenu("Yeah!!!"));
		graph3D.setJMenuBar(menuBar);

		graph3D.setWindowTitle("Window Title Is Customizable!");
	}
	*/

	/*
	private class CustomWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			System.out.println("windowClosing@" + this);
		}
	}

	private class CustomComponentListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			System.out.println("componentResized" + this);
		}
	}

	private class CustomMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("mousePressed@" + this);
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			System.out.println("mouseDragged@" + this);
		}
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			System.out.println("mouseWheelMoved" + this);
		}
	}
	private class CustomKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			System.out.println("keyPressed@" + this);
		}
	}

	// A plotter class, which plots contents using the specified renderer, when the plotting is requested by the RINEARN Graph 3D.
	private class Plotter implements RinearnGraph3DPlottingListener {
		private final RinearnGraph3DRenderer renderer;

		public Plotter(RinearnGraph3DRenderer renderer) {
			this.renderer = renderer;
		}

		@Override
		public void plottingRequested(RinearnGraph3DPlottingEvent e) {
			System.out.println("plottingRequested is called! @" + System.currentTimeMillis() + "[ms]");
			this.plot();
		}

		@Override
		public void plottingCanceled(RinearnGraph3DPlottingEvent e) {
		}

		@Override
		public void plottingFinished(RinearnGraph3DPlottingEvent e) {
		}

		public void plot() {

			// Draw many points.
			int n = 100;
			for (int i=0; i<n; i++) {

				// Get a color from the HSB color gradient.
				float colorScalarValue = i / (float)(n - 1);
				Color color = Color.getHSBColor(colorScalarValue, 1.0f, 1.0f);

				// Prepare he coordinate values of the point.
				double theta = 6.0 * Math.PI * i / (double)(n - 1);
				double x = Math.cos(theta) * 1.6;
				double y = Math.sin(theta);
				double z = 2.0 * i / (double)(n - 1) - 1.0;

				// Draw the point.
				renderer.drawPoint(x, y, z, 8.0, color);
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

					float colorScalarValue = (float)((1.0 - aZ) / 2.5);
					Color color = Color.getHSBColor(colorScalarValue, 1.0f, 1.0f);
					renderer.drawQuadrangle(aX,aY,aZ, bX,bY,bZ, cX,cY,cZ, dX,dY,dZ, color);
				}
			}

			// Draw many roundom points.
			for (int i=0; i<500; i++) {

				double x = Math.random() * 2.0 - 1.0;
				double y = Math.random() * 2.0 - 1.0;
				double z = Math.random() * 2.0 - 1.0;
				x *= 2.0;
				renderer.drawPoint(x, y, z, 4.0);
				//renderer.drawPoint(x, y, z, 4.0, Color.GREEN);
			}

			// Draw many roundom lines.
			for (int i=0; i<500; i++) {

				double aX = Math.random() * 2.0 - 1.0;
				double aY = Math.random() * 2.0 - 1.0;
				double aZ = Math.random() * 2.0 - 1.0;
				aX *= 2.0;
				double bX = aX + Math.random() * 0.6 - 0.3;
				double bY = aY + Math.random() * 0.6 - 0.3;
				double bZ = aZ + Math.random() * 0.6 - 0.3;
				bX *= 2.0;
				renderer.drawLine(aX, aY, aZ, bX, bY, bZ, 1.0);
			}

			// Draw many quadrangles.
			for (int i=0; i<100; i++) {

				double aX = Math.random() * 2.0 - 1.0;
				double aY = Math.random() * 2.0 - 1.0;
				double aZ = Math.random() * 2.0 - 1.0;
				aX *= 2.0;
				double bX = aX + 0.1;
				double bY = aY;
				double bZ = aZ;
				double cX = aX + 0.1;
				double cY = aY + 0.1;
				double cZ = aZ;
				double dX = aX;
				double dY = aY + 0.1;
				double dZ = aZ;
				renderer.drawQuadrangle(
						aX, aY, aZ,
						bX, bY, bZ,
						cX, cY, cZ,
						dX, dY, dZ
				);
			}
		}
	}


	private static ColorGradient create2DColorGradient() {

		Color clearBlack = new Color(0, 0, 0, 0);

		ColorGradient.AxisColorGradient xGradient = new ColorGradient.AxisColorGradient();
		xGradient.setAxis(ColorGradient.Axis.X);
		xGradient.setBoundaryColors(new Color[] {clearBlack, Color.RED});
		xGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient yGradient = new ColorGradient.AxisColorGradient();
		yGradient.setAxis(ColorGradient.Axis.Y);
		yGradient.setBoundaryColors(new Color[] {clearBlack, Color.GREEN});
		yGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient[] axisGradients = {
				xGradient,
				yGradient
		};

		ColorGradient gradient = new ColorGradient();
		gradient.setAxisColorGradients(axisGradients);
		return gradient;
	}


	private static ColorGradient create3DColorGradient() {

		Color clearBlack = new Color(0, 0, 0, 0);

		ColorGradient.AxisColorGradient xGradient = new ColorGradient.AxisColorGradient();
		xGradient.setAxis(ColorGradient.Axis.X);
		xGradient.setBoundaryColors(new Color[] {clearBlack, Color.RED});
		xGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient yGradient = new ColorGradient.AxisColorGradient();
		yGradient.setAxis(ColorGradient.Axis.Y);
		yGradient.setBoundaryColors(new Color[] {clearBlack, Color.GREEN});
		yGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient zGradient = new ColorGradient.AxisColorGradient();
		zGradient.setAxis(ColorGradient.Axis.Z);
		zGradient.setBoundaryColors(new Color[] {clearBlack, Color.BLUE});
		zGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient[] axisGradients = {
				xGradient,
				yGradient,
				zGradient
		};

		ColorGradient gradient = new ColorGradient();
		gradient.setAxisColorGradients(axisGradients);
		return gradient;
	}


	private static ColorGradient createXColorGradientWithYClearMask() {

		Color clearWhite = new Color(255, 255, 255, 0);

		ColorGradient.AxisColorGradient xGradient = new ColorGradient.AxisColorGradient();
		xGradient.setAxis(ColorGradient.Axis.X);
		xGradient.setBoundaryColors(new Color[] {
				Color.BLUE,
				Color.CYAN,
				Color.GREEN,
				Color.YELLOW,
				Color.RED
		});
		xGradient.setBlendMode(ColorGradient.BlendMode.ADDITION);

		ColorGradient.AxisColorGradient yGradient = new ColorGradient.AxisColorGradient();
		yGradient.setAxis(ColorGradient.Axis.Y);
		yGradient.setBoundaryColors(new Color[] {clearWhite, Color.WHITE});
		yGradient.setBlendMode(ColorGradient.BlendMode.MULTIPLICATION);

		ColorGradient.AxisColorGradient[] axisGradients = {
				xGradient,
				yGradient
		};

		ColorGradient gradient = new ColorGradient();
		gradient.setAxisColorGradients(axisGradients);
		return gradient;
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
		double xMin = -2.0;
		double yMin = -2.0;
		double xMax = 2.0;
		double yMax = 2.0;
		double xDelta = (xMax - xMin) / (double)(xCount - 1);
		double yDelta = (yMax - yMin) / (double)(yCount - 1);

		double[][] x = new double[xCount][yCount];
		double[][] y = new double[xCount][yCount];
		double[][] z = new double[xCount][yCount];

		for (int ix=0; ix<xCount; ix++) {
			for (int iy=0; iy<yCount; iy++) {
				x[ix][iy] = xMin + ix * xDelta;
				y[ix][iy] = yMin + iy * yDelta;
				z[ix][iy] = 0.5 * Math.sin(1.5 * x[ix][iy]) + 0.5 * Math.cos(2.0 * y[ix][iy]);
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

	private static class LineData {
		public int nodeCount;
		public double[] x;
		public double[] y;
		public double[] z;
	}
	private static LineData generateExamLineData() {
		int nodeCount = 5000;
		double[] x = new double[nodeCount];
		double[] y = new double[nodeCount];
		double[] z = new double[nodeCount];

		for (int inode=0; inode<nodeCount; inode++) {
			z[inode] = 20.0 * (inode / (double)nodeCount) - 10.0;
			x[inode] = Math.sin(20.0 * z[inode]);
			y[inode] = Math.cos(10.0 * z[inode]);
		}

		LineData lineData = new LineData();
		lineData.nodeCount = nodeCount;
		lineData.x = x;
		lineData.y = y;
		lineData.z = z;
		return lineData;
	}
	*/
}
