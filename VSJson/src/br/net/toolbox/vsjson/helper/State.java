package br.net.toolbox.vsjson.helper;

public class State {


	
	public static State createState(int type, Object object){
		
		State state = null;
		
		switch(type){
		case Helper.STATE_IN_DICT:
			state = new State();
			state.object = object;
			state.type = Helper.STATE_IN_DICT;
			break;
		case Helper.STATE_IN_LIST:
			state = new State();
			state.object = object;
			state.type = Helper.STATE_IN_LIST;
		break;
		}
		
		return state;
	}
	
	
	public int state;
	public Object object;
	public int type;
	public String key;
	
}
