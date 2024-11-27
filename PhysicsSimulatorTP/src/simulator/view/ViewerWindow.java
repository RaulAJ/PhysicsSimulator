package simulator.view;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Map;

class ViewerWindow extends JFrame implements SimulatorObserver {
    private Controller _ctrl;
    private SimulationViewer _viewer;
    private JFrame _parent;
    ViewerWindow(JFrame parent, Controller ctrl) {
        super("Simulation Viewer");
        _ctrl = ctrl;
        _parent = parent;
        initGUI();
        // registrar this como observador
        _ctrl.addObserver(this);
    }
    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
//  poner contentPane como mainPanel con scrollbars (JScrollPane)
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        setContentPane(scrollPane);
//  crear el viewer y añadirlo a mainPanel (en el centro)
        _viewer = new Viewer();
        mainPanel.add(_viewer, BorderLayout.CENTER);
//  en el método windowClosing, eliminar ‘this’ de los observadores
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                _ctrl.removeObserver(ViewerWindow.this);
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

            }});
        pack();
        if (_parent != null)
            setLocation(
                    _parent.getLocation().x + _parent.getWidth()/2 - getWidth()/2,
                    _parent.getLocation().y + _parent.getHeight()/2 - getHeight()/2);
        setVisible(true);
    }

    @Override
    public void onAdvance(Map<String, BodiesGroup> groups, double time) {
        _viewer.update();
    }

    @Override
    public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
        _viewer.reset();
        for (BodiesGroup group : groups.values()) {
            _viewer.addGroup(group);
            for (Body body : group) {
                _viewer.addBody(body);
            }
        }
    }

    @Override
    public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
        onReset(groups, time, dt);
    }

    @Override
    public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
        _viewer.addGroup(g);
    }

    @Override
    public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
        _viewer.addBody(b);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(BodiesGroup g) {

    }
// otros métodos van aquí….
}
