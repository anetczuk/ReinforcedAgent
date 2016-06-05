/**
 * 
 */
package agents.general.state;

import java.util.Iterator;
import java.util.Map.Entry;

import agents.general.math.Value;
import agents.general.math.function.DiscreteList;

/**
 * @author bob
 * 
 * Action-value function.
 *
 */	
public abstract class DiscreteFunction2D<ValueType extends Value> extends StateActionFunction<ValueType> {	

	public DiscreteFunction2D(int actionsSize) {
		super(actionsSize);
	}
	
	public DiscreteFunction2D(StateTranslator range) {
		super(range);
	}

//	private void initStates(StateTranslator range) {
//		final int tSize = range.observation.length;
//		DiscreteState state = new DiscreteState(tSize);
//		state.reset();
//		
//		do {
//			DiscreteState curr = new DiscreteState( state );
//			map.put(curr, new ActionValuesList());
//		} while( range.increase( state ) );
//	}
	
	public ValueType value(DiscreteState state, int action) {
		DiscreteList<ValueType> actions = getActions(state);
		return actions.get( action );
	}
	
	public Iterator<Item> iterator() {
		return new ItemIterator(); 
	}
	

	//=====================================================
	
	
	public class Item {
		public DiscreteState state;
		public int action;
		public ValueType value;
		
		public Item(DiscreteState state, int action, ValueType val) {
			this.state = state;
			this.action = action;
			value = val;
		}
	}

	class ItemIterator implements Iterator<Item> {
		
		private Iterator<Entry< DiscreteState, DiscreteList<ValueType> >> mapPos = null;
		private DiscreteState state = null;
		private DiscreteList<ValueType> list = null;
		private int action = 0;

		
		public ItemIterator() {
			mapPos = map.entrySet().iterator();
		}
		
		@Override
		public boolean hasNext() {
			if (mapPos.hasNext()==true) {
				return true;
			}
			if ( (action+1) < list.size()) {
				return true;
			}
			return false;
		}

		@Override
		public Item next() {
			++action;
			if (list != null) {
				if ( action < list.size() ) {
					/// valid
					return nextItem();
				}
			}
			
			Entry<DiscreteState, DiscreteList<ValueType>> nextElem = mapPos.next();
			setPosition( nextElem );
			return nextItem();
		}

		private void setPosition(Entry<DiscreteState, DiscreteList<ValueType>> nextElem) {
			state = nextElem.getKey();
			list = nextElem.getValue();
			action = 0;
		}

		private Item nextItem() {
			ValueType val = list.get(action);
			return new Item(state, action, val);
		}

		@Override
		public void remove() {
			/// not supported
		}
		
	}
	
}
