package presentation.controller;

import presentation.view.MainFrame;

/**
 * Controller providing listeners for the MainFrame.
 *
 * @author Vasile Lup
 * @see MainFrame
 */
public class MainController {
    private MainFrame view;

    public void setView(MainFrame view) {
        this.view = view;

        view.addListeners(x -> customerButtonListener(), x -> productButtonListener(), x -> orderButtonListener());
        view.setVisible(true);
    }

    private void customerButtonListener() {
        view.setCustomerFrameVisibility(true);
    }

    private void productButtonListener() {
        view.setProductFrameVisibility(true);
    }

    private void orderButtonListener() {
        view.setOrderFrameVisibility(true);
    }


}
