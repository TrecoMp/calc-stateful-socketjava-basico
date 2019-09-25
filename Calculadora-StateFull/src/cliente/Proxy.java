package cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Proxy {
	public void invoke() {
		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		File file;
		ClienteTcp c = new ClienteTcp();
		String response=null, request=null, msg=null, chave=null, dados=null, estado=null, linha=null;
		FileWriter fw;
		FileReader fr;
		try {
			file = new File("chave.tmp");
			if (file.exists())
				limpalixo();
			while(true) {
				if (file.exists()) {
					fr = new FileReader("chave.tmp");
					BufferedReader lerArq = new BufferedReader(fr);
					linha = lerArq.readLine();
					c.sendRequest(linha);
					response = c.getResponse();
					estado = response.split(":")[1]; 
					response = response.split(":")[0];
					if (estado.equals("0")) {
						System.out.println("Resultado: " + response);
						c.sendRequest("exit()");
						c.close();
						file.delete();
						System.out.println("BYE");
						break;
					}					
					System.out.println(response);
					msg = teclado.readLine();
					chave = linha.split(":")[0];
					fw = new FileWriter("chave.tmp");
					if (linha.split(":").length == 1 ) {
						fw.write(chave+":"+msg);
					}else {
						dados = linha.split(":")[1];
						fw.write(chave+":"+dados+";"+msg);
					}		
					fw.close();
					lerArq.close();
					fr.close();
				}else{
					fw = new FileWriter("chave.tmp");
					c.sendRequest("chave:");
					response = c.getResponse();
					response = response.split(":")[0];
					fw.write(response+":");
					fw.close();	
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void limpalixo() throws IOException {
		FileReader fr = new FileReader("chave.tmp");
		String linha, chave, dadosfinais;
		String[] dados;
		List<String> dadoslimpos = new ArrayList<String>();
		BufferedReader lerArq = new BufferedReader(fr);
		linha = lerArq.readLine();
		chave = linha.split(":")[0];
		if(linha.split(":").length > 1) {
			dados = linha.split(":")[1].split(";");
			for (String string : dados) {
				if (!string.equals("null")) {
					dadoslimpos.add(string);
				}
			}
			dadosfinais = dadoslimpos.get(0);
			System.out.println(dadosfinais);
			for (int i = 1; i < dadoslimpos.size(); i++) {
				dadosfinais = dadosfinais +";" + dadoslimpos.get(i);
				System.out.println(dadosfinais);
			}
			fr.close();
			File f = new File("chave.tmp");
			f.delete();
			FileWriter fw = new FileWriter("chave.tmp");
			fw.write(chave + ":" + dadosfinais);
			fw.close();
			
		}
		
		
	}
}
