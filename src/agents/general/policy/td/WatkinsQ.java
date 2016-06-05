/**
 * 
 */
package agents.general.policy.td;

import java.util.Iterator;

import rlVizLib.general.ParameterHolder;
import agents.general.policy.PolicyControl;
import agents.general.state.StateTranslator;

/**
 * @author bob
 * 
 * Tabular Sarsa
 *
 */
public class WatkinsQ extends TDLambda {
	
	public WatkinsQ(StateTranslator range, ParameterHolder params) {
		super(range, params); 
	}

	@Override
	protected void update(double learnFraction, double decayFactor) {
		final int bestAction = currStep.maxAction();
		
		Iterator<EligibilityTrace.Item> iter = ef.iterator();
		while(iter.hasNext()) {
			EligibilityTrace.Item item = iter.next();
			ETValue etVal = item.value;
			
			/// Q-function change
			final double qchange = learnFraction * etVal.value();
			qf.add(item.state, item.action, qchange);
			
			/// trace change
			if ( item.action == bestAction ) {
				etVal.multiply( decayFactor );
			} else {
				etVal.set( 0.0 );
			}
		}
	}
	
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class Factory extends TDLambda.Factory {
	
		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			return new WatkinsQ(range, params);
		}
	}

}
