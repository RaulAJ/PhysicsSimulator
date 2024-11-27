package simulator.model;

import simulator.misc.Vector2D;

public class Rocket extends Body{

	private double comb;
	
	private double l;
	
	public Rocket(String id, String gid, Vector2D p, Vector2D v, double m, double comb, double l) {
		super(id, gid, p, v, m);
		this.comb = comb;
		this.l = l;
	}

	@Override
	void advance(double t) {
		if(comb > 0) {
			this.p = p.plus(v.scale(t));
			comb -= l;
		}
		else {
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

}
