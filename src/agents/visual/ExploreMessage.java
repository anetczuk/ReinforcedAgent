/**
 * 
 */
package agents.visual;

import java.util.StringTokenizer;

import org.rlcommunity.rlglue.codec.RLGlue;

import rlVizLib.messaging.AbstractMessage;
import rlVizLib.messaging.MessageUser;
import rlVizLib.messaging.MessageValueType;
import rlVizLib.messaging.agent.AgentMessageType;
import rlVizLib.messaging.agent.AgentMessages;

/**
 * @author bob
 *
 */
public class ExploreMessage {

	public static class Response {
		public boolean state;
	}
	
	
	//=====================================================
	
	
	private static String token = "EXPLORE_STATE";
	
	
	public static void send(boolean selected) {
		StringBuffer stateBuffer = new StringBuffer();

		stateBuffer.append( token );
		stateBuffer.append( ":" );
		stateBuffer.append( selected );
		
		String theRequest = AbstractMessage.makeMessage(
								MessageUser.kEnv.id(),
								MessageUser.kBenchmark.id(), 
								AgentMessageType.kAgentCustom.id(),
								MessageValueType.kString.id(), 
								stateBuffer.toString() );

		RLGlue.RL_agent_message(theRequest);
	}

	public static Response parse(AgentMessages theMessageObject) {
		String thePayLoadString = theMessageObject.getPayLoad();
		
        if (thePayLoadString.startsWith( token ) == false) {
            return null;
        }
		
		StringTokenizer stateTokenizer = new StringTokenizer(thePayLoadString, ":");
		
		Response resp = new Response();
		stateTokenizer.nextToken();		// token
		resp.state = Boolean.parseBoolean(stateTokenizer.nextToken());
		System.out.println("parsing message: " + thePayLoadString );
		System.out.println("read value: " + Boolean.toString(resp.state) );
		return resp;
	}
	
}
