package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsTwoFixedPoints;

public class MovingTowardsTwoFixedPointsBuilder extends Builder<ForceLaws>{

	public  MovingTowardsTwoFixedPointsBuilder(){
        super("mt2fp", "Moving towards two fixeds point Builder");
    }
	@Override
	protected ForceLaws createInstance(JSONObject data) {
		// TODO Auto-generated method stub
		Vector2D c1 = new Vector2D(0,0);
		Vector2D c2 = new Vector2D(0,0);
		double g1 = 9.81;
		double g2 = 9.81;
		if(data.has("c1")) {
			JSONArray aux = data.getJSONArray("c1");
			c1 = new Vector2D(aux.getDouble(0), aux.getDouble(1));
		}
		if(data.has("c2")) {
			JSONArray aux = data.getJSONArray("c2");
			c2 = new Vector2D(aux.getDouble(0), aux.getDouble(1));
		}
		if (data.has("g1"))
        {
            g1 = data.getDouble("g1");
        }
		if (data.has("g2"))
        {
            g2 = data.getDouble("g2");
        }
		return new MovingTowardsTwoFixedPoints(c1, c2, g1, g2);
	}
	
	@Override
    public JSONObject getInfo() {
        JSONObject info = new JSONObject();
        JSONObject data = new JSONObject();
        info.put("type", "mt2fp");
        info.put("desc", "Moving towards two fixeds points");

        data.put("c1", "the first point towards bodies move\r\n"
        		+ "(a json list of 2 numbers, e.g., [100.0,50.0])");
        data.put("c2", "the second point towards bodies move\r\n"
        		+ "(a json list of 2 numbers, e.g., [100.0,50.0])");

        data.put("g1", "the length of the first acceleration vector\r\n"
        		+ "(a number)");
        data.put("g2", "the length of the second  acceleration vector\r\n"
        		+ "(a number)");
        
        info.put("data", data);
        return info;
    }

}
