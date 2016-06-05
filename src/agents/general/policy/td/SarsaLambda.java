/**
 * 
 */
package agents.general.policy.td;

import java.util.Iterator;

import rlVizLib.general.ParameterHolder;
import agents.general.math.Value;
import agents.general.policy.PolicyControl;
import agents.general.state.StateTranslator;

/**
 * @author bob
 * 
 * Tabular Sarsa
 *
 */
public class SarsaLambda extends TDLambda {
	
	public SarsaLambda(StateTranslator range, ParameterHolder params) {
		super(range, params); 
	}
	
	@Override
	protected void update(double learnFraction, double decayFactor) {
		Iterator<EligibilityTrace.Item> iter = ef.iterator();
		while(iter.hasNext()) {
			EligibilityTrace.Item item = iter.next();
			ETValue etVal = item.value;
			 
			/**
			 * Q-function change.
			 * In case of gamma = 0.0
			 * 		Q(S,A) = Q(S,A) + alpha * [R + gamma * Q(S',A') - Q(S,A)]
			 */
			final double qchange = learnFraction * etVal.value();
			/// System.out.println("qchange: " + Double.toString(qchange) );
			Value qval = qf.get(item.state, item.action);
			final double newVal = qval.value() + qchange;
			qval.set( newVal );
			//qf.add(item.state, item.action, qchange);
			
			/// trace change
			etVal.multiply( decayFactor );		
		}
	}
	
	
	//=============================================================
	
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class Factory extends TDLambda.Factory {

		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			return new SarsaLambda(range, params);
		}
		
	}

}
