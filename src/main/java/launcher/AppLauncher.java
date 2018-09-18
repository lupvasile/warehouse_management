package launcher;

import presentation.controller.MainController;
import presentation.view.MainFrame;

/**
 * Class containing the main method of the app.
 */
public class AppLauncher {

    /**
     * Entry point in the app.
     * Creates a view and controller and links them together.
     */
    public static void main(String[] args) {
        MainController controller = new MainController();
        MainFrame view = new MainFrame();
        controller.setView(view);
    }
}

