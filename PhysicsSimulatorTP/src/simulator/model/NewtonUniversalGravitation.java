package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class NewtonUniversalGravitation implements ForceLaws{

    //Universal gravitation constant
    private final double g;
    public NewtonUniversalGravitation(double g)
    {
        if (g <= 0 )
        {
            throw new IllegalArgumentException("G cannot be <= 0");
        }
        else
        {
            this.g = g;
        }
    }

    @Override
    public void apply(List<Body> bs) {
        //Initialize result to 0,0
        Vector2D result;
        for (Body bodyI : bs)
        {
            if (bodyI.getMass() == 0)
            {
                // a and v -> 0,0 (result)
                bodyI.setVelocity(new Vector2D());
            }
            else {
                Vector2D distance;
                Vector2D dij;
                for (Body bodyJ : bs) {
                    //pj - pi
                    distance = bodyJ.getPosition().minus(bodyI.getPosition());
                    dij = distance.direction();
                    // distance == 0?
                    if (!distance.equals(new Vector2D(0, 0))) {
                        double fMagnitude = g * bodyI.getMass() * bodyJ.getMass() / Math.pow(bodyJ.getPosition().distanceTo(bodyI.getPosition()), 2);
                        result = dij.scale(fMagnitude);
                        bodyI.addForce(result);
                    }

                }
            }
        }
    }
    @Override
    public String toString() {
        return "Newton's Universal Gravitation with G = " + g;
    }
}
