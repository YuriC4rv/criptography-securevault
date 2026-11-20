package securevault;

import java.security.Provider;
import java.security.Security;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

public class TesteJCA {

	public static void main(String[] args) throws Exception {
		
		Provider[] providers = Security.getProviders();
		
		System.out.println("PROVIDERS DISPONIVEIS:");
		
		for(Provider provider : providers) {
			
			System.out.println(provider.getName());
		}
		
		String mensagem = "Conhecendo a JCA";
		
		byte[] dados = mensagem.getBytes(StandardCharsets.UTF_8);
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		byte[] resultado = digest.digest(dados);
		
		String hash = HexFormat.of().formatHex(resultado);
		
		System.out.println("\nMENSAGEM ORIGINAL:");
		System.out.println(mensagem);
		
		System.out.println("\nHASH SHA-256:");
		System.out.println(hash);
		
		System.out.println("\nQUANTIDADE DE BYTES: " + resultado.length);
	}
 
	
 /*
  *
  * public -> indica que o método possui acesso público
  * 
  * static -> significa que o método pertecen à própria classe, e não a uma instância específica dela. 
  * 
  * void -> indica que o método não retorna nenhum valor.
  * 
  * main -> é o nome convencional utilizado pela JVM
  * 
  * parâmetro (String[] args) -> representa um array de Strings. Permitindo receber argumentos informados durante a inicialização do programa pela linha de comando.
  * 
  * throws Exception -> Ela permite que exceções sejam propagadas para quem o invocou, em vez de obrigatoriamente tratá-las dentro dele
  * 
  * MessageDigest.getInstance()
  * 
  * 
  */
}
