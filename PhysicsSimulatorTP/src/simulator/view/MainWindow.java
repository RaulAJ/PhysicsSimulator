package simulator.view;

import simulator.control.Controller;
import simulator.launcher.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainWindow extends JFrame {
    private Controller _ctrl;
    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);
        // crear ControlPanel y añadirlo en PAGE_START de mainPanel
        ControlPanel controlPanel = new ControlPanel(_ctrl);
        mainPanel.add(controlPanel, BorderLayout.PAGE_START);

        // crear StatusBar y añadirlo en PAGE_END de mainPanel
        StatusBar statusBar = new StatusBar(_ctrl);
        mainPanel.add(statusBar, BorderLayout.PAGE_END);
        // Definición del panel de tablas (usa un BoxLayout vertical)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        // crear la tabla de grupos y añadirla a contentPanel.
        // Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
        InfoTable groupsTable = new InfoTable("Groups", new GroupsTableModel(_ctrl));
        groupsTable.setPreferredSize(new Dimension(500, 250));
        contentPanel.add(groupsTable);
        // crear la tabla de cuerpos y añadirla a contentPanel.
        // Usa setPreferredSize(new Dimension(500, 250)) para fijar su tamaño
        InfoTable bodiesTable = new InfoTable("Bodies", new BodiesTableModel(_ctrl));
        bodiesTable.setPreferredSize(new Dimension(500, 250));
        contentPanel.add(bodiesTable);
        //ok: llama a Utils.quit(MainWindow.this) en el método windowClosing
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Utils.quit(MainWindow.this);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    }
