/**
 * 
 */
package agents.general.policy.td;

import org.rlcommunity.rlglue.codec.types.Observation;

import rlVizLib.general.ParameterHolder;
import agents.general.AgentAction;
import agents.general.AgentFactory;
import agents.general.math.AVG;
import agents.general.math.RangedAVG;
import agents.general.math.Value;
import agents.general.math.ValueCount;
import agents.general.math.function.DiscreteList;
import agents.general.policy.EGreedyPolicy;
import agents.general.policy.PolicyControl;
import agents.general.state.DiscreteState;
import agents.general.state.StateActionCount;
import agents.general.state.StateTranslator;

/**
 * @author bob
 *
 */
public abstract class TDLearn implements PolicyControl {

	/**
	 * 
	 * @author bob
	 *
	 */
	public class Step {
		public DiscreteState state = null;
		public DiscreteList<? extends Value> actionsList = null;
		public int action = 0;
		Value value = null;
		
		public Step() {
		}
				
		AgentAction cartAction() {
			return new AgentAction( action );
		}
		
		public double value() {
			return value.value();
		}
		
		public double value(int action) {
			return actionsList.get(action).value();
		}
		
		public int maxAction() {
			return actionsList.bestByMaxValue();
		}
		
		public double maxValue() {
			return actionsList.maxValue();
		}

		public void setValue(double newValue) {
			value.set(newValue);
		}
	}
	
	
	StateTranslator range = null;
	
	protected StateActionCount qf = null;
	protected Step lastStep = null;
	
	EGreedyPolicy policy;
	
	protected double alpha;		// learning factor
	protected double gamma;		// discount factor
	
	int episode = 0;
	protected double rewardSum = 0.0;
	double bestMinReward = Double.MAX_VALUE;
	double bestMaxReward = 0.0;
	protected AVG rewardAvg = new RangedAVG(20);

	protected boolean printData = true;
	protected boolean logStep = false;
	
	
	public TDLearn(StateTranslator range, double epsilon, double alpha, double gamma) {
		super();
		this.range = range;
		policy = new EGreedyPolicy(epsilon);
		this.alpha = alpha;
		this.gamma = gamma;
		
		//actionFunction = new StateActionValues(range.actions);
		qf = new StateActionCount(range);
	}
	
	public TDLearn(StateTranslator range, ParameterHolder params) {
		super();
		this.range = range;
		
		double epsilon = params.getDoubleParam(Factory.exploreField);
		double alpha = params.getDoubleParam(Factory.learnField);
		double gamma = params.getDoubleParam(Factory.discountField);
		
		policy = new EGreedyPolicy(epsilon);
		this.alpha = alpha;
		this.gamma = gamma;
		
		//actionFunction = new StateActionValues(range.actions);
		qf = new StateActionCount(range);
	}

	@Override
	public void setExplore(boolean state) {
		policy.setExplore( state );
	}
	
	@Override
	public void setPrintData(boolean state) {
		printData = state;
	}
	
	@Override
	public void setLogStep(boolean state) {
		logStep = state;
	}
	
	@Override
	public AgentAction start(Observation nextState) {
		policy.reset();
		++episode;
		rewardSum = 0;
		
		DiscreteState currState = range.convert( nextState );
		lastStep = calculateStep(currState);
		
		return lastStep.cartAction();
	}

	@Override
	public AgentAction step(double reward, Observation nextState) {
		rewardSum += 1;
		
		DiscreteState currState = range.convert(nextState);
		Step currStep = calculateStep(currState);
				
		learn(reward, currStep);
		
		lastStep = currStep;
		return currStep.cartAction();
	}
	
	protected abstract void learn(double reward, Step currStep);

	protected void learn(double endStepReward) {
		/// terminal state set to 0.0
		lastStep.setValue( 0.0 );
	}
	
	Step calculateStep(DiscreteState state) {
		DiscreteList<ValueCount> actionsList = qf.getActions( state );
		//int visitedNum = actionsList.count();
		//int currAction = policy.action( actionsList, visitedNum );
		
		int currAction = policy.actionExponential( actionsList );
		
		Value currVal = actionsList.get(currAction);
		
		Step step = new Step();
		step.state  = state;
		step.actionsList = actionsList;
		step.action = currAction;
		step.value  = currVal;
		
		if (logStep || policy.takenExplore) {
			StringBuilder build = new StringBuilder();
			build.append( policy.step );
			build.append( ": " );
			build.append( state.print() );
			build.append( " -> " );
			build.append( currAction );
			if (policy.takenExplore) {
				build.append( " (explore: " );
				build.append( policy.threshold );
				build.append( ")" );
			}
			System.out.println( build.toString() );
		}
		
		return step;
	}

	@Override
	public void end(double reward) {
		learn(reward);
		
		bestMinReward = Math.min(bestMinReward, rewardSum);
		bestMaxReward = Math.max(bestMaxReward, rewardSum);
		rewardAvg.add(rewardSum);
		
		if (episode%1000 == 0) {
			//store data
			String filename = "data/asfunc_" + Integer.toString(episode) + ".txt";
			range.store(filename);
			qf.store(filename);
		}
		
		if (printData == false)
			return;
		
		System.out.println("curr reward:    " + Double.toString(rewardSum));
		System.out.println("best reward:    " + Double.toString(bestMinReward) + " " + Double.toString(bestMaxReward));
		System.out.println("reward average: " + Double.toString( rewardAvg.value() ));
		{
			StringBuilder build = new StringBuilder();
			build.append( "known states: " );
			build.append( Integer.toString( qf.size() ) );
			build.append( " of " );
			build.append( Integer.toString( range.totalObservations() ) );
			build.append( "\n" );
			System.out.println( build.toString() );
		}
	}

	/**
	 * 
	 * @author bob
	 *
	 */
	public static abstract class Factory extends AgentFactory {
		
		private static final String learnField = "learn(alpha)";
		private static final String discountField = "discount(gamma)";
		private static final String exploreField = "explore(epsilon)";
		
		
		@Override
		public void initParams(ParameterHolder params) {
			//addDoubleParam( params, learnField, 0.3);
			addDoubleParam( params, learnField, 0.3);
			addDoubleParam( params, discountField, 0.8);
			addDoubleParam( params, exploreField, 0.05);
		}
		
	}

}
