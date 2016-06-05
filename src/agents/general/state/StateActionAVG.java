/**
 * 
 */
package agents.general.state;

import agents.general.math.StdAVG;
import agents.general.math.function.DiscreteFunction;
import agents.general.math.function.DiscreteList;
import agents.general.state.StateActionAVG.ActionAVGs;

/**
 * @author bob
 * 
 * Action-value function.
 *
 */
//TODO: refactor inheritance
public class StateActionAVG extends DiscreteFunction<DiscreteState, ActionAVGs> {

	int actionsNum;
	
	
	public StateActionAVG(int actionsSize) {
		actionsNum = actionsSize;
	}

	public StateActionAVG(StateTranslator range) {
		actionsNum = range.actionsSize;
	}
	
	@Override
	protected ActionAVGs createList() {
		return new ActionAVGs(actionsNum);
	}
	
	
	
	///=========================================================
	
	
	
	/**
	 * 
	 * @author bob
	 *
	 */
	public static class ActionAVGs extends DiscreteList<StdAVG> {

		public ActionAVGs(int actionsNumber) {
			for(int i=0; i<actionsNumber; ++i) {
				push( new StdAVG(1.0) );
			}
		}

//		public void average(int action, double value) {
//			StdAVG val = get(action);
//			val.average(value);
//		}
		
//		public int leastVisited() {
//			int bestIndex = 0;
//			int bestFactor = values.get(bestIndex).count;
//
//			for (int i = 1; i < values.size(); ++i) {
//				int val = values.get(i).count;
//				if (val > bestFactor) {
//					bestFactor = val;
//					bestIndex = i;
//				}
//			}
//
//			return bestIndex;
//		}

//		public int minCount() {
//			int min = values.get(0).count;
//			for (int i = 1; i < values.size(); ++i) {
//				min = Math.min(min, values.get(i).count);
//			}
//			return min;
//		}

//		public int count() {
//			int sum = 0;
//			for (int i = 0; i < values.size(); ++i) {
//				sum += values.get(i).count;
//			}
//			return sum;
//		}
		
	}
	
};
