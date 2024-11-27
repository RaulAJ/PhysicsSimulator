package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.StationaryBody;

public class StationaryBodyBuilder extends Builder<Body>{
    public StationaryBodyBuilder(){
        super("st_body", "Stationary body builder");
    }
    @Override
    protected Body createInstance(JSONObject data) {
        String id, gid;
        double m;
        Vector2D p;
        if (!data.has("id"))
        {
            throw new IllegalArgumentException("Must have id");
        }
        id = data.getString("id");

        if (!data.has("gid"))
        {
            throw new IllegalArgumentException("Must have gid");
        }
        gid = data.getString("gid");

        if (!data.has("p"))
        {
            throw new IllegalArgumentException("Must have p");
        }
        JSONArray aux = data.getJSONArray("p");

        if (aux.length() != 2)
        {
            throw new IllegalArgumentException("p must be 2D");
        }
        else
        {
            p = new Vector2D(aux.getDouble(0), aux.getDouble(1));
        }

        if (!data.has("m"))
        {
            throw new IllegalArgumentException("Must have m");
        }

        m = data.getDouble("m");
        return new StationaryBody(id, gid, p, m);
    }
}
