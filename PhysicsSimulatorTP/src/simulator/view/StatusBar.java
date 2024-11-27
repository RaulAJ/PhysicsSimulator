package simulator.view;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

class StatusBar extends JPanel implements SimulatorObserver {
    private double _simulationTime;
    // Añadir los atributos necesarios, si hace falta …
    private JLabel _timeLabel;
    private JLabel _groupsLabel;
    private Controller _ctrl;
    StatusBar(Controller ctrl) {
        _ctrl = ctrl;
        initGUI();
        _ctrl.addObserver(this);
    }
    private void initGUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createBevelBorder(1));
        // Crear una etiqueta de tiempo y añadirla al panel
        _simulationTime = 0;
        _timeLabel = new JLabel("Time: 0");
        this.add(_timeLabel);

        // Añadir un separador vertical
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(10, 20));
        this.add(s);

        // Crear la etiqueta de número de grupos y añadirla al panel
        _groupsLabel = new JLabel("Groups: 0");
        this.add(_groupsLabel);

    }

    @Override
    public void onAdvance(Map<String, BodiesGroup> groups, double time) {
        updateLabels(groups, time);
    }

    @Override
    public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
        updateLabels(groups, time);
    }

    @Override
    public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
        updateLabels(groups, time);

    }

    @Override
    public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
        updateLabels(groups, _simulationTime);
    }

    @Override
    public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
        updateLabels(groups, _simulationTime);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {
        // No es necesario actualizar las etiquetas aquí
    }

    @Override
    public void onForceLawsChanged(BodiesGroup g) {
        // No es necesario actualizar las etiquetas aquí
    }
    //El resto de métodos van aquí...
    private void updateLabels(Map<String, BodiesGroup> groups, double time) {
        _timeLabel.setText(String.format("Time: %.2f", time));
        _groupsLabel.setText(String.format("Groups: %d", groups.size()));
    }
}