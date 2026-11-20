package securevault;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class DecryptionService {

	private static final String TRANSFORMACAO = "AES/GCM/NoPadding";

	private static final int TAMANHO_NONCE = 12;

	private static final int TAMANHO_TAG = 128;

	public byte[] descriptografar(byte[] dadosCriptografados, SecretKey chave) throws GeneralSecurityException {
		
		if(dadosCriptografados == null || chave == null) {
			
			throw new IllegalArgumentException("Os dados criptografados e a chave são obrigatórios.");
		}
		
		if(!"AES".equalsIgnoreCase(chave.getAlgorithm())) {
			
			throw new IllegalArgumentException("A chave informada precisa utilizar o algoritmo AES.");
		}
		
		int tamanhoMinimo = TAMANHO_NONCE + (TAMANHO_TAG / 8);
		
		if(dadosCriptografados.length < tamanhoMinimo) {
			
			throw new IllegalArgumentException("Os dados criptografados estão incompletos ou inválidos.");
		}
		
		ByteBuffer buffer = ByteBuffer.wrap(dadosCriptografados);
		
		byte[] nonce = new byte[TAMANHO_NONCE];
		
		buffer.get(nonce);
		
		byte[] conteudoCriptografado = new byte[buffer.remaining()];
		
		buffer.get(conteudoCriptografado);
		
		Cipher cipher = Cipher.getInstance(TRANSFORMACAO);
		
		GCMParameterSpec parametros = new GCMParameterSpec(TAMANHO_TAG, nonce);
		
		cipher.init(Cipher.DECRYPT_MODE, chave, parametros);
		
		return cipher.doFinal(conteudoCriptografado);
		
	}
}
