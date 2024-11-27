package simulator.view;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {
    String[] _header = { "Id", "gId", "Mass", "Velocity", "Position", "Force" };
    List<Body> _bodies;
    BodiesTableModel(Controller ctrl) {
        _bodies = new ArrayList<>();
        // registrar this como observer
        ctrl.addObserver(this);
    }

    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return _header.length;
    }
    @Override
    public String getColumnName(int columnIndex) {
        return _header[columnIndex];
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Body body = _bodies.get(rowIndex);
        switch (columnIndex)
        {
            case 0:
                return body.getId();
            case 1:
                return body.getgId();
            case 2:
                return body.getMass();
            case 3:
                return body.getVelocity();
            case 4:
                return body.getPosition();
            case 5:
                return body.getForce();
            default:
                return null;
        }
    }

    @Override
    public void onAdvance(Map<String, BodiesGroup> groups, double time) {
        fireTableDataChanged();
    }

    @Override
    public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
        updateBodies(groups);
    }

    @Override
    public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
        updateBodies(groups);
    }

    @Override
    public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
        //updateBodies(groups);
    	for(Body b :g) {
    		_bodies.add(b);
    	}
    }

    @Override
    public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
    	_bodies.add(b);
        //updateBodies(groups);
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(BodiesGroup g) {

    }
// el resto de métodos van aquí…
    //Metodo para actualizar cuerpos
    private void updateBodies(Map<String, BodiesGroup> groups) {
        _bodies.clear();
        for (BodiesGroup group : groups.values()) {
            for (Body body : group) {
             _bodies.add(body);
            }
        }
        fireTableStructureChanged();
    }
}
