package br.net.toolbox.vsjson;

import java.util.List;
import java.util.Map;

import com.github.kevinsawicki.http.HttpRequest;

public class Teste {

	public static void main(String[] args) {
		String retorno = "[{\"lat\": -22.9060416, \"lng\": -43.1764733, \"nome\": \"Bar da esquina\"}, {\"lat\": -22.907692, \"lng\": -43.1829643, \"nome\": \"Bar da Tiradente\"}]";
		
		//String url = "http://conca.toolbox.net.br:5000/listar/-22.9063961/-43.1792279";
		
		//String retorno = HttpRequest.get(url).body();
		
		Parser parser = new Parser();
		List lista = (List)parser.fromJSON(retorno);
		for(Object mapObject : lista){
			Map map = (Map)mapObject;
			System.out.println(map.get("nome"));
			System.out.println(map.get("lat"));
			System.out.println(map.get("lng"));
		}
		

	}

}
