package br.net.toolbox.vsjson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.net.toolbox.vsjson.helper.Helper;
import br.net.toolbox.vsjson.helper.State;

public class Parser {

	private Map<String, Class> binds = new HashMap<String, Class>();
	private ArrayList<State> states = new ArrayList<State>();
	private int i = 0;
	private char [] data;
	private Class rootClass = null;
	private Object rootObject;
	
	public Object fromJSON(String jsonData) throws InstantiationException, IllegalAccessException {

		Object retorno = null;

		if (jsonData != null) {

			data = jsonData.toCharArray();
			
			for (;i < data.length; i++) {
				
				char c = data[i];
				
				switch (c) {
					case Helper.TOKEN_END_LIST:
						retorno = pop().object;
						break;
					case Helper.TOKEN_END_OBJECT:
						retorno = pop().object;
						break;
					case Helper.TOKEN_KEY_VALUE_SEPARATOR:
						handleValue();
						break;
					case Helper.TOKEN_START_LIST:
						startList();
						break;
					case Helper.TOKEN_START_OBJECT:
						if (this.rootClass == null){
							startObject();
						}else{
							if(getLastState().type == Helper.STATE_IN_LIST){
								startRootObject();
							}else if(getLastState().type == Helper.STATE_IN_ROOT_OBJECT){
								startInnerObject();
							}
						}
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

	private void startInnerObject(){
		
		//State state 
		//Class cls = findAttributeClass(name)
		
	}
	
	private void startRootObject() throws InstantiationException, IllegalAccessException{
		if (rootClass != null){
			rootObject = rootClass.newInstance();
			State state = State.createState(Helper.STATE_IN_ROOT_OBJECT, rootObject);
			((List)(getLastState().object)).add(rootObject);
			this.states.add(state);
		}
	}
	private void handleValue(){
		i++;

		char c = Character.toUpperCase(data[i]);
		
		while (c == 32){
			i++;
			c = Character.toUpperCase(data[i]);
		}
			
		
		State state = getLastState();
		
		Object value = null;
		
		switch(c){
		case '-': case '0':case '1':case '2':case '3': case '4':case '5':case '6': case '7':case '8':case '9':
			 value = consumeNumber();
			break;
		case Helper.TOKEN_STRING_CONTAINER:
			 value = cosumeString();
			break;
		case 'T': case 'F': case 'N':
			value = handleTrueFalseNull();
		}
		
		if(state.type == Helper.STATE_IN_KEY){
			pop();
			((Map)getLastState().object).put(state.key, value);
		}else if(state.type == Helper.STATE_IN_LIST){
			((List)getLastState().object).add(value);
		}
		
		
	}
	
	private void startList(){
		List list = new ArrayList();
		State state = State.createState(Helper.STATE_IN_LIST, list);
		this.states.add(state);
	}
	private void startObject(){
		Map map = new HashMap();
		State state = State.createState(Helper.STATE_IN_DICT, map);
		State actualState = this.states.get(this.states.size()-1);
		switch(actualState.type){
		case Helper.STATE_IN_LIST:
			((List)actualState.object).add(map);
			break;
		default:
			break;
		}
		this.states.add(state);
		
	}
	
	private Object handleTrueFalseNull(){

		char c = Character.toUpperCase(data[i]);
		Object value = null;
		switch(c){
		case 'T':
			if(String.valueOf(Arrays.copyOfRange(data, i, i+3)).toUpperCase().equals(Helper.VALUE_TRUE))
				value = Boolean.TRUE;
		case 'F':
			if(String.valueOf(Arrays.copyOfRange(data, i, i+3)).toUpperCase().equals(Helper.VALUE_FALSE))
				value = Boolean.FALSE;
		}
		
		return value;
		
	}
	
	private void createKey(String string){
		State state = new State();
		state.key = string;
		state.type = Helper.STATE_IN_KEY;
		this.states.add(state);
	}
	
	private Double consumeNumber(){
		int f = i;

		while (data[i] != Helper.TOKEN_END_LIST && data[i] !=Helper.TOKEN_END_OBJECT && data[i] != Helper.TOKEN_VALUE_SEPARATOR)
			i++;
		return Double.parseDouble(String.valueOf(Arrays.copyOfRange(data, f, i)));
		
	}
	private void handleString(){
		String string = cosumeString();
		State state = getLastState();
		
		switch (state.type) {
		case Helper.STATE_IN_LIST:
			((List)state.object).add(string);
			break;
		case Helper.STATE_IN_DICT:
			createKey(string);
		default:
			break;
		}
		
		
	}
	private String cosumeString(){
		
		i++;
		
		int f = i;
		
		while(data[i] != Helper.TOKEN_STRING_CONTAINER )
			i++;
		return String.valueOf(Arrays.copyOfRange(data, f, i));
	}
	
	public void bind(String key, Class value) {
		binds.put(key, value);
	}

	public void listOf(Class cls){
		this.rootClass = cls;
	}
	
	
	private State getLastState(){
		return this.states.get(this.states.size()-1);
	}
	
	private State pop(){
		State state = getLastState();
		this.states.remove(this.states.size() -1);
		return state;
	}
	
	private Class findAttributeClass(String name) throws SecurityException, NoSuchFieldException{
		State state = getLastState();
		
		Field field = rootClass.getDeclaredField(name);
		
		return field.getDeclaringClass();
		
	}
}
