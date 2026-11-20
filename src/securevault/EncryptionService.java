package securevault;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class EncryptionService {
	
	private static final String TRANSFORMACAO = "AES/GCM/NoPadding";
	
	private static final int TAMANHO_NONCE = 12;
	
	private static final int TAMANHO_TAG = 128;
	
	private final SecureRandom secureRandom;
	
	public EncryptionService() {
		
		this.secureRandom = new SecureRandom();
	}
	
	public byte[] criptografar(byte[] dados, SecretKey chave) throws GeneralSecurityException{
		
		if(dados == null || chave == null) {
			
			throw new IllegalArgumentException("Os dados e a chave são obrigatórios.");
		}
	
		if(!"AES".equalsIgnoreCase(chave.getAlgorithm())) {
			
			throw new IllegalArgumentException("A chave informada precisa utilizar o algoritmo AES.");
		}
		
		byte[] nonce = new byte[TAMANHO_NONCE];
		
		secureRandom.nextBytes(nonce);
		
		Cipher cipher = Cipher.getInstance(TRANSFORMACAO);
		
		GCMParameterSpec parametros = new GCMParameterSpec(TAMANHO_TAG, nonce);
		
		cipher.init(Cipher.ENCRYPT_MODE, chave, parametros);
		
		byte[] dadosCriptografados = cipher.doFinal(dados);
		
		ByteBuffer resultado = ByteBuffer.allocate(nonce.length + dadosCriptografados.length);
		
		resultado.put(nonce);
		
		resultado.put(dadosCriptografados);
		
		return resultado.array();
		
	}
	
}
