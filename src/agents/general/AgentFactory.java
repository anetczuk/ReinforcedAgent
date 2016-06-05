/**
 * 
 */
package agents.general;

import java.util.ArrayList;
import java.util.List;

import agents.general.policy.PolicyControl;
import agents.general.state.StateTranslator;
import rlVizLib.general.ParameterHolder;


/**
 * @author bob
 *
 */
public abstract class AgentFactory {

	public abstract void initParams(ParameterHolder params);
	
	public abstract PolicyControl create(StateTranslator range, ParameterHolder params);
	
	
	public static void addDoubleParam(ParameterHolder params, String field, double defaultVal) {
		if (params.isParamSet(field))
			return ;
		params.addDoubleParam(field, defaultVal);
	}
	
	
	
	//=================================================================

	
	
	static class NullFactory extends AgentFactory {
		
		@Override
		public void initParams(ParameterHolder params) {
			/// no params to register
		}

		@Override
		public PolicyControl create(StateTranslator range, ParameterHolder params) {
			return null;
		}
		
	}
	
	public static class Registry {
	
		static List<AgentFactory> factories = new ArrayList<AgentFactory>();
	
		
		private Registry() {
		}
		
		
		public static int size() {
			return factories.size();
		}
		
		public static void add(AgentFactory factory) {
			factories.add( factory );
		}
		
		public static AgentFactory get(int index) {
			if (index < 0) {
				return null;
			}
			if (index >= factories.size()) {
				return null;
			}
			return factories.get(index);
		}
		
		public static AgentFactory defaultFactory() {
			if (factories.size() > 0) {
				return factories.get(0);
			}
			return new NullFactory();
		}

		public static void initParams(ParameterHolder params) {
			for (AgentFactory f : factories) {
				f.initParams( params );
			}
		}
		
	}
	
}
