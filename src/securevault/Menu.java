package securevault;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.crypto.AEADBadTagException;
import javax.crypto.SecretKey;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.security.MessageDigest;
import java.util.HexFormat;

public class Menu extends JFrame {
	private final KeyManager keyManager;
	private final EncryptionService encryptionService;
	private final DecryptionService decryptionService;
	private final FileService fileService;
	private final HashService hashService;
	private final JTextArea mensagemOriginal = new JTextArea(6, 40);
	private final JTextArea mensagemCriptografada = new JTextArea(6, 40);
	private final JTextArea mensagemDescriptografada = new JTextArea(6, 40);
	private final JTextField caminhoOriginal = new JTextField();
	private final JTextField caminhoDestino = new JTextField();
	private final JTextArea textoHash = new JTextArea(5, 40);
	private final JTextField hashGerado = new JTextField();
	private final JTextField hashComparacao = new JTextField();

	public Menu(KeyManager keyManager, EncryptionService encryptionService, DecryptionService decryptionService,
			FileService fileService, HashService hashService) {
		this.keyManager = keyManager;
		this.encryptionService = encryptionService;
		this.decryptionService = decryptionService;
		this.fileService = fileService;
		this.hashService = hashService;
		configurarJanela();
		criarInterface();
	}

	private void configurarJanela() {
		setTitle("SecureVault | Proteção de Dados");
		setSize(850, 700);
		setMinimumSize(new Dimension(700, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void criarInterface() {
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
		JLabel titulo = new JLabel("SECUREVAULT", SwingConstants.CENTER);
		titulo.setFont(new Font("SansSerif", Font.BOLD, 26));
		JLabel subtitulo = new JLabel("Sistema de criptografia e proteção de dados", SwingConstants.CENTER);
		subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		JPanel painelCabecalho = new JPanel(new GridLayout(2, 1, 5, 5));
		painelCabecalho.add(titulo);
		painelCabecalho.add(subtitulo);
		JTabbedPane abas = new JTabbedPane();
		abas.addTab("Mensagens", criarAbaMensagens());
		abas.addTab("Arquivos", criarAbaArquivos());
		abas.addTab("SHA-256", criarAbaHash());
		painelPrincipal.add(painelCabecalho, BorderLayout.NORTH);
		painelPrincipal.add(abas, BorderLayout.CENTER);
		setContentPane(painelPrincipal);
	}

	private JPanel criarAbaMensagens() {
		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(new EmptyBorder(15, 10, 10, 10));
		JPanel conteudo = new JPanel(new GridLayout(3, 1, 10, 10));
		mensagemOriginal.setLineWrap(true);
		mensagemOriginal.setWrapStyleWord(true);
		mensagemCriptografada.setLineWrap(true);
		mensagemCriptografada.setWrapStyleWord(true);
		mensagemCriptografada.setEditable(false);
		mensagemDescriptografada.setLineWrap(true);
		mensagemDescriptografada.setWrapStyleWord(true);
		mensagemDescriptografada.setEditable(false);
		conteudo.add(criarCampoTexto("Mensagem original", mensagemOriginal));
		conteudo.add(criarCampoTexto("Mensagem criptografada (Base64)", mensagemCriptografada));
		conteudo.add(criarCampoTexto("Mensagem descriptografada", mensagemDescriptografada));
		JButton botaoCriptografar = new JButton("Criptografar");
		JButton botaoDescriptografar = new JButton("Descriptografar");
		JButton botaoLimpar = new JButton("Limpar");
		botaoCriptografar.addActionListener(e -> criptografarMensagem());
		botaoDescriptografar.addActionListener(e -> descriptografarMensagem());
		botaoLimpar.addActionListener(e -> limparMensagens());
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		botoes.add(botaoCriptografar);
		botoes.add(botaoDescriptografar);
		botoes.add(botaoLimpar);
		painel.add(conteudo, BorderLayout.CENTER);
		painel.add(botoes, BorderLayout.SOUTH);
		return painel;
	}

	private JPanel criarAbaArquivos() {
		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(new EmptyBorder(25, 15, 15, 15));
		JPanel campos = new JPanel(new GridLayout(2, 1, 10, 10));
		JPanel campoOrigem = new JPanel(new BorderLayout(5, 5));
		JPanel campoDestino = new JPanel(new BorderLayout(5, 5));
		caminhoOriginal.setEditable(false);
		caminhoDestino.setEditable(false);
		JButton selecionarOrigem = new JButton("Selecionar");
		JButton selecionarDestino = new JButton("Selecionar");
		selecionarOrigem.addActionListener(e -> selecionarArquivo(caminhoOriginal, false));
		selecionarDestino.addActionListener(e -> selecionarArquivo(caminhoDestino, true));
		campoOrigem.add(new JLabel("Arquivo de origem:"), BorderLayout.NORTH);
		campoOrigem.add(caminhoOriginal, BorderLayout.CENTER);
		campoOrigem.add(selecionarOrigem, BorderLayout.EAST);
		campoDestino.add(new JLabel("Arquivo de destino:"), BorderLayout.NORTH);
		campoDestino.add(caminhoDestino, BorderLayout.CENTER);
		campoDestino.add(selecionarDestino, BorderLayout.EAST);
		campos.add(campoOrigem);
		campos.add(campoDestino);
		JButton botaoCriptografar = new JButton("Criptografar arquivo");
		JButton botaoDescriptografar = new JButton("Descriptografar arquivo");
		botaoCriptografar.addActionListener(e -> criptografarArquivo());
		botaoDescriptografar.addActionListener(e -> descriptografarArquivo());
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		botoes.add(botaoCriptografar);
		botoes.add(botaoDescriptografar);
		painel.add(campos, BorderLayout.NORTH);
		painel.add(botoes, BorderLayout.CENTER);
		return painel;
	}

	private JPanel criarAbaHash() {
		JPanel painel = new JPanel(new BorderLayout(10, 10));
		painel.setBorder(new EmptyBorder(15, 10, 10, 10));
		JPanel conteudo = new JPanel(new GridLayout(3, 1, 10, 10));
		textoHash.setLineWrap(true);
		textoHash.setWrapStyleWord(true);
		hashGerado.setEditable(false);
		conteudo.add(criarCampoTexto("Texto para gerar o hash", textoHash));
		conteudo.add(criarCampoTexto("Hash SHA-256 gerado", hashGerado));
		conteudo.add(criarCampoTexto("Hash para comparação", hashComparacao));
		JButton botaoGerar = new JButton("Gerar SHA-256");
		JButton botaoComparar = new JButton("Comparar hashes");
		JButton botaoLimpar = new JButton("Limpar");
		botaoGerar.addActionListener(e -> gerarHash());
		botaoComparar.addActionListener(e -> compararHashes());
		botaoLimpar.addActionListener(e -> {
			textoHash.setText("");
			hashGerado.setText("");
			hashComparacao.setText("");
		});
		JPanel botoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		botoes.add(botaoGerar);
		botoes.add(botaoComparar);
		botoes.add(botaoLimpar);
		painel.add(conteudo, BorderLayout.CENTER);
		painel.add(botoes, BorderLayout.SOUTH);
		return painel;
	}

	private JPanel criarCampoTexto(String titulo, JTextArea area) {
		JPanel painel = new JPanel(new BorderLayout(5, 5));
		JLabel rotulo = new JLabel(titulo);
		rotulo.setFont(new Font("SansSerif", Font.BOLD, 13));
		JScrollPane rolagem = new JScrollPane(area);
		painel.add(rotulo, BorderLayout.NORTH);
		painel.add(rolagem, BorderLayout.CENTER);
		return painel;
	}

	private JPanel criarCampoTexto(String titulo, JTextField campo) {
		JPanel painel = new JPanel(new BorderLayout(5, 5));
		JLabel rotulo = new JLabel(titulo);
		rotulo.setFont(new Font("SansSerif", Font.BOLD, 13));
		painel.add(rotulo, BorderLayout.NORTH);
		painel.add(campo, BorderLayout.CENTER);
		return painel;
	}

	private SecretKey obterChave() throws Exception {
		return keyManager.obterOuCriarChave();
	}

	private void criptografarMensagem() {
		String mensagem = mensagemOriginal.getText();
		if (mensagem.isBlank()) {
			mostrarAviso("Digite uma mensagem para criptografar.");
			return;
		}
		executarOperacao(() -> {
			byte[] dados = mensagem.getBytes(java.nio.charset.StandardCharsets.UTF_8);
			byte[] resultado = encryptionService.criptografar(dados, obterChave());
			return Base64.getEncoder().encodeToString(resultado);
		}, resultado -> {
			mensagemCriptografada.setText(resultado);
			mensagemDescriptografada.setText("");
		});
	}

	private void descriptografarMensagem() {
		String pacote = mensagemCriptografada.getText().trim();
		if (pacote.isBlank()) {
			mostrarAviso("Criptografe uma mensagem ou informe um conteúdo Base64.");
			return;
		}
		executarOperacao(() -> {
			byte[] dadosCriptografados = Base64.getDecoder().decode(pacote);
			byte[] dadosOriginais = decryptionService.descriptografar(dadosCriptografados, obterChave());
			return new String(dadosOriginais, java.nio.charset.StandardCharsets.UTF_8);
		}, mensagemDescriptografada::setText);
	}

	private void limparMensagens() {
		mensagemOriginal.setText("");
		mensagemCriptografada.setText("");
		mensagemDescriptografada.setText("");
	}

	private void selecionarArquivo(JTextField campo, boolean salvar) {
		JFileChooser seletor = new JFileChooser();
		int resultado;
		if (salvar) {
			resultado = seletor.showSaveDialog(this);
		} else {
			resultado = seletor.showOpenDialog(this);
		}
		if (resultado == JFileChooser.APPROVE_OPTION) {
			campo.setText(seletor.getSelectedFile().getAbsolutePath());
		}
	}

	private void criptografarArquivo() {
		String origem = caminhoOriginal.getText();
		String destino = caminhoDestino.getText();
		if (origem.isBlank() || destino.isBlank()) {
			mostrarAviso("Selecione os arquivos de origem e destino.");
			return;
		}
		executarOperacao(() -> {
			fileService.criptografarArquivo(Path.of(origem), Path.of(destino), obterChave());
			return "Arquivo criptografado com sucesso.";
		}, this::mostrarSucesso);
	}

	private void descriptografarArquivo() {
		String origem = caminhoOriginal.getText();
		String destino = caminhoDestino.getText();
		if (origem.isBlank() || destino.isBlank()) {
			mostrarAviso("Selecione os arquivos de origem e destino.");
			return;
		}
		executarOperacao(() -> {
			fileService.descriptografarArquivo(Path.of(origem), Path.of(destino), obterChave());
			return "Arquivo descriptografado com sucesso.";
		}, this::mostrarSucesso);
	}

	private void gerarHash() {
		String texto = textoHash.getText();
		if (texto.isBlank()) {
			mostrarAviso("Digite um texto para gerar o hash.");
			return;
		}
		executarOperacao(() -> {
			byte[] dados = texto.getBytes(java.nio.charset.StandardCharsets.UTF_8);
			byte[] hash = hashService.gerarHash(dados);
			return HexFormat.of().formatHex(hash);
		}, hashGerado::setText);
	}

	private void compararHashes() {
		String primeiro = hashGerado.getText().trim();
		String segundo = hashComparacao.getText().trim();
		if (primeiro.isBlank() || segundo.isBlank()) {
			mostrarAviso("Informe os dois hashes para comparação.");
			return;
		}
		executarOperacao(() -> {
			byte[] hash1 = HexFormat.of().parseHex(primeiro);
			byte[] hash2 = HexFormat.of().parseHex(segundo);
			return hashService.compararHashes(hash1, hash2);
		}, resultado -> {
			if (resultado) {
				mostrarSucesso("Os hashes são iguais.");
			} else {
				mostrarAviso("Os hashes são diferentes.");
			}
		});
	}

	private <T> void executarOperacao(Callable<T> operacao, Consumer<T> aoConcluir) {
		setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
		new SwingWorker<T, Void>() {
			@Override
			protected T doInBackground() throws Exception {
				return operacao.call();
			}

			@Override
			protected void done() {
				setCursor(java.awt.Cursor.getDefaultCursor());
				try {
					T resultado = get();
					aoConcluir.accept(resultado);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					mostrarErro("A operação foi interrompida.");
				} catch (ExecutionException e) {
					Throwable causa = e.getCause();
					while (causa.getCause() != null) {
						causa = causa.getCause();
					}
					if (causa instanceof AEADBadTagException) {
						mostrarErro("Falha na autenticação: os dados podem "
								+ "ter sido alterados ou a chave está incorreta.");
					} else if (causa instanceof IllegalArgumentException && causa.getMessage() != null
							&& causa.getMessage().contains("hex")) {
						mostrarErro("Um dos hashes possui formato hexadecimal inválido.");
					} else {
						mostrarErro(causa.getMessage() != null ? causa.getMessage()
								: "Não foi possível executar a operação.");
					}
				}
			}
		}.execute();
	}

	private void mostrarAviso(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
	}

	private void mostrarSucesso(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Operação concluída", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostrarErro(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Erro na operação", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Não foi possível aplicar o visual do sistema.");
		}
		SwingUtilities.invokeLater(() -> {
			char[] senhaKeyStore = "SenhaSegura123!".toCharArray();
			try {
				KeyManager keyManager = new KeyManager(Path.of("dados", "securevault.p12"), senhaKeyStore);
				EncryptionService encryptionService = new EncryptionService();
				DecryptionService decryptionService = new DecryptionService();
				FileService fileService = new FileService(encryptionService, decryptionService);
				HashService hashService = new HashService();
				Menu menu = new Menu(keyManager, encryptionService, decryptionService, fileService, hashService);
				menu.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Não foi possível iniciar o SecureVault:\n" + e.getMessage(),
						"Erro de inicialização", JOptionPane.ERROR_MESSAGE);
			} finally {
				java.util.Arrays.fill(senhaKeyStore, '\0');
			}
		});
	}
}
