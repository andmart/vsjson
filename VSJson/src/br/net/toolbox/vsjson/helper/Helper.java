package br.net.toolbox.vsjson.helper;

public class Helper {

	public final static char TOKEN_START_LIST = '[';
	public final static char TOKEN_END_LIST = ']';
	public final static char TOKEN_START_OBJECT = '{';
	public final static char TOKEN_END_OBJECT = '}';
	public final static char TOKEN_KEY_VALUE_SEPARATOR = ':';
	public final static char TOKEN_VALUE_SEPARATOR = ',';
	public final static char TOKEN_STRING_CONTAINER = '"';
	
	public final static int STATE_IN_LIST = 1;
	public final static int STATE_IN_DICT = 2;
	public final static int STATE_IN_KEY = 3;
	public final static int STATE_IN_ROOT_OBJECT = 4;
	
	public final static String VALUE_TRUE = "TRUE";
	public final static String VALUE_FALSE = "FALSE";
	public final static String VALUE_NULL = "NULL";
	
	
}
