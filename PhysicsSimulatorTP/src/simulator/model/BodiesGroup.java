package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class BodiesGroup implements Iterable<Body> {
    private final String id;
    private ForceLaws forceLaws;
    private List<Body> bodies;
    private List<Body> bodiesRO;

    public BodiesGroup(String id, ForceLaws forceLaws) throws IllegalArgumentException{
        if (id == null || forceLaws == null || !(id.trim().length() > 0)){
            throw new IllegalArgumentException("Invalid parameters for BodiesGroup");
        }
        else {
            this.id = id;
            this.forceLaws = forceLaws;
            this.bodies = new ArrayList<>();
            this.bodiesRO = Collections.unmodifiableList(bodies);
        }
    }
    public String getId(){
        return this.id;
    }
    //cambia las leyes de fuerza a fl
    public void setForceLaws(ForceLaws fl){
        if (fl == null)
        {
            throw new IllegalArgumentException("Invalid value for fl: null");
        }
        else {
            this.forceLaws = fl;
        }
    }
    void addBody(Body b){
        if (b == null || b.getId() == null)
        {
            throw new IllegalArgumentException("Cannot add body with null values");
        }
        for (Body body : bodies) {
            if (body.getId().equals(b.getId()))
            {
                throw new IllegalArgumentException("Body with the same id already exists");
            }
        }
        this.bodies.add(b);
    }
    void advance(double dt)
    {
        if (dt <= 0)
        {
            throw new IllegalArgumentException("Delta time must be positive");
        }
        for (Body body: bodies)
        {
            body.resetForce();
        }
        forceLaws.apply(bodies);
        for (Body body: bodies)
        {
            body.advance(dt);
        }
    }
    public JSONObject getState(){
        JSONObject jo = new JSONObject();
        jo.put("id", this.getId());
        JSONArray ja = new JSONArray();
        for (Body body: bodies)
        {
            ja.put(body.getState());
        }
        jo.put("bodies", ja);

        return jo;
    }
    public String toString(){
        return this.getState().toString();
    }

    public String getForceLawsInfo(){
        return forceLaws.toString();
    }

    @Override
    public Iterator<Body> iterator() {
        return bodiesRO.iterator();
    }
    
    public BodiesGroup getCopy() {
    	BodiesGroup bg = new BodiesGroup(this.getId(), this.forceLaws);
    	
    	for(Body b : bodies) {
    		bg.addBody(b);
    	}
    	
    	return bg;
    }
    
    public void deleteBody(Body b) {
    	List<Body> bs = new ArrayList<>(bodies);
    	for(Body bi : bs) {
    		if(bi.getId().equals(b.getId())) {
    			if(bodies.remove(bi)) {
    				System.out.println("df");
    			}
    		}
    	}
    	
    }
}

