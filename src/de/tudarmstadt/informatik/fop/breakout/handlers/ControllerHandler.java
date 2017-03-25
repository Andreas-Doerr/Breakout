package de.tudarmstadt.informatik.fop.breakout.handlers;

import de.tudarmstadt.informatik.fop.breakout.ui.Breakout;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

/**
 * Created by PC - Andreas on 19.03.2017.
 *
 * @author Andreas Dörr
 */
public class ControllerHandler {

	private static Controller controller;
	private static float deadZone = 0.01f;
	private static boolean hasAxis = true;
	// button was pressed
	private static boolean[] button_wasPressed = new boolean[11];

	public static void init() {
		hasAxis = true;

		try {
			Controllers.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Controllers.poll();

		for (int i=0;i<Controllers.getControllerCount();i++) {
			controller = Controllers.getController(i);
			System.out.println(i + ": " + controller.getName());
		}

		initNewController();

	}

	private static void initNewController() {
		if (Controllers.getControllerCount() > 0) {
			hasAxis = true;
			int selectedController = OptionsHandler.getSelectedController();

			try {
				controller = Controllers.getController(selectedController);
				System.out.println("Selected controller: " + controller.getName() + " (" + selectedController + ")");
				try {
					controller.setDeadZone(0, deadZone);
				} catch (IndexOutOfBoundsException a) {
					System.err.println("ERROR: DeadZone can not be set on controller: " + selectedController + " (this controller probably has no axis-input)");
					hasAxis = false;
				}
			} catch (IndexOutOfBoundsException e) {
				System.err.println("ERROR: Controller " + selectedController + " does not exist!");
			}

			System.out.println("INFO: Your controller has " + controller.getButtonCount() + " buttons.");
		}

	}

	public static void switchController() {
		if (OptionsHandler.getSelectedController() < Controllers.getControllerCount() - 1) {
			OptionsHandler.setSelectedController(OptionsHandler.getSelectedController() + 1);
			OptionsHandler.saveOptions();
		} else {
			OptionsHandler.setSelectedController(0);
			OptionsHandler.saveOptions();
		}
		ControllerHandler.initNewController();
		if (!missingForAcceptableController().equals(LanguageHandler.CONTROLLER_OK)) {
			SoundHandler.playNotAcceptable();
		}
	}
	public static String getControllerName() {
		if (Controllers.getControllerCount() > 0) {
			return controller.getName();
		} else {
			return "No Controllers";
		}

	}

	private static boolean doesButtonExist(int button) {
		// returns true if the button exists (the button's index is not higher than the amount of buttons and not below 0)
		return !Breakout.getDebug() && Controllers.getControllerCount() > 0 && button < controller.getButtonCount() && button >= 0;

	}

	public static boolean isButtonDown(int button) {
		// return true if button exists and game is not in debug mode and button is pressed
		return doesButtonExist(button) && !Breakout.getDebug() && controller.isButtonPressed(button);
	}
	public static boolean isButtonPressed(int button) {
		boolean isDown = isButtonDown(button);
		boolean isPressed = false;
		if (isDown && !button_wasPressed[button]) {
			button_wasPressed[button] = true;
			SoundHandler.playButtonPress();
			isPressed = true;
		} else if (!isDown) {
			button_wasPressed[button] = false;
		}
		return isPressed;
	}

	public static float getRightStick_x() {
		float rightStick_x = 0f;
		if (!Breakout.getDebug() && Controllers.getControllerCount() > 0) {
			rightStick_x = controller.getRZAxisValue();
		}
		return rightStick_x;
	}

	public static String missingForAcceptableController() {
		// returns a String containing:
			// if controller is acceptable: ""
			// if not: which axis and buttons are missing (in Text form intended for output)
		String missing = "";
		if (!hasAxis) {
			missing += LanguageHandler.NO_AXIS + "\n";
		}
		for (int i = 1; i < 5; i++) {
			if (!doesButtonExist(i - 1)) {
				missing += LanguageHandler.NO_BUTTON + ": " + i + "\n";
			}
		}
		if (missing.equals("")) {
			missing = LanguageHandler.CONTROLLER_OK;
		}
		return missing;
	}
}
