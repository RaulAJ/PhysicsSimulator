package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
/**
 * v = velocity vector
 * f = force vector
 * p = position vector
 * m = mass (double)
 * */
public abstract class Body {
    protected String id, gid;
    protected Vector2D v, f, p;
    protected double m;
    public Body(String id, String gid, Vector2D p,Vector2D v, double m)
    {
        if (id == null || !(id.trim().length() > 0) || gid == null || !(gid.trim().length() > 0) ||
                v == null || p == null || m < 0 || m == 0.0)
        {
            throw new IllegalArgumentException("Cannot build Body. Invalid parameters.");
        }
        this.id = id;
        this.gid = gid;
        this.v = v;
        this.p = p;
        this.m = m;
        this.f = new Vector2D();
    }

    public String getId()
    {
        return id;
    }
    public String getgId()
    {
        return gid;
    }
    public double getMass()
    {
        return m;
    }
    public Vector2D getPosition(){
        return p;
    }
    public Vector2D getVelocity()
    {
        return v;
    }
    public Vector2D getForce()
    {
        return f;
    }
    public void setVelocity(Vector2D v)
    {
        this.v = v;
    }

    public void addForce(Vector2D force)
    {
        if (this.f != null)
        {
            this.f = this.f.plus(force);
        }
    }
    public void resetForce()
    {
        this.f = new Vector2D();
    }
    abstract void advance(double pos);
    @Override
    public int hashCode(){
        return  -1;
    }

    

    public JSONObject getState()
    {
        JSONArray position = new JSONArray();
        position.put(p.getX());
        position.put(p.getY());

        JSONArray velocity = new JSONArray();
        velocity.put(v.getX());
        velocity.put(v.getY());
        JSONArray force = new JSONArray();
        force.put(f.getX());
        force.put(f.getY());

        JSONObject jo = new JSONObject();
        jo.put("id", id);
        jo.put("m", m);
        jo.put("p", position);
        jo.put("v", velocity);
        jo.put("f", force);
        return jo;
    }

    public String toString(){
        return getState().toString();
    }
}
