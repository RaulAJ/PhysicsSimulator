package simulator.view;

import simulator.control.Controller;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GroupsTableModel extends AbstractTableModel implements SimulatorObserver {
    String[] _header = { "Id", "Force Laws", "Bodies" };
    List<BodiesGroup> _groups;
    GroupsTableModel(Controller ctrl) {
        _groups = new ArrayList<>();
        // registrar this como observador;
        ctrl.addObserver(this);
    }

    @Override
    public int getRowCount() {
        return _groups.size();
    }

    @Override
    public int getColumnCount() {
        return _header.length;
    }

    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BodiesGroup group = _groups.get(rowIndex);
        switch (columnIndex){
            case 0:
                return group.getId();
            case 1:
                return group.getForceLawsInfo();
            case 2:
                StringBuilder s = new StringBuilder();
                for(Body b : group) {
                	s.append(b.getId());
                	s.append(" ");
                }
                return s;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
    	return _header[columnIndex];
    }
    
    @Override
    public void onAdvance(Map<String, BodiesGroup> groups, double time) {
        fireTableDataChanged();
    }

    @Override
    public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
        _groups.clear();
        _groups.addAll(groups.values());
        fireTableStructureChanged();
    }

    @Override
    public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
        _groups.clear();
        _groups.addAll(groups.values());
        fireTableStructureChanged();
    }

    @Override
    public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
        _groups.clear();
        _groups.addAll(groups.values());
        fireTableStructureChanged();
    }

    @Override
    public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
       // _groups.clear();
        //_groups.addAll(groups.values());
        fireTableStructureChanged();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(BodiesGroup g) {
        fireTableDataChanged();

    }
// el resto de métodos van aquí …
}
