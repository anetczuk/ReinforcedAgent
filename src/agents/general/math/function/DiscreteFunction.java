/**
 * 
 */
package agents.general.math.function;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author bob
 *
 */
public abstract class DiscreteFunction<Key extends DiscreteKey, ListType extends DiscreteList<?> > {
	
	protected Map<Key, ListType> map = new HashMap<Key, ListType>();
	
	
	public int size() {
		return map.size();
	}
	
	public ListType getActions(Key state) {
		ListType list = map.get(state);
		if (list != null) {
			return list;
		}

		// element not exists - create new one
		list = createList();
		map.put(state, list);
		return list;
	}

//	public Value value(Key state, int action) {
//		ListType actions = getActions(state);
//		return actions.get(action);
//	}
	
	protected abstract ListType createList();
	
	public void clear() {
		map.clear();
	}
	
	public String print() {
		StringBuilder builder = new StringBuilder();

		for (Entry<Key, ListType> elem : map.entrySet()) {
			builder.append( elem.getKey().print() );
			builder.append( ": ");
			builder.append( elem.getValue().print() );
			builder.append( "\n" );
		}

		return builder.toString();
	}

	public void store(String filename) {
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			final File file = new File( filename );
			final File parent_directory = file.getParentFile();
			if (null != parent_directory) {
			    parent_directory.mkdirs();
			}
			fstream = new FileWriter(file);
			out = new BufferedWriter(fstream);

			store(out);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Close the output stream
			try {
				out.close();
				fstream.close();
			} catch (IOException e) {
			}
		}
	}

	private void store(BufferedWriter out) throws IOException {
		for (Entry<Key, ListType> elem : map.entrySet()) {
			Key key = elem.getKey();
			key.store(out);
			out.write("  ");
			DiscreteList<?> value = elem.getValue();
			value.store(out);
			out.write("\n");
		}
	}
	
}
