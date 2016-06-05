/**
 * 
 */
package agents.general.policy.td;

import rlVizLib.general.ParameterHolder;
import agents.general.policy.PolicyControl;
import agents.general.state.StateTranslator;


/**
 * @author bob
 *
 */
public class Sarsa extends TDLearn {

	public Sarsa(StateTranslator range, ParameterHolder params) {
		super(range, params);
	}

	@Override
	protected void learn(double reward, Step currStep) {
		stepLearn(reward, currStep);
		/// stepLearn(0.0, currStep);
	}
	
//	protected void learn(double endStepReward) {
//		stepLearn(-1.0, lastStep);
//	}

	protected void stepLearn(double reward, Step currStep) {
		double delta = reward + gamma * currStep.value() - lastStep.value();
		double newVal = lastStep.value() + alpha * delta;
		
		/// Q(S,A) = Q(S,A) + alpha * [R + gamma * Q(S',A') - Q(S,A)]
		lastStep.setValue( newVal );
	}
	
	@Override
	public void end(double reward) {
		if (printData) {
			//		System.out.println("ef:\n" + ef.print() );
			// System.out.println("qf:\n" + qf.print() );			
		}
		super.end(reward);
	}
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class Factory extends TDLearn.Factory {
		
		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			return new Sarsa(range, params);
		}
	}

}
