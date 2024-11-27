package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public class MovingTowardsFixedPoint implements ForceLaws{
    private final Vector2D c;
    private final double g;
    public MovingTowardsFixedPoint(Vector2D c, double g)
    {
        if (g <= 0 || c == null) {
            throw new IllegalArgumentException("g cannot be negative and c cannot be null");
        }
        else {
            this.c = c;
            this.g = g;
        }
    }
    @Override
    public void apply(List<Body> bs) {
        for (Body body : bs) {
            // di = c - pi .direction?
            Vector2D di = this.c.minus(body.getPosition()).direction();
            Vector2D forceResult = di.scale(body.getMass()*this.g);
            body.addForce(forceResult);
        }
    }
    //devuelva una pequeña descripción de la correspondiente fuerza
    @Override
    public String toString(){
        return "Moving towards " + c + " with constant acceleration " + g;
    }
}
