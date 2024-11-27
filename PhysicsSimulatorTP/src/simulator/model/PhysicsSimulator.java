package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class PhysicsSimulator implements Observable<SimulatorObserver> {
	//Tiempo real por paso
	private double dt;
	//Current time in seconds
	private double time_s;
	private ForceLaws forceLaws;
	private Map<String,BodiesGroup> bodiesGroupMap;
	private List<String> groupIdList;

	private List<SimulatorObserver> observerList;

	private Map<String, BodiesGroup> _groupsRO;

	public PhysicsSimulator(ForceLaws forceLaws, double dt){

		if (forceLaws == null || dt < 0)
		{
			throw new IllegalArgumentException("Forcelaws cannot be null. Delta-time cannot be negative-");
		}
		else {
			this.forceLaws = forceLaws;
			//initially time = 0:
			this.time_s = 0;

			this.dt = dt;
			this.bodiesGroupMap = new HashMap<>();
			this.groupIdList = new ArrayList<>();
			this.observerList = new ArrayList<>();
			_groupsRO = Collections.unmodifiableMap(bodiesGroupMap);
		}
	}

	public void advance() {
		for (Map.Entry<String, BodiesGroup> entry : bodiesGroupMap.entrySet())
		{
			entry.getValue().advance(this.dt);            
		}
		//incrementar tiempo actual en dt segundos
		this.time_s += this.dt;
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onAdvance(_groupsRO , time_s);
		}
	}
	/**
	 * añade un nuevo grupo con identificador id al mapa de grupos
	 * */
	public void addGroup(String id) throws IllegalArgumentException{
		if (bodiesGroupMap.containsKey(id))
		{
			// Exception: parameter "id" already exists
			throw new IllegalArgumentException("Cannot add group: " + id + ". Id already exists");
		}
		BodiesGroup g = new BodiesGroup(id, this.forceLaws);
		this.bodiesGroupMap.put(id, g);
		this.groupIdList.add(id);
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onGroupAdded(_groupsRO, g);
		}
	}
	/**
	 * añade el cuerpo b al grupo con identificador b.getGid()
	 * */
	public void addBody(Body b) {
		boolean groupExists = false;
		String groupId = b.getgId();

		if (bodiesGroupMap.containsKey(groupId))
		{
			groupExists = true;
			bodiesGroupMap.get(groupId).addBody(b);
		}
		/*
        for (Map.Entry<String, BodiesGroup> entry : bodiesGroupMap.entrySet())
        {
            if (b.getgId().equals(entry.getKey()))
            {
                groupExists = true;
                entry.getValue().addBody(b);
            }
        }*/

		if (!groupExists)
		{
			throw new IllegalArgumentException("Cannot add body to non-existent group.");
		}
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onBodyAdded(_groupsRO, b);
		}
	}
	/**
	 * cambia las leyes de la fuerza del grupo con identificador id a fl.
	 * */
	public void setForceLaws(String gId, ForceLaws fl) {
		boolean groupExists = false;
		if (bodiesGroupMap.containsKey(gId))
		{
			groupExists = true;
			bodiesGroupMap.get(gId).setForceLaws(fl);
		}
		if (!groupExists)
		{
			throw new IllegalArgumentException("Group does not exist");
		}
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onForceLawsChanged(this._groupsRO.get(gId));
		}
	}
	public JSONObject getState() {
		JSONObject jo = new JSONObject();
		jo.put("time", time_s);
		JSONArray ja = new JSONArray();
		for (String id : groupIdList)
		{
			ja.put(this.bodiesGroupMap.get(id).getState());
		}
		jo.put("groups", ja);
		return jo;
	}
	public String toString(){
		return this.getState().toString();
	}

	public void reset(){
		this.bodiesGroupMap.clear();
		this.groupIdList.clear();
		this.time_s = 0.0;
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onReset(_groupsRO, time_s, dt);
		}

	}
	public void setDeltaTime(double dt)
	{
		if (dt < 0)
		{
			throw new IllegalArgumentException("Dt cannot be a negative number");
		}
		else {
			this.dt = dt;
			for (SimulatorObserver simulatorObserver: observerList)
			{
				simulatorObserver.onDeltaTimeChanged(dt);
			}
		}
	}
	@Override
	public void addObserver(SimulatorObserver o) {
		this.observerList.add(o);
		o.onRegister(_groupsRO, time_s, dt);

	}

	@Override
	public void removeObserver(SimulatorObserver o) {
		this.observerList.remove(o);
	}

	public double getDeltaTime() {
		return dt;
	}

	public void removeBody(String id) {
		// TODO Auto-generated method stub
		Map<String, BodiesGroup> aux = new HashMap<>();
		Iterator<Map.Entry<String, BodiesGroup>> it = bodiesGroupMap.entrySet().iterator();
		for (Map.Entry<String, BodiesGroup> entry : bodiesGroupMap.entrySet())
		{			
			BodiesGroup auxiliar = entry.getValue();
			for(Body b : entry.getValue()) {
				if(id.equals(b.getId())) {
					//entry.getValue().deleteBody(b);
					auxiliar = aux1(entry.getValue(), b);
					auxiliar.deleteBody(b);
				}

			}
			aux.put(entry.getKey(), auxiliar);

		}
		bodiesGroupMap = aux;
		BodiesGroup g  = bodiesGroupMap.get("g1");
		for(Body b : g) {
			System.out.println(b.getId());
		}
		_groupsRO = bodiesGroupMap;
		for (SimulatorObserver simulatorObserver: observerList)
		{
			simulatorObserver.onReset(_groupsRO, time_s, dt);
		}
		
	}
	
	private BodiesGroup aux1(BodiesGroup bg, Body b) {
		//BodiesGroup bgaux = new BodiesGroup(bg.getId(), this.forceLaws);
		
		return bg.getCopy();
	}
}
