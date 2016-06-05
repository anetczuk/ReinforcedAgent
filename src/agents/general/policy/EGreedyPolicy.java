/**
 * 
 */
package agents.general.policy;

import java.util.Random;

import agents.general.math.Value;
import agents.general.math.ValueCount;
import agents.general.math.function.DiscreteList;

/**
 * @author bob
 *
 */
public class EGreedyPolicy {

	double epsilon;
	public int step = 0;
	int episode = 0;
	Random rand = new Random();
	boolean explore = true;
	public boolean takenExplore = false;
	public double threshold = 1.0;
	
	
	public EGreedyPolicy(double epsilon) {
		this.epsilon = epsilon;
	}
	
	public void setExplore(boolean state) {
		explore = state;
	}
	
	public int actionExp(DiscreteList<Value> stateActions) {
		++step;
		takenExplore = false;
		
		if (explore == false) {
			return stateActions.bestByMaxValue();	
		}
		
		//threshold = 0.1;
		threshold = 1.0 / episode;
		//threshold = 0.95 * step / (step + 200) + 0.05;
		
		if (rand.nextDouble() < threshold) {
			//random
			int act = rand.nextInt( stateActions.size() );
			takenExplore = (act != stateActions.bestByMaxValue());
			return act;
		}
		return stateActions.bestByMaxValue();
	}
	
	public int actionExponential(DiscreteList<ValueCount> stateActions) {
		++step;
		takenExplore = false;
		
		if (explore == false) {
			return stateActions.bestByMaxValue();	
		}
		
		final int total = count(stateActions) + 1;
		//threshold = 0.1;
		//threshold = 10.0 / episode;
		//threshold = 10.0 / total + (3.0 + 5.0 * epsilon) / (10.0 + episode);
		threshold = 10.0 / total + epsilon / episode;
		//threshold = 0.95 * step / (step + 200) + 0.05;
		
		if (rand.nextDouble() < threshold) {
			//random
			int act = randomCount(stateActions, total);
			takenExplore = (act != stateActions.bestByMaxValue());
			return act;
		}
		return stateActions.bestByMaxValue();
	}
	
	int count(DiscreteList<ValueCount> stateActions) {
		final int lSize = stateActions.size();
		int total = 0;
		for(int i=0; i<lSize; ++i) {
			total += stateActions.get(i).count();
		}
		return total;
	}
	
	int randomCount(DiscreteList<ValueCount> stateActions, final int total) {
		final int lSize = stateActions.size();
		int r = rand.nextInt( total );
		for(int i=0; i<lSize; ++i) {
			r -= (total - stateActions.get(i).count());
			if (r < 0)
				return i;
		}
		return lSize-1;		
	}
	
	public void reset() {
		step = 0;
		++episode;
	}

	public int action(DiscreteList<Value> stateActions) {
		takenExplore = false;
		
		if (explore == false) {
			return stateActions.bestByMaxValue();	
		}
		
//		if (stateActions.minCount() > 300) {
//			return stateActions.bestByMaxValue();
//		}
		
		//threshold = 0.1;
		//threshold = factor(episode);
		threshold = epsilon;
		
		if (rand.nextDouble() < threshold) {
			//random
			int act = rand.nextInt( stateActions.size() );
			takenExplore = (act != stateActions.bestByMaxValue());
			return act;
		}
		return stateActions.bestByMaxValue();
	}

	double factor(int num) {
		//double factor = 1.0 / (step / 10 + 1.0);
		double factor = 1.0 / (num/1000.0 + 1.0);
		//double factor = 0.95 * num / (num + 200) + 0.05;
		return factor;
	}
	
}
