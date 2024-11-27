package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {
    public NoForceBuilder(){
        super("nf", "No force builder");
    }
    @Override
    protected ForceLaws createInstance(JSONObject data) {
        return new NoForce();
    }
    @Override
    public JSONObject getInfo(){
        JSONObject info = new JSONObject();
        info.put("type", "nf");
        info.put("desc", "No Force");
        JSONObject jo = new JSONObject();
        info.put("data", jo);
        return info;
    }
}
