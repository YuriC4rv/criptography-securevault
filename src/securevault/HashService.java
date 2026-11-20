package securevault;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashService {

	private static final String ALGORITMO = "SHA-256";
	
	public byte[] gerarHash(byte[] dados) throws NoSuchAlgorithmException{
		
		if(dados == null) {
			
			throw new IllegalArgumentException("Os dados para geração do hash são obrigatórios.");
		}
		
		MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
		
		return digest.digest(dados);
	}
	
	public boolean compararHashes(byte[] hash1, byte[] hash2) {
		
		if(hash1 == null || hash2 == null) {
			
			throw new IllegalArgumentException("Os hashes para comparação são obrigatórios.");
		}
		
		return MessageDigest.isEqual(hash1, hash2);
	}
}
