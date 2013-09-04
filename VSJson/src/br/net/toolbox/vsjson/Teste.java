package br.net.toolbox.vsjson;

public class Teste {

	public static void main(String[] args) {
		String param = "[{\"lat\": -22.9060416, \"lng\": -43.1764733, \"nome\": \"Bar da esquina\"}]";
		
		Parser parser = new Parser();
		parser.fromJSON(param);
		

	}

}
