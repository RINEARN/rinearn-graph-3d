package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.renderer.RinearnGraph3DRenderer;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


/**
 * The class handling events on the graph screen,
 * such as mouse-dragging events for rotate a graph, mouse wheel-scrolling events for zooming up/down a graph, and so on.
 */
public final class ScreenHandler {

	/** The array index representing X coordinate, for 2 or 3-dimensional arrays. */
	private static final int X = 0;

	/** The array index representing Y coordinate, for 2 or 3-dimensional arrays. */
	private static final int Y = 1;

	/** The label playing the role of the screen, on which a 3D graph is displayed. */
	private final JLabel screenLabel;

	/** The rendering engine of 3D graphs. */
	private final RinearnGraph3DRenderer renderer;

	/** The loop which performs rendering and updates the screen, on an independent thread. */
	public final RenderingLoop renderingLoop;

	/** Stores the X and Y coordinates of the center of the graph on the screen. */
	private volatile int[] graphCenterCoords = new int[2];

	/** The speed of the rotation by mouse-dragging for radial direction from the graph center. */
	private static final double RADIAL_ROTATION_SPEED = 0.005;

	/** The speed of the rotation by mouse-dragging for circumferential direction from the graph center. */
	private static final double CIRCUMFERENTIAL_ROTATION_SPEED = 0.003;

	/**
	 * Creates new instance for handling events occurred on the specified view, using the specified model.
	 * 
	 * @param model The front-end class of Model layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of View layer, which provides visible part of GUI without event handling.
	 * @param renderer The rendering engine of 3D graphs.
	 * @param renderingLoop The loop which invokes rendering procedures on an independent thread, and updates the screen.
	 */
	public ScreenHandler(Model model, View view, RinearnGraph3DRenderer renderer, RenderingLoop renderingLoop) {
		this.screenLabel = view.mainWindow.screenLabel;
		this.renderer = renderer;
		this.renderingLoop = renderingLoop;

		// The implementation of MouseListener and MouseMotionLister,
		// handing mouse-dragging events for rotate a graph.
		RotationEventListener rotationEventListener = new RotationEventListener();
		this.screenLabel.addMouseListener(rotationEventListener);
		this.screenLabel.addMouseMotionListener(rotationEventListener);

		// Initializes the graph center's coordinates to match with the screen center.
		BufferedImage screenImage = BufferedImage.class.cast(renderer.getScreenImage());
		this.graphCenterCoords[X] = screenImage.getWidth()/2;
		this.graphCenterCoords[Y] = screenImage.getHeight()/2;
	}


	/**
	 * The event listener handling mouse-dragging events for rotate a graph.
	 */
	private final class RotationEventListener extends MouseAdapter {

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseX = -1;

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseY = -1;

		/**
		 * Stores the X and Y coordinates of the mouse pointer, when the mouse's left button is pressed.
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			if (!SwingUtilities.isLeftMouseButton(me)) {
				return;
			}
			this.lastMouseX = me.getX();
			this.lastMouseY = me.getY();
		}

		/**
		 * Rotates the angles of the graph, when the mouse is dragged by pressing the left-button.
		 */
		@Override
		public void mouseDragged(MouseEvent me) {
			if (!SwingUtilities.isLeftMouseButton(me)) {
				return;
			}

			// Short aliases.
			int centerX = graphCenterCoords[X];
			int centerY = graphCenterCoords[Y];

			// Get the differential vector (dx, dy) from the last coordinate of the mouse pointer.
			int currentMouseX = me.getX();
			int currentMouseY = me.getY();
			int dx = lastMouseX - currentMouseX;
			int dy = lastMouseY - currentMouseY;

			// From the differential vector, extract a component vector facing the radial direction from the graph center.
			double distanceFromCenter = Math.sqrt(
					(currentMouseX - graphCenterCoords[X]) * (currentMouseX - graphCenterCoords[X])
					+
					(currentMouseY - graphCenterCoords[Y]) * (currentMouseY - graphCenterCoords[Y])					
			);

			// Check whether we can define a "radial unit vector" under the current situation,
			// where the radial unit vector is a vector of which foot is the graph center,
			// and facing the direction toward the mouse pointer, and the length of the vector is 1.
			// (We can't define it when the mouse pointer is just on the graph center.)
			boolean existsRadialUnitVecotr = this.existsRadialUnitVector(lastMouseX, lastMouseY, centerX, centerY);

			// If we can define a radial unit vector, define it.
			double[] radialUnitVector = null;
			if (existsRadialUnitVecotr) {
				radialUnitVector = this.computeRadialUnitVector(lastMouseX, lastMouseY, centerX, centerY);
			}

			// From the differential vector (dx, dy),
			// extract a component vector facing the radial direction, of which center is the graph center.
			// Call this vector as "radial delta vector".
			double[] radialDeltaVector = this.computeRadialDeltaVector(
					dx, dy, existsRadialUnitVecotr, radialUnitVector
			);

			// From the differential vector (dx, dy),
			// compute the length of a component vector facing the circumferential direction around the graph center.
			// Call this vector as "circumferential delta vector".
			double circumferentialDeltaVectorLength = this.computeCircumferentialDeltaVectorLength(
					dx, dy, existsRadialUnitVecotr, radialUnitVector, radialDeltaVector
			);

			// Near the rotational center,
			// rotations by radial/circumferential delta vectors computed above gives a little "hanged up" feelings.
			// Hence, apply simple 2-axes rotation algorithm when the mouse is near the center.
			if (distanceFromCenter < 100) {
				renderer.rotateAroundX(-dy * RADIAL_ROTATION_SPEED);
				renderer.rotateAroundY(-dx * RADIAL_ROTATION_SPEED);

			// When the mouse is far enough from the center,
			// apply 3-axes rotation algorithm based on radial/circumferential vectors.
			} else {
				renderer.rotateAroundX(-radialDeltaVector[Y] * RADIAL_ROTATION_SPEED);
				renderer.rotateAroundY(-radialDeltaVector[X] * RADIAL_ROTATION_SPEED);
				renderer.rotateAroundZ(circumferentialDeltaVectorLength * CIRCUMFERENTIAL_ROTATION_SPEED);								
			}

			// Perform rendering on the rendering loop's thread asynchronously.
			renderingLoop.requestRendering();

			// Updates the coordinates of the mouse pointer at the lastly pressed point, to the current point.
			this.lastMouseX = currentMouseX;
			this.lastMouseY = currentMouseY;
		}


		/**
		 * Checks whether we can define a "radial unit vector" under the current situation,
		 * where the radial unit vector is a vector of which foot is the graph center,
		 * and facing the direction toward the mouse pointer, and the length of the vector is 1.
		 * 
		 * We can't define it when the mouse pointer is just on the graph center.
		 * 
		 * @param mousePointerX The X coordinate of the mouse pointer.
		 * @param mousePointerY The Y coordinate of the mouse pointer.
		 * @param graphCenterX The X coordinate of the center of the graph.
		 * @param graphCenterY The Y coordinate of the center of the graph.
		 * @return Returns true if we can define a radial unit vector.
		 */
		private boolean existsRadialUnitVector(int mousePointerX, int mousePointerY, int graphCenterX, int graphCenterY) {
			boolean exists = (mousePointerX != graphCenterX || mousePointerY != graphCenterY);
			return exists;
		}


		/**
		 * Compute the value of the "radial unit vector" under the current situation,
		 * where the radial unit vector is a vector of which foot is the graph center,
		 * and facing the direction toward the mouse pointer, and the length of the vector is 1.
		 * 
		 * @param mousePointerX The X coordinate of the mouse pointer.
		 * @param mousePointerY The Y coordinate of the mouse pointer.
		 * @param graphCenterX The X coordinate of the center of the graph.
		 * @param graphCenterY The Y coordinate of the center of the graph.
		 * @return Returns the array storing X and Y components of the radial unit vector.
		 */
		private double[] computeRadialUnitVector(int mousePointerX, int mousePointerY, int graphCenterX, int graphCenterY) {
			if (!this.existsRadialUnitVector(mousePointerX, mousePointerY, graphCenterX, graphCenterY)) {
				return null;
			}

			// Compute the non-normalized radial vector.
			double[] radialUnitVector = new double[2];
			radialUnitVector[X] = mousePointerX - graphCenterX;
			radialUnitVector[Y] = mousePointerY - graphCenterY;

			// Normalize the vector length to 1.
			double vectorLength =  Math.sqrt(radialUnitVector[X] * radialUnitVector[X] + radialUnitVector[Y] * radialUnitVector[Y]);
			radialUnitVector[X] /= vectorLength;
			radialUnitVector[Y] /= vectorLength;
			return radialUnitVector;
		}


		/**
		 * Compute the value of the "radial delta vector" under the current situation,
		 * which is a component vector of the differential vector (dx, dy),
		 * facing the radial direction, of which center is the graph center.
		 * 
		 * @param dx The X component of the differential vector of the dragged mouse pointer.
		 * @param dy The Y component of the differential vector of the dragged mouse pointer.
		 * @param existsRadialUnitVector The flag representing whether we can define a radial unit vector.
		 * @param radialUnitVector The radial unit vector, or null if we can not define it.
		 * @return Returns The array storing X and Y components of the radial delta vector.
		 */
		private double[] computeRadialDeltaVector(int dx, int dy, boolean existsRadialUnitVector, double[] radialUnitVector) {
			if (existsRadialUnitVector) {
				double radialInnerProduct = dx * radialUnitVector[X] + dy * radialUnitVector[Y];
				double[] radialDeltaVector = { radialInnerProduct * radialUnitVector[X], radialInnerProduct * radialUnitVector[Y] };
				return radialDeltaVector;
			} else {
				double[] radialDeltaVector = { dx, dy };
				return radialDeltaVector;
			}
		}


		/**
		 * Compute the length of the "circumferential delta vector" under the current situation,
		 * which is a component vector facing the circumferential direction around the graph center.
		 * 
		 * The length returned by this method is a "signed" value.
		 * If the rotation direction of the circumferential delta vector is counterclockwise, the sign is positive.
		 * If it is clockwise, the sign is negative.
		 * 
		 * @param dx The X component of the differential vector of the dragged mouse pointer.
		 * @param dy The Y component of the differential vector of the dragged mouse pointer.
		 * @param existsRadialUnitVector The flag representing whether we can define a radial unit vector.
		 * @param radialUnitVector The radial unit vector, or null if we can not define it.
		 * @return Returns The "signed" length of the circumferential delta vector.
		 */
		private double computeCircumferentialDeltaVectorLength(int dx, int dy, boolean existsRadialUnitVector, double[] radialUnitVector, double[] radialDeltaVector) {
			if (!existsRadialUnitVector) {
				return 0.0;
			}

			// Compute X and Y components of circumferential delta vector.
			// We can compute it easily by subtracting the radial delta vector from the differential vector,
			// because the directions of radial and circumferential vectors are orthogonal,
			// and the both vectors are a component vector of the differential vector.
			double[] circumferentialDeltaVector = { dx - radialDeltaVector[X], dy - radialDeltaVector[Y] };

			// Compute the length of the vector, to be returned.
			double circumferentialDeltaVectorLength = Math.sqrt(
					circumferentialDeltaVector[X] * circumferentialDeltaVector[X]
					+
					circumferentialDeltaVector[Y] * circumferentialDeltaVector[Y]
			);

			// If the rotation direction of the circumferential delta vector is clockwise, negate the sign of the length.
			// We can detect it by computing the cross product vector
			// between the radial UNIT vector and the circumferential delta vectors, and checking the sign of its Z component.
			// Note that, the radial DELTA vector may be zero vector theoretically, so don't use it instead of the radial unit vector here.
			double crossProductZ = radialUnitVector[X] * circumferentialDeltaVector[Y] - radialUnitVector[Y] * circumferentialDeltaVector[X];
			if (crossProductZ < 0) {
				circumferentialDeltaVectorLength = -circumferentialDeltaVectorLength;
			}
			return circumferentialDeltaVectorLength;
		}
	}

}
