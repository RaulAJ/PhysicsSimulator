package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.*;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class Controller {
    private PhysicsSimulator _sim;

    private Factory<Body> bodyFactory;
    private Factory<ForceLaws> forceLawsFactory;
    public Controller(PhysicsSimulator physicsSimulator, Factory<Body> bodyFactory, Factory<ForceLaws> forceLawsFactory){
        this._sim = physicsSimulator;
        this.bodyFactory = bodyFactory;
        this.forceLawsFactory = forceLawsFactory;
    }
    /**
     * estructura JSON de la forma:
     * { “groups”: [g1,. . .], “laws”: [l1,. . .], “bodies”: [bb1,...] }
     * */
    public void loadData(InputStream in)
    {
        //1. Crear grupos; 2. Crear bodies; 3. Parsear leyes
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray groups = jsonInput.getJSONArray("groups");
        for (int i = 0; i < groups.length(); i++)
        {
            this._sim.addGroup(groups.getString(i));
        }
        JSONArray bodyList = jsonInput.getJSONArray("bodies");
        for (int i = 0; i < bodyList.length(); i++)
        {
            this._sim.addBody(bodyFactory.createInstance(bodyList.getJSONObject(i)));
        }

        //clave laws es opcional
        if(jsonInput.has("laws")){
            JSONArray lawList = jsonInput.getJSONArray("laws");
            for (int i = 0; i< lawList.length(); i++)
            {
                //li es una estructura JSON de la forma:
                //{ “id”: id, “laws”: laws}**
                JSONObject li = lawList.getJSONObject(i);
                this._sim.setForceLaws(li.getString("id"), forceLawsFactory.createInstance(li.getJSONObject("laws")));
            }
        }
    }
    /**
     * ejecuta el simulador n
     * pasos, y muestra los diferentes estados obtenidos en out, utilizando
     * el siguiente formato JSON:
     * { "states": [s0,s1,...,sn] }
     * */
    public void run(int n, OutputStream out)
    {
        PrintStream p = new PrintStream(out);
        p.println("{");
        p.println("\"states\": [");
        //Step 0
        p.println(this._sim.getState().toString() + ",");
        for (int i = 0; i < n; i++)
        {
            this._sim.advance();
            p.println(this._sim.getState().toString());
            if ( n - i != 1)
            {
                p.print(",");
            }
        }
        p.println("]");
        p.println("}");

    }
    public void reset()
    {
        this._sim.reset();
    }
    public void setDeltaTime(double dt){
        this._sim.setDeltaTime(dt);
    }
    public void addObserver(SimulatorObserver o){
        this._sim.addObserver(o);
    }
    public void removeObserver(SimulatorObserver o)
    {
        this._sim.removeObserver(o);
    }
    public List<JSONObject> getForceLawsInfo(){
        return this.forceLawsFactory.getInfo();
    }
    public double getDeltaTime() {
    	return _sim.getDeltaTime();
    }
    public void setForceLaws(String gId, JSONObject info){
        ForceLaws fl = forceLawsFactory.createInstance(info);
        this._sim.setForceLaws(gId, fl);
    }
    //ejecuta el simulador n pasos, sin mostrar nada en un OutputStream
    public void run(int n)
    {
        for (int i = 0; i < n; i++ )
        {
            _sim.advance();
        }
    }
    
    public void removeBody(String id) {
    	_sim.removeBody(id);
    }
    
}
