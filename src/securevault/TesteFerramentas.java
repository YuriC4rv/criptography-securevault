package securevault;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class TesteFerramentas {

	public static void main(String[] args) throws Exception {
		
		String mensagem = "Conhecendo as ferramentas criptográficas do Java";
		
		byte[] dados = mensagem.getBytes(StandardCharsets.UTF_8);
		
		SecureRandom random = new SecureRandom();
		
		byte[] nonce = new byte[12];
		
		random.nextBytes(nonce);
		
		KeyGenerator gerador = KeyGenerator.getInstance("AES");
		
		gerador.init(256);
		
		SecretKey chave = gerador.generateKey();
		
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		
		GCMParameterSpec parametros = new GCMParameterSpec(128, nonce);
		
		cipher.init(Cipher.ENCRYPT_MODE, chave, parametros);
		
		byte[] dadosCriptografados = cipher.doFinal(dados);
		
		cipher.init(Cipher.DECRYPT_MODE, chave, parametros);
		
		byte[] dadosRecuperados = cipher.doFinal(dadosCriptografados);
		
		String mensagemRecuperada = new String(dadosRecuperados, StandardCharsets.UTF_8);
		
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		byte[] hash = digest.digest(dados);
		
		KeyPairGenerator geradorPar = KeyPairGenerator.getInstance("RSA");
		
		geradorPar.initialize(2048);
		
		KeyPair parChaves = geradorPar.generateKeyPair();
		
		Signature assinatura = Signature.getInstance("SHA256withRSA");
	
		assinatura.initSign(parChaves.getPrivate());
		
		assinatura.update(dados);
		
		byte[] assinaturaDigital = assinatura.sign();
		
		assinatura.initVerify(parChaves.getPublic());
		
		assinatura.update(dados);
		
		boolean assinaturaValida = assinatura.verify(assinaturaDigital);
		
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		
		keyStore.load(null, "senha123".toCharArray());
		
		System.out.println("MENSAGEM ORIGINAL:");
		System.out.println(mensagem);
		
		System.out.println("\nCHAVE AES GERADA:");
		System.out.println(HexFormat.of().formatHex(chave.getEncoded()));
	
		System.out.println("\nNONCE:");
		System.out.println(HexFormat.of().formatHex(nonce));
		
		System.out.println("\nDADOS CRIPTOGRAFADOS:");
		System.out.println(HexFormat.of().formatHex(dadosCriptografados));
		
		System.out.println("\nMENSAGEM RECUPERADA:");
		System.out.println(mensagemRecuperada);
		
		System.out.println("\nHASH SHA-256");
		System.out.println(HexFormat.of().formatHex(hash));
		
		System.out.println("\nASSINATURA DIGITAL VALIDA:");
		System.out.println(assinaturaValida);
		
		System.out.println("\nTIPO DO KEYSTORE:");
		System.out.println(keyStore.getType());
		
		System.out.println("\nQUANTIDADE DE ENTRADAS NO KEYSTORE:");
		System.out.println(keyStore.size());
		
	}

}
