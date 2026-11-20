package securevault;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.AEADBadTagException;
import javax.crypto.SecretKey;

public class TesteDecryptionService {

	public static void main(String[] args) throws Exception {
		
		Path caminho = Path.of("dados", "securevault.p12");
		
		char[] senha = "SenhaSegura123!".toCharArray();
		
		try {
			KeyManager gerenciador = new KeyManager(caminho, senha);
			
			SecretKey chave = gerenciador.obterOuCriarChave();
			
			EncryptionService servicoCriptografia = new EncryptionService();
			
			DecryptionService servicoDescriptografia = new DecryptionService();
			
			String mensagem = "Testando o AES-256-GCM com Java!";
			
			byte[] dadosOriginais = mensagem.getBytes(StandardCharsets.UTF_8);
			
			System.out.println("INICIANDO TESTES DE PROTEÇÃO DE MENSAGEM");
			
			System.out.println("\nMENSAGEM ORIGINAL:");
			
			System.out.println(mensagem);
			
			byte[] dadosCriptografados = servicoCriptografia.criptografar(dadosOriginais, chave);
			
			System.out.println("\nCRIPTOGRAFIA REALIZADA!");
			
			System.out.println("\nTAMANHO DO PACOTE CRIPTOGRAFADO:");
			
			System.out.println(dadosCriptografados.length + " bytes");
			
			byte[] dadosRecuperados = servicoDescriptografia.descriptografar(dadosCriptografados, chave);
			
			String mensagemRecuperada = new String(dadosRecuperados, StandardCharsets.UTF_8);
			
			System.out.println("\nMENSAGEM DESCRIPTOGRAFADA:");
			
			System.out.println(mensagemRecuperada);
			
			boolean mensagensIguais = MessageDigest.isEqual(dadosOriginais, dadosRecuperados);
			
			System.out.println("\nAS MENSAGENS SÃO IGUAIS?");
			
			System.out.println(mensagensIguais);
			
			byte[] pacoteAlterado = Arrays.copyOf(dadosCriptografados, dadosCriptografados.length);
			
			pacoteAlterado[pacoteAlterado.length - 1] ^= 1;
			
			System.out.println("\nTESTANDO ALTERAÇÃO DOS DADOS:");
			
			try {
				
				servicoDescriptografia.descriptografar(pacoteAlterado, chave);
				
				System.out.println("FALHA: alteração não detectada!");
			}catch(AEADBadTagException e) {
				
				System.out.println("ALTERAÇÃO DETECTADA: autenticação rejeitada!");
			}
		
			System.out.println("\nTESTE FINALIZADO!");
		}finally {
			
			Arrays.fill(senha, '\0');
		}

	}

}
