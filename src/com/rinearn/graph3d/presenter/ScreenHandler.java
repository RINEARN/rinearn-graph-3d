package com.rinearn.graph3d.presenter;

import com.rinearn.graph3d.view.View;
import com.rinearn.graph3d.model.Model;
import com.rinearn.graph3d.config.CameraConfiguration;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;


/**
 * The class handling events on the graph screen,
 * such as mouse-dragging events for rotate a graph, mouse wheel-scrolling events for zooming up/down a graph, and so on.
 */
public final class ScreenHandler {

	/** The speed of the rotation by mouse-dragging for radial direction from the graph center. */
	private static final double RADIAL_ROTATION_SPEED = 0.005;

	/** The speed of the rotation by mouse-dragging for circumferential direction from the graph center. */
	private static final double CIRCUMFERENTIAL_ROTATION_SPEED = 0.003;

	/** The maximum value of the magnification. */
	private static final double MAGNIFICATION_MAX = 100000.0;

	/** The minimum value of the magnification. */
	private static final double MAGNIFICATION_MIN = 1.0;

	/** The array index representing X coordinate, for 2 or 3-dimensional arrays. */
	private static final int X = 0;

	/** The array index representing Y coordinate, for 2 or 3-dimensional arrays. */
	private static final int Y = 1;

	/** The front-end class of "Model" layer, which provides internal logic procedures and so on. */
	private final Model model;

	/** The front-end class of "View" layer, which provides visible part of GUI without event handling. */
	private final View view;

	/** The front-end class of "Presenter" layer, which invokes Model's procedures triggered by user's action on GUI. */
	private final Presenter presenter;

	/** Stores the X and Y coordinates of the center of the screen. */
	private volatile int[] screenCenterCoords = new int[2];

	/** The flag for turning on/off the event handling feature of this instance. */
	private volatile boolean eventHandlingEnabled = true;


	/**
	 * Creates new instance for handling events occurred on the specified view, using the specified model.
	 * 
	 * @param model The front-end class of "Model" layer, which provides internal logic procedures and so on.
	 * @param view The front-end class of "View" layer, which provides visible part of GUI without event handling.
	 * @param presenter The front-end class of "Presenter" layer, which handles events occurred on GUI, and API requests.
	 */
	public ScreenHandler(Model model, View view, Presenter presenter) {
		this.model = model;
		this.view = view;
		this.presenter = presenter;

		JLabel screenLabel = view.mainWindow.screenLabel;

		// Add the MouseListener/MouseMotionLister handing mouse-dragging events for rotate a graph.
		RotationEventListener rotationEventListener = new RotationEventListener();
		screenLabel.addMouseListener(rotationEventListener);
		screenLabel.addMouseMotionListener(rotationEventListener);

		// Add the MouseListener/MouseMotionLister handling mouse-dragging events for shifting a graph center.
		CenterOffsetEventListener centerOffsetEventHandler = new CenterOffsetEventListener();
		screenLabel.addMouseListener(centerOffsetEventHandler);
		screenLabel.addMouseMotionListener(centerOffsetEventHandler);

		// Add the MouseWheelListener handling mouse-wheel-scrolling events for zooming-up/down the graph.
		ZoomEventListener zoomEventListener = new ZoomEventListener();
		screenLabel.addMouseWheelListener(zoomEventListener);

		// Add the ComponentListener handling resizing events of the graph screen.
		ResizeEventListener resizeEventListener = new ResizeEventListener();
		screenLabel.addComponentListener(resizeEventListener);

		// Initializes the screen center's coordinates.
		this.screenCenterCoords[X] = screenLabel.getWidth()/2;
		this.screenCenterCoords[Y] = screenLabel.getHeight()/2;
	}


	/**
	 * Turns on/off the event handling feature of this instance.
	 * 
	 * @param enabled Specify false for turning off the event handling feature (enabled by default).
	 */
	public synchronized void setEventHandlingEnabled(boolean enabled) {
		this.eventHandlingEnabled = enabled;
	}


	/**
	 * Gets whether the event handling feature of this instance is enabled.
	 * 
	 * @return Returns true if the event handling feature is enabled.
	 */
	public synchronized boolean isEventHandlingEnabled() {
		return this.eventHandlingEnabled;
	}





	// ================================================================================
	// 
	// - Event Listeners -
	//
	// ================================================================================


	/**
	 * The event listener handling resizing events of the graph screen.
	 */
	private class ResizeEventListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent ce) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			new SetScreenSizeAPIListener().run();
		}
	}


	/**
	 * The event listener handling mouse-wheel-scrolling events for zooming-up/down the graph.
	 */
	private final class ZoomEventListener extends MouseAdapter {

		/**
		 * Stores the X and Y coordinates of the mouse pointer, when the mouse's RIGHT BUTTON is pressed.
		 */
		@Override
		public void mouseWheelMoved(MouseWheelEvent me) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			CameraConfiguration cameraConfiguration = model.getConfiguration().getCameraConfiguration();
			double magnification = cameraConfiguration.getMagnification();

			// Zoom-in or out, depending on the direction of the wheel rotation.
			int wheelRot = me.getWheelRotation();
			if (wheelRot == 0) {
				return; // High-resolution mouse wheel may return 0 for rotation amount, when the wheel slightly rotated.
			} else if (wheelRot < 0) {
				magnification *= 1.2;
			} else if (0 < wheelRot) {
				magnification /= 1.2;
			}

			// If the updated magnification is too large or small, replace it by the acceptable limit value(s).
			if (magnification < MAGNIFICATION_MIN) {
				magnification = MAGNIFICATION_MIN;
			} else if (MAGNIFICATION_MAX < magnification) {
				magnification = MAGNIFICATION_MAX;
			}
			cameraConfiguration.setMagnification(magnification);

			// Reflect the updated camera angles.
			presenter.propagateConfiguration();

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();
		}
	}


	/**
	 * The event listener handling mouse-dragging events for shifting a graph center.
	 */
	private final class CenterOffsetEventListener extends MouseAdapter {

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseX = -1;

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseY = -1;

		/**
		 * Stores the X and Y coordinates of the mouse pointer, when the mouse's RIGHT BUTTON is pressed.
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			if (!SwingUtilities.isRightMouseButton(me)) {
				return;
			}
			this.lastMouseX = me.getX();
			this.lastMouseY = me.getY();
		}

		/**
		 * Shifts the center of the graph, when the mouse is dragged BY PRESSING THE RIGHT-BUTTON.
		 */
		@Override
		public void mouseDragged(MouseEvent me) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			if (!SwingUtilities.isRightMouseButton(me)) {
				return;
			}
			CameraConfiguration cameraConfiguration = model.getConfiguration().getCameraConfiguration();

			// Get the differential vector (dx, dy) from the last coordinate of the mouse pointer.
			int currentMouseX = me.getX();
			int currentMouseY = me.getY();
			int dx = currentMouseX - lastMouseX;
			int dy = currentMouseY - lastMouseY;

			// Integrate dx and dy to the values of the graph center offsets.
			int centerOffsetX = cameraConfiguration.getHorizontalCenterOffset();
			int centerOffsetY = cameraConfiguration.getVerticalCenterOffset();
			centerOffsetX += dx; // Be careful of the sign.
			centerOffsetY -= dy; // Be careful of the sign.
			cameraConfiguration.setHorizontalCenterOffset(centerOffsetX);
			cameraConfiguration.setVerticalCenterOffset(centerOffsetY);

			// Reflect the updated camera angles.
			presenter.propagateConfiguration();

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();

			// Updates the coordinates of the mouse pointer at the lastly pressed point, to the current point.
			this.lastMouseX = currentMouseX;
			this.lastMouseY = currentMouseY;
		}
	}


	/**
	 * The event listener handling mouse-dragging events for rotateing a graph.
	 */
	private final class RotationEventListener extends MouseAdapter {

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseX = -1;

		/** Stores the X coordinate of the mouse pointer at the lastly pressed point. */
		private volatile int lastMouseY = -1;

		/**
		 * Stores the X and Y coordinates of the mouse pointer, when the mouse's LEFT BUTTON is pressed.
		 */
		@Override
		public void mousePressed(MouseEvent me) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			if (!SwingUtilities.isLeftMouseButton(me)) {
				return;
			}
			this.lastMouseX = me.getX();
			this.lastMouseY = me.getY();
		}

		/**
		 * Rotates the angles of the graph, when the mouse is dragged BY PRESSING THE LEFT-BUTTON.
		 */
		@Override
		public void mouseDragged(MouseEvent me) {
			if (!isEventHandlingEnabled()) {
				return;
			}
			if (!SwingUtilities.isLeftMouseButton(me)) {
				return;
			}
			CameraConfiguration cameraConfiguration = model.getConfiguration().getCameraConfiguration();

			// Short aliases.
			int centerOffsetX = cameraConfiguration.getHorizontalCenterOffset();
			int centerOffsetY = cameraConfiguration.getVerticalCenterOffset();
			int centerX = screenCenterCoords[X] + centerOffsetX; // Be careful of the sign.
			int centerY = screenCenterCoords[Y] - centerOffsetY; // Be careful of the sign.

			// Get the differential vector (dx, dy) from the last coordinate of the mouse pointer.
			int currentMouseX = me.getX();
			int currentMouseY = me.getY();
			//int dx = lastMouseX - currentMouseX;
			//int dy = lastMouseY - currentMouseY;
			int dx = currentMouseX - lastMouseX;
			int dy = currentMouseY - lastMouseY;

			// From the differential vector, extract a component vector facing the radial direction from the graph center.
			double distanceFromCenter = Math.sqrt(
					(currentMouseX - centerX) * (currentMouseX - centerX)
					+
					(currentMouseY - centerY) * (currentMouseY - centerX)
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
				cameraConfiguration.rotateAroundX(dy * RADIAL_ROTATION_SPEED);
				cameraConfiguration.rotateAroundY(dx * RADIAL_ROTATION_SPEED);

			// When the mouse is far enough from the center,
			// apply 3-axes rotation algorithm based on radial/circumferential vectors.
			} else {
				cameraConfiguration.rotateAroundX(radialDeltaVector[Y] * RADIAL_ROTATION_SPEED);
				cameraConfiguration.rotateAroundY(radialDeltaVector[X] * RADIAL_ROTATION_SPEED);
				cameraConfiguration.rotateAroundZ(circumferentialDeltaVectorLength * CIRCUMFERENTIAL_ROTATION_SPEED);					
			}

			// Reflect the updated camera angles.
			presenter.propagateConfiguration();

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();

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
			if (0 < crossProductZ) {
				circumferentialDeltaVectorLength = -circumferentialDeltaVectorLength;
			}
			return circumferentialDeltaVectorLength;
		}
	}





	// ================================================================================
	// 
	// - API Listeners -
	//
	// ================================================================================


	/**
	 * Updates the size of the screen, into the size corresponding to the current window size.
	 * (API Implementation)
	 */
	public void updateScreenSize() {

		// Handle the API on the event-dispatcher thread.
		SetScreenSizeAPIListener apiListener = new SetScreenSizeAPIListener();
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * Sets the size of the screen.
	 * (API Implementation)
	 * 
	 * @param screenWidth The screenWidth (pixels) of the screen.
	 * @param screenHeight The screenHeight (pixels) of the screen.
	 */
	public void setScreenSize(int screenWidth, int screenHeight) {

		// Handle the API on the event-dispatcher thread.
		SetScreenSizeAPIListener apiListener = new SetScreenSizeAPIListener(screenWidth, screenHeight);
		if (SwingUtilities.isEventDispatchThread()) {
			apiListener.run();
		} else {
			try {
				SwingUtilities.invokeAndWait(apiListener);
			} catch (InvocationTargetException | InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}


	/**
	 * The class handling API requests for updating the size of the graph screen,
	 * on the event-dispatcher thread.
	 */
	private class SetScreenSizeAPIListener implements Runnable {

		/** The screenWidth of the screen, to be updated. If the value is -1, the size is computed automatically. */
		private volatile int screenWidth;

		/** The screenHeight of the screen, to be updated. If the value is -1, the size is computed automatically. */
		private volatile int screenHeight;

		/**
		 * Create a new instance for resizing the screen size, into the size corresponding with the current window size.
		 */
		public SetScreenSizeAPIListener() {
			this.screenWidth = -1;
			this.screenHeight = -1;
		}

		/**
		 * Create a new instance for resizing size into the specified size.
		 * 
		 * @param screenWidth The screenWidth of the screen.
		 * @param screenHeight The screenHeight of the screen.
		 */
		public SetScreenSizeAPIListener(int width, int height) {
			this.screenWidth = width;
			this.screenHeight = height;
		}

		@Override
		public void run() {
			if (this.screenWidth == -1) {
				this.screenWidth = (int)view.mainWindow.getScreenSize().getWidth();
			}
			if (this.screenHeight == -1) {
				this.screenHeight = (int)view.mainWindow.getScreenSize().getHeight();
			}

			// Store the above size into the configuration container.
			CameraConfiguration cameraConfig = model.getConfiguration().getCameraConfiguration();
			cameraConfig.setScreenSize(this.screenWidth, this.screenHeight);

			// Reflect the updated screen size.
			presenter.propagateConfiguration();

			// Perform rendering on the rendering loop's thread asynchronously.
			presenter.renderingLoop.requestRendering();

			// Update the screen center's coordinates.
			screenCenterCoords[X] = this.screenWidth/2;
			screenCenterCoords[Y] = this.screenHeight/2;
		}
	}

}
