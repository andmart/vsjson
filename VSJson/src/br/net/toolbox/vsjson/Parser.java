package br.net.toolbox.vsjson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.net.toolbox.vsjson.helper.State;

public class Parser {

	private Map<String, Class> binds = new HashMap<String, Class>();
	private ArrayList<State> states = new ArrayList<State>();
	private int i = 0;
	private char [] data;

	public Object fromJSON(String jsonData) {

		Object retorno = null;

		if (jsonData != null) {

			data = jsonData.toCharArray();
			
			for (;i < data.length; i++) {
				
				char c = data[i];
				
				switch (c) {
					case Helper.TOKEN_END_LIST:
						break;
					case Helper.TOKEN_END_OBJECT:
						break;
					case Helper.TOKEN_KEY_VALUE_SEPARATOR:
						break;
					case Helper.TOKEN_START_LIST:
						startList();
						break;
					case Helper.TOKEN_START_OBJECT:
						break;
					case Helper.TOKEN_STRING_CONTAINER:
						handleString();
						break;
					case Helper.TOKEN_VALUE_SEPARATOR:
						break;	
					default:
						break;
				}

			}
		}

		return retorno;
	}

	private void startList(){
		List list = new ArrayList();
		State state = new State();
		state.parent = list;
		state.type = Helper.STATE_IN_LIST;
		this.states.add(state);
	}
	
	private void createKey(String string){
		State state = new State();
		state.key = string;
		state.type = Helper.STATE_IN_KEY;
		this.states.add(state);
	}
	
	
	private void handleString(){
		String string = cosumeString();
		State state = this.states.get(this.states.size()-1);
		
		switch (state.type) {
		case Helper.STATE_IN_LIST:
			((List)state.parent).add(string);
			break;
		case Helper.STATE_IN_DICT:
			createKey(string);
		default:
			break;
		}
		
		
	}
	private String cosumeString(){
		int f = i;
		
		while(data[i] != Helper.TOKEN_STRING_CONTAINER )
			i++;
		
		return String.valueOf(Arrays.copyOfRange(data, f, i));
	}
	
	public void bind(String key, Class value) {
		binds.put(key, value);
	}

}
