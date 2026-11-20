package securevault;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;

import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

public class KeyManager {
	
	private static final String TIPO_KEYSTORE = "PKCS12";
	
	private static final String ALIAS = "chave-aes";
	
	private static final String ALGORITMO = "AES";
	
	private static final int TAMANHO_CHAVE = 256;
	
	private final Path caminhoKeyStore;
	
	private final char[] senhaKeyStore;
	
	private final SecureRandom secureRandom;
	
	public KeyManager(Path caminhoKeyStore, char[] senhaKeyStore) {
		
		if(caminhoKeyStore == null || senhaKeyStore == null || senhaKeyStore.length == 0) {
			
			throw new IllegalArgumentException("O caminho e a senha do KeyStore são obrigatórios.");
		}
		
		this.caminhoKeyStore = caminhoKeyStore;
		
		this.senhaKeyStore = Arrays.copyOf(senhaKeyStore, senhaKeyStore.length);
		
		this.secureRandom = new SecureRandom();
	}
	
	public SecretKey obterOuCriarChave() throws GeneralSecurityException, IOException{
		
		if(Files.exists(caminhoKeyStore)) {
			
			return recuperarChave();
		}
		
		SecretKey chave = gerarChaveAES256();
		
		armazenarChave(chave);
		
		return chave;
	}
	
	private SecretKey gerarChaveAES256() throws GeneralSecurityException{
		
		KeyGenerator gerador = KeyGenerator.getInstance(ALGORITMO);
		
		gerador.init(TAMANHO_CHAVE, secureRandom);
		
		return gerador.generateKey();
		
	}
	
	private void armazenarChave(SecretKey chave) throws GeneralSecurityException, IOException{
		
		KeyStore keyStore = KeyStore.getInstance(TIPO_KEYSTORE);
		
		keyStore.load(null, senhaKeyStore);
		
		KeyStore.SecretKeyEntry entrada = new KeyStore.SecretKeyEntry(chave);
		
		KeyStore.PasswordProtection protecao = new KeyStore.PasswordProtection(senhaKeyStore);
		
		try {
			
			keyStore.setEntry(ALIAS, entrada, protecao);
			
			Path diretorio = caminhoKeyStore.getParent();
			
			if(diretorio != null) {
				
				Files.createDirectories(diretorio);
			}
		try(OutputStream saida = Files.newOutputStream(caminhoKeyStore)){
			
			keyStore.store(saida, senhaKeyStore);
		}
		
		}finally {
			
			try {
				
				protecao.destroy();
			
			} catch(DestroyFailedException e) {
				
				System.out.println("Aviso: não foi possível limpar a proteção da chave.");
			}
		}
	}
	
	private SecretKey recuperarChave() throws GeneralSecurityException, IOException{
		
		KeyStore keyStore = KeyStore.getInstance(TIPO_KEYSTORE);
		
		try(InputStream entrada = Files.newInputStream(caminhoKeyStore)){
			
			keyStore.load(entrada, senhaKeyStore);
		}
		
		Key chaveRecuperada = keyStore.getKey(ALIAS, senhaKeyStore);
		
		if(!(chaveRecuperada instanceof SecretKey)) {
			
			throw new GeneralSecurityException("Não foi possível recuperar a chave AES do KeyStore.");
		}
		
		return(SecretKey) chaveRecuperada;
	}
}
