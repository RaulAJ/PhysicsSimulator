package simulator.factories;

import org.json.JSONObject;
import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws>
{
    private double _gConstant = 6.67E-11;
    public NewtonUniversalGravitationBuilder(){
        super("nlug", "Newton universal gravitation builder");
    }
    @Override
    protected ForceLaws createInstance(JSONObject data) {
        if (data.has("G")) {
            double val = data.getDouble("G");
            if (val == 0)
            {
                throw new IllegalArgumentException("G cannot be 0");
            }
            else
            {
                _gConstant = data.getDouble("G");
            }
        }
        return new NewtonUniversalGravitation(_gConstant);
    }
    @Override
    public JSONObject getInfo() {
        JSONObject info = new JSONObject();
        JSONObject data = new JSONObject();
        info.put("type", "nlug");
        info.put("desc", "Newton's law of universal gravitation");
        data.put("G", "the gravitational constant (a number)");
        info.put("data", data);
        return info;
    }
}
