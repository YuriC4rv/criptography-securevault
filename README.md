# SecureVault

Aplicação desenvolvida em Java para criptografia e descriptografia de mensagens e arquivos, utilizando recursos nativos da plataforma Java.

O SecureVault é um projeto prático de segurança da informação que explora conceitos de criptografia, gerenciamento de chaves e integridade dos dados. Seu desenvolvimento utiliza algoritmos e APIs disponibilizados pelo próprio ecossistema Java, sem dependência de bibliotecas externas ou frameworks de criptografia.

## Sobre o projeto

O objetivo do SecureVault é demonstrar, por meio de uma aplicação funcional, como implementar mecanismos de proteção de dados utilizando Java.

Durante o desenvolvimento, são explorados recursos da Java Cryptography Architecture (JCA) e da Java Cryptography Extension (JCE), aplicando conceitos de segurança diretamente no código.

O projeto também utiliza Java NIO para manipulação de arquivos e APIs nativas para geração de números aleatórios seguros.

## Funcionalidades

- Criptografia de mensagens utilizando AES-256-GCM.
- Descriptografia de mensagens mediante validação de autenticação.
- Geração e gerenciamento de chaves criptográficas.
- Criptografia de arquivos.
- Descriptografia e recuperação de arquivos.
- Geração de hashes utilizando SHA-256.
- Verificação de integridade dos dados.
- Interface gráfica desenvolvida com Java Swing.

## Tecnologias utilizadas

| Tecnologia | Aplicação |
|---|---|
| Java 17 ou superior | Linguagem e plataforma de desenvolvimento |
| AES-256-GCM | Criptografia autenticada |
| SHA-256 | Geração de hashes |
| JCA | Arquitetura de serviços criptográficos |
| JCE | Recursos de criptografia do Java |
| Java NIO | Manipulação de arquivos |
| SecureRandom | Geração de números aleatórios seguros |
| Java Swing | Interface gráfica |

## Organização do projeto

O projeto utiliza uma organização baseada em responsabilidades, separando os componentes responsáveis pelas operações criptográficas, gerenciamento de chaves, manipulação de arquivos e interface da aplicação.

Principais componentes:

| Componente | Responsabilidade |
|---|---|
| Main | Inicialização da aplicação |
| EncryptionService | Implementação das operações de criptografia |
| DecryptionService | Implementação das operações de descriptografia |
| KeyManager | Geração e gerenciamento de chaves |
| FileService | Manipulação e processamento de arquivos |
| HashService | Geração de hashes SHA-256 |
| Menu | Interface e interação com o usuário |

## Requisitos

Para executar o projeto, você precisará de:

- Java Development Kit (JDK) 17 ou superior.
- Eclipse IDE ou outra IDE compatível com Java.
- Git, caso deseje clonar o repositório.

## Como executar

1. Clone este repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Abra o Eclipse IDE.

3. Importe o projeto para o workspace utilizando a opção `Existing Projects into Workspace`.

4. Localize a classe principal da aplicação.

5. Execute o projeto como uma aplicação Java.

## Conceitos de segurança abordados

O SecureVault trabalha com diferentes conceitos da segurança da informação:

- Criptografia simétrica e autenticação de dados.
- Geração e utilização de chaves criptográficas.
- Proteção de informações armazenadas em arquivos.
- Funções hash e integridade dos dados.
- Tratamento de falhas durante operações criptográficas.
- Utilização de APIs nativas de segurança do Java.

O projeto utiliza algoritmos criptográficos disponibilizados pela plataforma Java. Ele não implementa algoritmos criptográficos próprios.

## Objetivo educacional

Este projeto foi desenvolvido como parte do curso **Criptografia na Prática com Java**, com a proposta de transformar conceitos de segurança da informação em uma experiência prática de desenvolvimento.

A intenção é proporcionar uma compreensão mais próxima de como os recursos criptográficos podem ser utilizados em aplicações Java, explorando suas APIs, funcionalidades e responsabilidades.

O SecureVault também pode servir como referência para estudos, experimentos e projetos futuros relacionados à criptografia e à segurança de aplicações.

## Aviso de segurança

O SecureVault possui finalidade educacional e demonstrativa. Apesar de utilizar algoritmos criptográficos reconhecidos, o projeto não deve ser considerado uma solução de armazenamento seguro pronta para uso em ambientes de produção sem uma avaliação técnica, auditoria de segurança e revisão dos procedimentos de gerenciamento e armazenamento de chaves.

A segurança de uma aplicação criptográfica depende não apenas dos algoritmos utilizados, mas também de sua implementação, configuração e utilização.

---

Desenvolvido como projeto prático de estudo em Java e segurança da informação.
