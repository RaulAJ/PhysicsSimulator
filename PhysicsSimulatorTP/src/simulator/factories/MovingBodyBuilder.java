package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MovingBody;


/**
 * Todos los “builders”
 * deben lanzar excepciones cuando los datos de entrada no sean
 * válidos
 * */

public class MovingBodyBuilder extends Builder<Body>{

    public MovingBodyBuilder(){
        super("mv_body", "Moving body builder");

    }

    @Override
    protected Body createInstance(JSONObject data) {

        String id, gid;
        double m;
        Vector2D p, v;

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

        if (!data.has("m"))
        {
            throw new IllegalArgumentException("Must have m");
        }
        m = data.getDouble("m");

        if (!data.has("p"))
        {
            throw new IllegalArgumentException("Must have p");
        }
        //Check if p and v are 2D vectors
        JSONArray aux = data.getJSONArray("p");
        if (aux.length() != 2)
        {
            throw new IllegalArgumentException("p must be 2D");
        }
        else
        {
            p = new Vector2D(aux.getDouble(0), aux.getDouble(1));
        }

        if (!data.has("v"))
        {
            throw new IllegalArgumentException("Must have v");
        }
        aux = data.getJSONArray("v");
        if (aux.length() != 2)
        {
            throw new IllegalArgumentException("v must be 2D");
        }
        else
        {
            v = new Vector2D(aux.getDouble(0), aux.getDouble(1));
        }
        return new MovingBody(id, gid, p, v, m);
    }

}
