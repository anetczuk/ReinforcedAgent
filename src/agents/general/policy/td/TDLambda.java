/**
 * 
 */
package agents.general.policy.td;

import org.rlcommunity.rlglue.codec.types.Observation;

import rlVizLib.general.ParameterHolder;
import agents.general.AgentAction;
import agents.general.state.StateTranslator;

/**
 * @author bob
 * 
 * Tabular Sarsa
 *
 */
public abstract class TDLambda extends TDLearn {

	EligibilityTrace ef = null;
	double lambda;
	
	protected Step currStep = null;
	
	
	public TDLambda(StateTranslator range, ParameterHolder params) {
		super(range, params);
		
		ef = new EligibilityTrace( range.actionsSize );
		lambda = params.getDoubleParam( Factory.decayField ); 
	}

	@Override
	public AgentAction start(Observation nextState) {
		ef.clear();
		return super.start(nextState);
	}
	
	@Override
	protected void learn(double reward, Step currStep) {
		stepLearn(reward, currStep);
	}
	
	@Override
	protected void learn(double reward) {
//		if ( rewardSum < (rewardAvg.value()-1.0) ) {
//			/// extra reward
//			stepLearn( 2.0 * reward, lastStep);
//		} else {
//			/// standard reward
//			stepLearn(reward, lastStep);			
//		}
		
		stepLearn(reward, lastStep);			
		
		//stepLearn(-1.0, lastStep);
		//stepLearn( -2*rewardSum, lastStep);
	}
	
	protected void stepLearn(double reward, Step currStep) {
		this.currStep = currStep;
		
		//ef.add(lastStep.state, lastStep.action, 1.0);
		ef.set(lastStep.state, lastStep.action, 1.0);
		
		/// R + gamma * Q(S',A') - Q(S,A)
		final double delta = reward + gamma * currStep.value() - lastStep.value();
		
		/// alpha * [R + gamma * Q(S',A') - Q(S,A)] 
		final double learnFraction = alpha * delta;
		
		final double decayFactor = gamma * lambda;
		
		update(learnFraction, decayFactor);
	}
	
	protected abstract void update(double learnFraction, double decayFactor);

	@Override
	public void end(double reward) {
		if (printData) {
//		System.out.println("ef:\n" + ef.print() );
//		System.out.println("qf:\n" + qf.print() );
		}
		super.end(reward);
	}
	
	
	//=============================================================
	
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static abstract class Factory extends TDLearn.Factory {
		
		private static final String decayField = "decay(lambda)";
		
		
		@Override
		public void initParams(ParameterHolder params) {
			super.initParams(params);
			
			addDoubleParam( params, decayField, 0.95);
		}

	}

}
