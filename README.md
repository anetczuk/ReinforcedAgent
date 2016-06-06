# ReinforcedAgent
Java implementation of reinforcement learning algorithms based on book of 
R. S. Sutton and A. Barto. Online version of the book can be found here: 
	https://webdocs.cs.ualberta.ca/~sutton/book/the-book.html


Implementation includes following algorithms:
- Sarsa,
- Q-learning,
- Sarsa(lambda),
- Watkins's Q(lambda)
and e-greedy policy with some modifications.

Agent is prepared to work in RL-Library environment (http://library.rl-community.org), especially solving Mountain Car and CartPole problems.

User interface collaborates with rl-viz library: creates GUI interface on 
RLVizApp (main window - pre selection and configuration of agent) and on 
Agent Visualizer (control during experiment). 
It's achieved, among others, by:
- implementing AgentInterface, HasAVisualizerInterface, AbstractVisualizer, 
  DynamicControlTarget,
- integrating TaskSpec, ParameterHolder, Observation, DynamicControlTarget , 
  rlVizLib.messaging. 
