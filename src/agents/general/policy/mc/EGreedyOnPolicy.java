/**
 * 
 */
package agents.general.policy.mc;

import java.util.Random;

import agents.general.state.DiscreteState;
import agents.general.state.StateActionAVG;
import agents.general.state.StateActionAVG.ActionAVGs;

/**
 * @author bob
 *
 */
public class EGreedyOnPolicy {

	final double eps;
	
	StateActionAVG policy = null; 
	Random rand = new Random();
	
	
	public EGreedyOnPolicy(double epsilon, int actionsNumber) {
		eps = epsilon;
		policy = new StateActionAVG( actionsNumber );
	}
	
	public int select(DiscreteState state) {
		ActionAVGs list = policy.getActions(state);
		double rval = rand.nextDouble();
		return list.select(rval);
	}
	
	public void improve(StateActionAVG qFunction, DiscreteState state) {
		ActionAVGs qList = qFunction.getActions(state);
		final int greedyAction = qList.bestByMaxValue();
		
		ActionAVGs actions = policy.getActions(state);
		final int aSize = actions.size();
		final double epart = eps / aSize;
		for( int a = 0; a<aSize; ++a) {
			/**
			 * 'epart' or '1.0 - eps + epart'
			 */
			double newVal = epart;
			if (a == greedyAction) {
				/// greedy case
				newVal += 1.0 - eps;
			} 
			/// else regular case
			actions.set(a, newVal);
		}
	}
	
}
