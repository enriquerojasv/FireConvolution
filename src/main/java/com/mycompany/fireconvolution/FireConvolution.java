package com.mycompany.fireconvolution;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author rvenr
 */
public class FireConvolution extends JFrame {

    ControlPanel controlPanel;
    Viewer viewer;
    Fire fire;

    public static void main(String[] args) {
        for (LookAndFeelInfo look_feel : UIManager.getInstalledLookAndFeels()) {
            System.out.println(look_feel.getName());
            if ("Windows".equals(look_feel.getName())) {
                try {
                    UIManager.setLookAndFeel(look_feel.getClassName());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                FireConvolution fireConvolution = new FireConvolution();
            }
        }
    }

    private void initializeComponents() {
        this.setSize(800, 800);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.viewer = new Viewer(this);
        this.fire = new Fire(this);
        this.controlPanel = new ControlPanel(this);
        this.addPanels();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void addPanels() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.001;
        c.weighty = 1;
        this.add(this.controlPanel, c);
        c.weightx = 0.999;
        c.gridx = 1;
        this.add(this.viewer, c);
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public Viewer getViewer() {
        return viewer;
    }

    public FireConvolution() {
        initializeComponents();
    }
}
