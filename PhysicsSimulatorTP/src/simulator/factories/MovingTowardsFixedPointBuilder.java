package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

    public  MovingTowardsFixedPointBuilder(){
        super("mtfp", "Moving towards fixed point Builder");
    }
    @Override
    protected ForceLaws createInstance(JSONObject data) {
        Vector2D c = new Vector2D(0,0);
        double g = 9.81;
        //c y g son opcionales
        if (data.has("c"))
        {
            JSONArray aux = data.getJSONArray("c");
            c = new Vector2D(aux.getDouble(0), aux.getDouble(1));
        }
        if (data.has("g"))
        {
            g = data.getDouble("g");
        }
        return new MovingTowardsFixedPoint(c, g);
    }
    @Override
    public JSONObject getInfo() {
        JSONObject info = new JSONObject();
        JSONObject data = new JSONObject();
        info.put("type", "mtfp");
        info.put("desc", "Moving towards a fixed point");

        data.put("c", "the point towards which bodies move (e.g., [100.0,50.0])");
        data.put("g", "the length of the acceleration vector (a number)");

        info.put("data", data);
        return info;
    }
}
