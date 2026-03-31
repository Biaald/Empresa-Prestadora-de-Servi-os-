# Sistema Telecom: RMI e Serializacao Customizada

Este projeto consiste em um sistema distribuido para gerenciamento de linhas telefonicas, integrando o middleware Java RMI com um protocolo de serializacao binaria manual desenvolvido sobre as classes InputStream e OutputStream.

---

## 1. Funcionalidades do Sistema

- Persistencia Binaria: Conversao de objetos da classe Linha para fluxos de bytes.
- RMI Distribuido: Registro e consulta de reclamacoes via Java RMI Registry.
- Polimorfismo: Gerenciamento de servicos (Siga-me e Secretaria) agregados a Linha.
- Interface Interativa: Menu em modo texto para operacoes em tempo real via Scanner.

---

## 2. Arquitetura de Comunicacao

O sistema opera atraves de uma arquitetura cliente-servidor. A camada de transporte utiliza RMI para chamadas de metodo remoto, enquanto a camada de dados utiliza um protocolo binario customizado para otimizar o envio de objetos complexos atraves de arrays de bytes.

---

## 3. Instrucoes para Execucao

Para o funcionamento correto dos servicos, siga a ordem de inicializacao dos terminais:

### Passo 1: Compilacao
Acesse a pasta do projeto e compile todos os modulos:

```
    javac *.java
```

### Passo 2: Inicializacao do Servidor
O servidor e responsavel por iniciar o registro RMI na porta 1099 e publicar o objeto de servico:
    java ServidorRMI

Log esperado: "--- Servidor de Reclamacoes PRONTO ---"

### Passo 3: Execucao do Cliente
Em um novo terminal, execute a interface de usuario:
    java ClienteRMI

---

## 4. Protocolo Binario Customizado (Opcao 3)

A serializacao manual (Opcao 3 do menu) utiliza o padrao Length-Prefix. A estrutura do datagrama binario segue a seguinte ordem rigorosa:

1. numLinhas (int - 4 bytes): Total de objetos serializados no pacote.
2. tamNome (int - 4 bytes): Comprimento da String Nome em bytes.
3. nome (bytes): Dados binarios codificados da String Nome.
4. tamTitular (int - 4 bytes): Comprimento da String Titular em bytes.
5. titular (bytes): Dados binarios codificados da String Titular.
6. qtdServicos (int - 4 bytes): Inteiro representando a quantidade de servicos ativos na linha.

---

## 5. Estrutura do Codigo Fonte

- Linha.java: Modelo de dados principal e agregacao de servicos.
- Servico.java: Classe base abstrata para definicoes de Siga-me e Secretaria.
- LinhaOutputStream.java: Implementacao da serializacao manual (Objeto para Byte).
- LinhaInputStream.java: Implementacao da desserializacao manual (Byte para Objeto).
- Reclamacao.java: Interface remota definindo os contratos RMI.
- ReclamacaoServiceImpl.java: Implementacao da logica de negocios e persistencia em memoria.
- ServidorRMI.java: Classe de inicializacao, criacao de registro e bind.
- ClienteRMI.java: Interface de usuario com menu de selecao e Scanner.
- TestesStreams.java: Modulo de validacao para fluxos em Arquivo, Socket TCP e System IO.

---

## 6. Conceitos de Sistemas Distribuidos Demonstrados

- Programacao Orientada a Objetos: Heranca, Polimorfismo e Agregacao.
- Middleware: Uso de RMI Registry, Stubs e UnicastRemoteObject.
- Marshalling e Unmarshalling: Conversao manual de tipos abstratos para binario.
- Redes: Comunicacao via Sockets TCP e tratamento de falhas de conexao.

---

## 7. Requisitos Tecnicos

- Ambiente: Java JDK 8 ou superior.
- Bibliotecas: Utilizacao exclusiva de pacotes nativos (java.rmi, java.io, java.net, java.util).
- Compatibilidade: Linux, Windows e macOS.

---
