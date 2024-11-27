package simulator.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.misc.Vector2D;
import simulator.model.BodiesGroup;
import simulator.model.Body;
import simulator.model.SimulatorObserver;
import simulator.model.StationaryBody;

public class ForceTableModel extends AbstractTableModel implements SimulatorObserver{

	private String[] _headers = { "Body", "Total Forces"};
	Map<Body, Vector2D> _bodies;

	ForceTableModel(Controller ctrl) {
		// TODO Auto-generated constructor stub
		_bodies = new HashMap<>();
		//_bodies.put(new StationaryBody("3", "d",new Vector2D(0,0),32), new Vector2D(0,0));
		ctrl.addObserver(this);

	}

	@Override
	public int getRowCount() {
		return _bodies.size();
	}

	@Override
	public int getColumnCount() {
		return _headers.length;
	}
	public String getColumnName(int columnIndex) {
		return _headers[columnIndex];
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object [] a = _bodies.keySet().toArray();
		Body b = (Body) a[rowIndex];
		
		switch(columnIndex) {
			case 0:
				return b.getId();
			case 1:
				return _bodies.get(a[rowIndex]);
	
			default:
				return null;
		}
	}

	private void updateBodies(Map<Body, Vector2D> _bodies) {
		for(Map.Entry<Body, Vector2D> entry : _bodies.entrySet()) {
			Vector2D fn = entry.getKey().getForce();
			Vector2D fa = entry.getValue();
			Vector2D fr = fn.plus(fa);
			entry.setValue(fr);
		}

        fireTableDataChanged();
	}

	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		updateBodies(_bodies);

        fireTableDataChanged();

	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		updateBodies(_bodies);
        fireTableStructureChanged();

	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method 
		_bodies.clear();
		for(Entry<String, BodiesGroup> entry : groups.entrySet()) {
			for(Body bg : entry.getValue()){
				_bodies.put(bg, new Vector2D());
			}
		}

	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub

		for(Body b : g) {
			_bodies.put(b, new Vector2D(0,0));
		}
		updateBodies(_bodies);

        fireTableStructureChanged();

	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		_bodies.put(b, new Vector2D(0,0));
		updateBodies(_bodies);
        fireTableStructureChanged();

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onForceLawsChanged(BodiesGroup g) {
		// TODO Auto-generated method stub
	}
}
