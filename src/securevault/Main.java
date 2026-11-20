package securevault;

import java.nio.file.Path;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		
		Path caminhoKeyStore = Path.of("dados", "securevault.p12");
		
		char[] senhaKeyStore = "SenhaSegura123!".toCharArray();
		
		try {
			
			KeyManager keyManager = new KeyManager(caminhoKeyStore, senhaKeyStore);
			
			EncryptionService encryptionService = new EncryptionService();
			
			DecryptionService decryptionService = new DecryptionService();
			
			FileService fileService = new FileService(encryptionService, decryptionService);
			
			HashService hashService = new HashService();
			
			System.out.println("SERVIÇOS DO SECUREVAULT INICIALIZADOS!");
			
		}finally {
			
			Arrays.fill(senhaKeyStore, '\0');
		}

	}

}
