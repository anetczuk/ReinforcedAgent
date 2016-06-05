/**
 * 
 */
package agents.general.policy.mc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.rlcommunity.rlglue.codec.types.Observation;

import agents.general.AgentAction;
import agents.general.state.DiscreteState;
import agents.general.state.StateActionAVG;
import agents.general.state.StateTranslator;
import agents.general.state.StateActionAVG.ActionAVGs;

/**
 * @author bob
 *
 */
public class MCCart {
	
	StateTranslator range = null;
	
	StateActionAVG qRewards = null;
	EGreedyOnPolicy policy = null;
	FirstVisitEpisode episode = new FirstVisitEpisode();
	
	double totalReward = 0.0;
	double bestReward = 0.0;
	
	
	public MCCart(StateTranslator aRange) {
		range = aRange;
		qRewards = new StateActionAVG( range.actionsSize );
		policy = new EGreedyOnPolicy(0.3, range.actionsSize);
	}
	
	public AgentAction start(Observation state) {
		totalReward = 0.0;
		
		DiscreteState discreteState = range.convert(state);
		
		int choice = policy.select(discreteState);
		episode.start(discreteState, choice);
		
		return new AgentAction( choice );
	}

	public AgentAction step(double stepReward, Observation state) {
		totalReward += stepReward;
		
		DiscreteState discreteState = range.convert(state);
		
		int choice = policy.select(discreteState);
		episode.step(totalReward, discreteState, choice);
		
		return new AgentAction( choice );
	}
	
	public void end(double endReward) {
		episode.end( 0.0 );
		episode.calculate( qRewards, policy );
		

		/**
		 * Statistics
		 */
		bestReward = Math.max(bestReward, totalReward);
		
		System.out.println("episode size: " + episode.size());
		//System.out.println("qRewards: \n" + qRewards.print() );		
		System.out.println("curr reward: " + Double.toString(totalReward));
		System.out.println("best reward: " + Double.toString(bestReward));
		
		{
			StringBuilder build = new StringBuilder();
			build.append( "known states: " );
			build.append( Integer.toString( qRewards.size() ) );
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
class FirstVisitEpisode {
	
	class StateAction {
		public DiscreteState state;
		public int action;
		
		public StateAction(DiscreteState aState, int anAction) {
			state = aState;
			action = anAction;
		}
		
		@Override
		public boolean equals(Object object) {
			if (object == null)
				return false;	
//			if (super.equals(object) == true) {
//				//the same reference
//				return true;
//			}
			
			/// different references - check content
			
			if (object instanceof StateAction == false) {
				return false;
			}
			StateAction typedObj = (StateAction) object;
			if (typedObj.state.equals( state ) == false)
				return false;
			if (typedObj.action != action)
				return false;
			
			return true;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(state, action);
		}
		
	}
	
	
	Map<StateAction, Double> firstVisits = new HashMap<StateAction, Double>();
	StateAction last = null;
	
	
	public FirstVisitEpisode() {
	}

	public int size() {
		return firstVisits.size();
	}
	
	public void calculate(StateActionAVG qFunction, EGreedyOnPolicy policy) {
		/// policy evaluation (update Q estimation)
		for( Entry<StateAction, Double> entry : firstVisits.entrySet() ) {
			StateAction sa = entry.getKey();
			DiscreteState state = sa.state;//.average( entry.getValue() );
			ActionAVGs aList = qFunction.getActions( state );
			aList.add( sa.action, entry.getValue() );
		}
		
		/// policy improvement
		Set<DiscreteState> states = stateSet();
		for( DiscreteState state: states) {
			policy.improve( qFunction, state );
		}
	}
	
	private Set<DiscreteState> stateSet() {
		Set<DiscreteState> states = new HashSet<DiscreteState>();
		for( StateAction key: firstVisits.keySet()) {
			states.add( key.state );
		}
		return states;
	}
	
	public void start(DiscreteState state, int choice) {
		firstVisits.clear();
		last = new StateAction(state, choice);
	}
	
	public void step(double reward, DiscreteState nextState, int nextChoice) {
		add(reward);
		last = new StateAction(nextState, nextChoice);
	}
	
	public void end(double reward) {
		add(reward);
		last = null;
	}
	
	private void add(double reward) {
		if (firstVisits.containsKey(last) == true)
			return ;
		firstVisits.put(last, reward);
	}
	
}
