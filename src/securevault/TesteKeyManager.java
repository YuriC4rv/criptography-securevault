package securevault;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.SecretKey;

public class TesteKeyManager {

	public static void main(String[] args) throws Exception{
		
		Path caminho = Path.of("dados", "securevault.p12");
		
		char[] senha = "SenhaSegura123!".toCharArray();
		
		KeyManager gerenciador = new KeyManager(caminho, senha);
		
		System.out.println("INICIANDO TESTES DO KEY MANAGER");
		
		
		// 1 -> Gerar ou recuperar a chave
		SecretKey chave = gerenciador.obterOuCriarChave();
		System.out.println("\nCHAVE OBTIDA COM SUCESSO!");
		
		// 2 -> Verifica o algoritmo
		System.out.println("\nALGORITMO:");
		System.out.println(chave.getAlgorithm());
		
		// 3 -> Verifica o tamanho da chave
		System.out.println("\nTAMANHO DA CHAVE:");
		int tamanho = chave.getEncoded().length * 8;
		System.out.println(tamanho + " bits");
		
		// 4 ->  Verifica a existência do KeyStore
		System.out.println("\nARQUIVO KEYSTORE EXISTE:");
		System.out.println(Files.exists(caminho));
		
		// 5 -> Recupera novamente a chave
		SecretKey chaveRecuperada = gerenciador.obterOuCriarChave();
		
		// 6 -> Compara as chaves
		boolean iguais = MessageDigest.isEqual(chave.getEncoded(), chaveRecuperada.getEncoded());
		System.out.println("\nAS CHAVES SÃO IGUAIS?");
		System.out.println(iguais);
		
		// 7 -> Limpa o array de senha utilizado no teste
		Arrays.fill(senha, '\0');
		
		System.out.println("\nTESTE FINALIZADO!");
		

	}

}
