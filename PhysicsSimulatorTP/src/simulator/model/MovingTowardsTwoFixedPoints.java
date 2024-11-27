package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsTwoFixedPoints implements ForceLaws{
	
	private final Vector2D c1, c2;
	private final double g1, g2;
	
	public MovingTowardsTwoFixedPoints(Vector2D c1, Vector2D c2, double g1, double g2) {
		if(g1 <= 0 || g2 <= 0 || c1 == null || c2 == null) {
			throw new IllegalArgumentException("E");
		}
		else {
			this.c1 = c1;
			this.c2 = c2;
			this.g1 = g1;
			this.g2 = g2;
		}
	}
	
	@Override
	public void apply(List<Body> bs) {
		// TODO Auto-generated method stub
		for(Body b : bs) {
			Vector2D d1, d2;
			d1 = this.c1.minus(b.getPosition()).direction();
			d2 = this.c2.minus(b.getPosition()).direction();
			Vector2D aux1, aux2, f;
			aux1 = d1.scale(g1);
			aux2 = d2.scale(g2);
			f = aux1.plus(aux2).scale(b.getMass());
			b.addForce(f);
		}
	}
	
	public String toString() {
		return "Moving towards two fixedpoints";
	}

}
