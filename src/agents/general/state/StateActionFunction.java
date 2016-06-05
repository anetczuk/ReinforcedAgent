/**
 * 
 */
package agents.general.state;

import agents.general.math.Value;
import agents.general.math.function.DiscreteFunction;
import agents.general.math.function.DiscreteList;

/**
 * @author bob
 * 
 * Action-value function.
 *
 */
public abstract class StateActionFunction<ValueType extends Value> extends DiscreteFunction<DiscreteState, DiscreteList<ValueType> > {

	int actionsNum;
	
	
	public StateActionFunction(int actionsSize) {
		actionsNum = actionsSize;
	}

	public StateActionFunction(StateTranslator range) {
		actionsNum = range.actionsSize;
	}
	
	public ValueType get(DiscreteState state, int action) {
		DiscreteList<ValueType> actions = getActions(state);
		return actions.get(action);
	}
	
	public void set(DiscreteState state, int action, double value) {
		DiscreteList<ValueType> actions = getActions(state);
		actions.set(action, value);
	}
	
	public void add(DiscreteState state, int action, double value) {
		DiscreteList<ValueType> actions = getActions(state);
		actions.add(action, value);
	}
	
	@Override
	protected DiscreteList<ValueType> createList() {
		DiscreteList<ValueType> list = new DiscreteList<ValueType>();
		for(int i=0; i<actionsNum; ++i) {
			list.push( createItem() );
		}
		return list;
	}
	
	protected abstract ValueType createItem();
	
};
