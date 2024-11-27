package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;

public class DangerZone implements SimulatorObserver{

	Map<Body,Map<Integer, Integer>> bodies;
	
	private Double r;
	
	public DangerZone(Controller ctrl, Double r) {
		bodies = new HashMap<>();
		ctrl.addObserver(this);
	}
	
	private void update() {
		for(Map.Entry<Body, Map<Integer, Integer>> e : bodies.entrySet()) {
			if(e.getKey().getPosition().magnitude() <= r) {
				e.getValue().put(1, e.getValue().get(e));
			}
		}
	}
	
	@Override
	public void onAdvance(Map<String, BodiesGroup> groups, double time) {
		// TODO Auto-generated method stub
		update();
	}

	@Override
	public void onReset(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		bodies.clear();
	}

	@Override
	public void onRegister(Map<String, BodiesGroup> groups, double time, double dt) {
		// TODO Auto-generated method stub
		bodies.clear();
		for(Map.Entry<String, BodiesGroup> e : groups.entrySet()) {
			for(Body b : e.getValue()) {
				bodies.put(b, new HashMap(0 ,0));
			}
		}
		
	}

	@Override
	public void onGroupAdded(Map<String, BodiesGroup> groups, BodiesGroup g) {
		// TODO Auto-generated method stub
		for(Body b : g) {
			bodies.put(b, new HashMap(0 ,0));
		}
	}

	@Override
	public void onBodyAdded(Map<String, BodiesGroup> groups, Body b) {
		// TODO Auto-generated method stub
		bodies.put(b, new HashMap(0 ,0));
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
