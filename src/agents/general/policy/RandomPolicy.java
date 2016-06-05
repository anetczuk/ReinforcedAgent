/**
 * 
 */
package agents.general.policy;

import java.util.Random;

import org.rlcommunity.rlglue.codec.types.Observation;

import agents.general.AgentAction;
import agents.general.AgentFactory;
import agents.general.state.StateTranslator;
import rlVizLib.general.ParameterHolder;

/**
 * @author bob
 *
 */
public class RandomPolicy implements PolicyControl {

	StateTranslator range = null;
	Random random = null;
	
	
	public RandomPolicy(StateTranslator range, int seed) {
		this.range = range;
		if (seed == 0) {
			random = new Random();			
		} else {			
			random = new Random( seed );
		}
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
		return randomAction();
	}

	@Override
	public AgentAction step(double reward, Observation nextState) {
		return randomAction();
	}

	private AgentAction randomAction() {
		int act = random.nextInt( range.actionsSize );
		return new AgentAction( act );
	}

	@Override
	public void end(double reward) {
		/// do nothing
	}

//  private void setRandomActions(Action action) {
//  for (int i = 0; i < TSO.getNumDiscreteActionDims(); i++) {
//      IntRange thisActionRange = TSO.getDiscreteActionRange(i);
//      action.intArray[i] = random.nextInt(thisActionRange.getRangeSize()) + thisActionRange.getMin();
//  }
//  for (int i = 0; i < TSO.getNumContinuousActionDims(); i++) {
//      DoubleRange thisActionRange = TSO.getContinuousActionRange(i);
//      action.doubleArray[i] = random.nextDouble() * (thisActionRange.getRangeSize()) + thisActionRange.getMin();
//  }
//}
	
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class Factory extends AgentFactory {
		
		private static final String seedField = "Random seed";
		
		
		@Override
		public void initParams(ParameterHolder params) {
			params.addIntegerParam( seedField, 0);
		}

		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			int seed = params.getIntegerParam( seedField );
			return new RandomPolicy(range, seed);
		}
	}
}
