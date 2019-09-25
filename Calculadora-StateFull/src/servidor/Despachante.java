package servidor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import util.Calculadora;

public class Despachante {	
	Calculadora calc = Calculadora.getInstance();
	Random gerador = new Random();
	public String invoke(String request) throws IOException {
		String[] dados=null;
		File file = null;
		FileWriter fw = null;
		String response=null, chave=null;
		int execucao = 0;
		if (request.split(":")[0].equals("chave")) {
			chave = String.valueOf(gerador.nextInt());
			response = chave + ":1";
			fw = new FileWriter("chave:"+chave+".tmp");
			fw.close();
		}else {
			chave = request.split(":")[0];
			if (request.split(":").length > 1) {
				dados = request.split(":")[1].split(";");
				execucao = dados.length;
			}
			if (!verificachave(chave)) {
				response = "Chave de acesso inválida, reinicie a conexão :0";
			}else {
				switch (execucao) {
				case 0:
					response = "Informe o operador (+,-,/,*) :1";
					break;
				case 1:
					response = "Informe o primeiro numero :1";
					break;
				case 2:
					response = "Informe o segundo numero :1";
					break;
				case 3:
					switch (dados[0]) {
					case "+":
						response = String.valueOf(calc.add(Double.parseDouble(dados[1]), Double.parseDouble(dados[2])));
						response = response + ":0";
						break;
					case "-":
						response = String.valueOf(calc.sub(Double.parseDouble(dados[1]), Double.parseDouble(dados[2])));
						response = response + ":0";
						break;
					case "/":
						response = String.valueOf(calc.div(Double.parseDouble(dados[1]), Double.parseDouble(dados[2])));
						response = response + ":0";
						break;
					case "*":
						response = String.valueOf(calc.mult(Double.parseDouble(dados[1]), Double.parseDouble(dados[2])));
						response = response + ":0";
						break;
					default:
						break;
					}
					file = new File("chave:"+chave+".tmp");
					file.delete();
					break;
				default:
					break;
				}
			}
		}
		return response;
	}
	public boolean verificachave(String chave) {
		File file = new File("chave:"+chave+".tmp");
		if (file.exists()) {
			return true;
		}
		return false;
	}
}