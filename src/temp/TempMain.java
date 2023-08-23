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
