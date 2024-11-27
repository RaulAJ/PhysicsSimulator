package simulator.model;

import simulator.misc.Vector2D;


public class MovingBody extends Body{
    public MovingBody(String id, String gid, Vector2D p, Vector2D v, double m) {
        super(id, gid, p, v, m);

    }
    @Override
    public void advance(double t){

        //Acceleration vector
        Vector2D a;
        if (this.m == 0)
        {
            a = new Vector2D();
        }
        else {
            // f * (1/m)
            a = f.scale(1/m);
        }
        /**
         * aux = 1/2 * a * t^2
         * result = p + vt + aux
         * */
        Vector2D aux = a.scale(0.5 * t * t);
        this.p = aux.plus(v.scale(t)).plus(p);
        this.v = v.plus(a.scale(t));
    }

}
