/**
 * 
 */
package agents.general.policy.mc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.rlcommunity.rlglue.codec.types.Observation;

import agents.general.AgentAction;
import agents.general.policy.PolicyControl;
import agents.general.state.DiscreteState;
import agents.general.state.StateTranslator;

/**
 * @author bob
 *
 */
public class BoxCart implements PolicyControl {

	StateTranslator range = null;
	double rewardSum = 0;
	
	Boxes boxes = new Boxes();
	
	
	public BoxCart(StateTranslator range) {
		this.range = range;
	}

	@Override
	public void setExplore(boolean state) {
		System.out.println("........... Not implemented ..........");
	}
	
	@Override
	public void setPrintData(boolean state) {
		System.out.println("........... Not implemented ..........");
	}
	
	@Override
	public void setLogStep(boolean state) {
		System.out.println("........... Not implemented ..........");
	}
	
	@Override
	public AgentAction start(Observation nextState) {
		rewardSum = 0;
		boxes.clear();
		
		DiscreteState discreteState = range.convert(nextState);
		int choice = boxes.add(discreteState, rewardSum);
		
		return new AgentAction( choice );
	}

	@Override
	public AgentAction step(double reward, Observation nextState) {
		rewardSum += 1;
		
		DiscreteState discreteState = range.convert(nextState);
		int choice = boxes.add(discreteState, rewardSum);
		
		return new AgentAction( choice );
	}

	@Override
	public void end(double reward) {
		rewardSum += 1;
		boxes.calculate( rewardSum );
		
		{
			StringBuilder build = new StringBuilder();
			build.append( "known states: " );
			build.append( Integer.toString( boxes.size() ) );
			build.append( " of " );
			build.append( Integer.toString( range.totalObservations() ) );
			build.append( "\n" );
			System.out.println( build.toString() );
		}
	}

}


/**
 * 
 * @author bob
 *
 */
class Boxes {
	
	static class Box {
		ArrayList<Double> times = new ArrayList<Double>();
		
		double ll = 1.0;
		double lu = 1.0;
		double rl = 1.0;
		double ru = 1.0;
		
		int action = 0;
		
		
		public Box() {
		}

		public void add(double value) {
			times.add(value);
		}

		public void clear() {
			times.clear();
		}
		
		public void update(final double totalReward, final double dk, final double target) {
			ll = ll * dk + rewardSum(totalReward);
			lu = lu * dk + times.size();
			rl = rl * dk;
			ru = ru * dk;
			
			final double k = 1.0;
			
			double ktarget = k * target;
			double luk = lu + k;
			double lval = (ll + ktarget) / luk;
			double ruk = ru + k;
			double rval = (rl + ktarget) / ruk;
			
			if (lval > rval)
				action = 0;
			else
				action = 1;
		}
		
		double rewardSum(double totalReward) {
			double sum = 0.0;
			int num = times.size();
			for( int i=0; i<num; ++i) {
				sum += ( totalReward - times.get(i) );
			}
			return sum;
		}
		
	}
	
	Map<DiscreteState, Box > map = new HashMap<DiscreteState, Box >();
	
	double gl = 0.0;
	double gu = 0.0;
	double metris = 0.0;
	double target = 0.0;
	
	
	public Boxes() {
	}
	
	public int size() {
		return map.size();
	}

	public void calculate(double totalReward) {
		final double dk = 0.5;			// less than 1
		
		gl = gl * dk + totalReward;
		gu = gu * dk + 1.0;
		metris = gl / gu;
		
		final double c0 = 0.0;			// greater equal 0
		final double c1 = 1.0;			// greater equal 1
		target = c0 + c1 * metris;
		
		for( Box box : map.values()) {
			box.update( totalReward, dk, target );
		}
	}

	public void clear() {
		for( Box box : map.values()) {
			box.clear();
		}
	}

	public int add(DiscreteState state, double value) {
		Box box = map.get(state);
		if (box == null) {
			box = new Box();
			map.put(state, box);
		}
		box.add(value);
		
		return box.action;
	}
	
}
