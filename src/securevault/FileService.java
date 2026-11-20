package securevault;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

public class FileService {
	
	private final EncryptionService servicoCriptografia;
	private final DecryptionService servicoDescriptografia;
	
	public FileService(EncryptionService servicoCriptografia, DecryptionService servicoDescriptografia) {
		
		if(servicoCriptografia == null || servicoDescriptografia == null) {
			
			throw new IllegalArgumentException("Os serviços criptográficos são obrigatórios.");
		}
		
		this.servicoCriptografia = servicoCriptografia;
		this.servicoDescriptografia = servicoDescriptografia;
	}
	
	public void criptografarArquivo(Path arquivoOriginal, Path arquivoDestino, SecretKey chave) throws IOException, GeneralSecurityException{
		
		validarParametros(arquivoOriginal, arquivoDestino, chave);
		
		validarCaminhos(arquivoOriginal, arquivoDestino);
		
		if(!Files.isRegularFile(arquivoOriginal)) {
			throw new IOException("O arquivo original não existe ou não é um arquivo válido");
		}
		
		byte[] dadosOriginais = Files.readAllBytes(arquivoOriginal);
		
		byte[] dadosCriptografados = servicoCriptografia.criptografar(dadosOriginais, chave);
		
		prepararDiretorio(arquivoDestino);
		
		Files.write(arquivoDestino, dadosCriptografados, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	public void descriptografarArquivo(Path arquivoCriptografado, Path arquivoDestino, SecretKey chave) throws IOException, GeneralSecurityException{
		
		validarParametros(arquivoCriptografado, arquivoDestino, chave);
		
		validarCaminhos(arquivoCriptografado, arquivoDestino);
		
		if(!Files.isRegularFile(arquivoCriptografado)) {
			
			throw new IOException("O arquivo criptografado não existe ou não é um arquivo válido.");
		}
		byte[] dadosCriptografados = Files.readAllBytes(arquivoCriptografado);
		
		byte[] dadosRecuperados = servicoDescriptografia.descriptografar(dadosCriptografados, chave);
		
		prepararDiretorio(arquivoDestino);
		
		Files.write(arquivoDestino, dadosRecuperados, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
	}
	
	private void validarParametros(Path origem, Path destino, SecretKey chave) {
		
		if(origem == null || destino == null || chave == null) {
			
			throw new IllegalArgumentException("Os caminhos e a chave criptográfica são obrigatórios.");
		}
		
		if(!"AES".equalsIgnoreCase(chave.getAlgorithm())) {
			
			throw new IllegalArgumentException("A chave informada precisa utilizar o algoritmo AES.");
		}
	}
	
	private void validarCaminhos(Path origem, Path destino) {
		
		Path origemNormalizada = origem.toAbsolutePath().normalize();
		
		Path destinoNormalizado = destino.toAbsolutePath().normalize();
		
		if(origemNormalizada.equals(destinoNormalizado)) {
			
			throw new IllegalArgumentException("O arquivo de origem e o arquivo de destino devem ser diferentes.");
		}
	}
	
	private void prepararDiretorio(Path arquivoDestino) throws IOException{
		
		Path diretorio = arquivoDestino.getParent();
		
		if(diretorio != null) {
			
			Files.createDirectories(diretorio);
		}
	}
}
