package simulator.model;

import org.json.JSONObject;

import java.util.List;

public class NoForce implements ForceLaws{
    @Override
    public void apply(List<Body> bs) {

    }
    public NoForce()
    {

    }
    @Override
    public String toString(){
        return "No force";
    }



}
