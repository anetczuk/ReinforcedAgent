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
public class QLearn extends TDLearn {
	
	public QLearn(StateTranslator range, ParameterHolder params) {
		super(range, params);
	}

	@Override
	protected void learn(double reward, Step currStep) {
		double param = reward + gamma * currStep.maxValue() - lastStep.value();
		double newVal = lastStep.value() + alpha * param;
		lastStep.setValue( newVal );
	}

	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class Factory extends TDLearn.Factory {
		
		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			return new QLearn(range, params);
		}
	}
	
}
