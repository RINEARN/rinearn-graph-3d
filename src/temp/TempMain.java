package temp;

import com.rinearn.graph3d.RinearnGraph3D;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;

import com.rinearn.graph3d.config.RinearnGraph3DConfiguration;
import com.rinearn.graph3d.config.ColorConfiguration;
import com.rinearn.graph3d.config.ColorGradient;

import java.awt.Color;


/**
 * A temporary main class in the development of this application.
 */
public class TempMain {

	public static void main(String[] args) {
		System.out.println("Hello RINEARN Graph 3D Ver.6!");

		// Launch a new RINEARN Graph 3D window (to be implemented).
		RinearnGraph3D graph3D = new RinearnGraph3D();

		// Gets the rendering engine of 3D graphs.
		RinearnGraph3DRenderer renderer = graph3D.getRenderer();

		// Create a color configuration of 3D color gradient.
		ColorConfiguration colorConfig = new ColorConfiguration();
		ColorGradient gradient = create3DColorGradient();
		colorConfig.setDataColorGradients(new ColorGradient[] {gradient});

		// Reflect the above color configuration to the renderer.
		RinearnGraph3DConfiguration config = RinearnGraph3DConfiguration.createEmptyConfiguration();
		config.setColorConfiguration(colorConfig);
		renderer.setConfiguration(config);


		// Draw many roundom points.
		for (int i=0; i<500; i++) {

			double x = Math.random() * 2.0 - 1.0;
			double y = Math.random() * 2.0 - 1.0;
			double z = Math.random() * 2.0 - 1.0;
			renderer.drawPoint(x, y, z, 4.0);
			//renderer.drawPoint(x, y, z, 4.0, Color.GREEN);
		}

		// Draw many roundom lines.
		for (int i=0; i<500; i++) {

			double aX = Math.random() * 2.0 - 1.0;
			double aY = Math.random() * 2.0 - 1.0;
			double aZ = Math.random() * 2.0 - 1.0;
			double bX = aX + Math.random() * 0.6 - 0.3;
			double bY = aY + Math.random() * 0.6 - 0.3;
			double bZ = aZ + Math.random() * 0.6 - 0.3;
			renderer.drawLine(aX, aY, aZ, bX, bY, bZ, 1.0);
		}

		// Draw many quadrangles.
		for (int i=0; i<100; i++) {

			double aX = Math.random() * 2.0 - 1.0;
			double aY = Math.random() * 2.0 - 1.0;
			double aZ = Math.random() * 2.0 - 1.0;
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

		/*
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
		*/

		// Render the 3D graph.
		renderer.render();
	}


	private static ColorGradient create2DColorGradient() {

		Color clearBlack = new Color(0, 0, 0, 0);

		ColorGradient.AxisColorGradient xGradient = new ColorGradient.AxisColorGradient();
		xGradient.setAxis(ColorGradient.GradientAxis.X);
		xGradient.setBoundaryCount(2);
		xGradient.setBoundaryColors(new Color[] {clearBlack, Color.RED});

		ColorGradient.AxisColorGradient yGradient = new ColorGradient.AxisColorGradient();
		yGradient.setAxis(ColorGradient.GradientAxis.Y);
		yGradient.setBoundaryCount(2);
		yGradient.setBoundaryColors(new Color[] {clearBlack, Color.GREEN});

		ColorGradient.AxisColorGradient[] axisGradients = {
				xGradient,
				yGradient
		};

		ColorGradient.BlendMode[] axisBlendModes = {
				ColorGradient.BlendMode.ADDITION,
				ColorGradient.BlendMode.ADDITION
		};

		ColorGradient gradient = new ColorGradient();
		gradient.setAxisCount(2);
		gradient.setAxisColorGradients(axisGradients);
		gradient.setAxisBlendModes(axisBlendModes);
		return gradient;
	}


	private static ColorGradient create3DColorGradient() {

		Color clearBlack = new Color(0, 0, 0, 0);

		ColorGradient.AxisColorGradient xGradient = new ColorGradient.AxisColorGradient();
		xGradient.setAxis(ColorGradient.GradientAxis.X);
		xGradient.setBoundaryCount(2);
		xGradient.setBoundaryColors(new Color[] {clearBlack, Color.RED});

		ColorGradient.AxisColorGradient yGradient = new ColorGradient.AxisColorGradient();
		yGradient.setAxis(ColorGradient.GradientAxis.Y);
		yGradient.setBoundaryCount(2);
		yGradient.setBoundaryColors(new Color[] {clearBlack, Color.GREEN});

		ColorGradient.AxisColorGradient zGradient = new ColorGradient.AxisColorGradient();
		zGradient.setAxis(ColorGradient.GradientAxis.Z);
		zGradient.setBoundaryCount(2);
		zGradient.setBoundaryColors(new Color[] {clearBlack, Color.BLUE});

		ColorGradient.AxisColorGradient[] axisGradients = {
				xGradient,
				yGradient,
				zGradient
		};

		ColorGradient.BlendMode[] axisBlendModes = {
				ColorGradient.BlendMode.ADDITION,
				ColorGradient.BlendMode.ADDITION,
				ColorGradient.BlendMode.ADDITION
		};

		ColorGradient gradient = new ColorGradient();
		gradient.setAxisCount(3);
		gradient.setAxisColorGradients(axisGradients);
		gradient.setAxisBlendModes(axisBlendModes);
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

}
