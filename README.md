# projeto-telefonia-
Modelagem de dominio de servicos telefonicos (Servico, Linha, ativacao de servicos); Streams customizados para serializar e desserializar objetos Linha em formato binario. Tambem existe um servico remoto simples de reclamacoes via RMI.
# 📞 Sistema de Linhas Telefônicas e Streams Customizados

Este projeto demonstra modelagem de domínio para serviços telefônicos (Serviço, Linha, ativação) com streams customizados para serialização/deserialização binária de objetos **Linha**. Inclui também um serviço RMI simples para reclamações.

## Visão Geral

O projeto contém **dois blocos principais**:

1. **Modelagem de Domínio**: `Servico` (abstrato), `SigaMe`, `Secretaria`, `Linha`
2. **Streams Customizados**: Serialização binária própria para objetos `Linha` 
3. **Serviço RMI**: Gerenciamento de reclamações remotas

## Estrutura do Projeto

## Modelagem de Domínio

### `Servico` (Classe Abstrata)
```java
Servico(nome: String, valorMensal: double)
├── SigaMe(numeroDestino: String)
└── Secretaria(limiteMensagens: int)
```

### `Linha`
```java
Linha(numeroTelefone: String, titular: String)
└── servicosContratados: List<Servico>
    ├── adicionarServico(Servico)
```

## Protocolo Binário Customizado

### **Escrita** (`LinhaOutputStream.enviarDados()`)

### **Leitura** (`LinhaInputStream.lerDados()`)
Reconstrói `Linha[]` lendo formato acima **(serviços não serializados)**.

## Como Executar

### 1. Testes dos Streams
```bash
javac *.java
java TestesStreams
```
**Testa**: arquivo (`lines_dados.bin`), TCP (porta 8080), System I/O.

### 2. Servidor RMI Reclamações
```bash
# Terminal 1 - Registry
rmiregistry

# Terminal 2 - Servidor  
java ReclamacaoServiceImpl
```

### 3. Cliente RMI (exemplo)
```java
Reclamacao service = (Reclamacao) Naming.lookup("rmi://localhost/Reclamacao");
String protocolo = service.registrarReclamacao("1199999", "Fatura errada");
```

## Exemplo de Uso - Streams

```java
// CRIAR DADOS
Linha[] linhas = {
    new Linha("1198765", "João Silva"),
    new Linha("1198766", "Maria Santos")
};
linhas.adicionarServico(new SigaMe("1199999"));

// TESTE ARQUIVO
LinhaOutputStream out = new LinhaOutputStream(
    new FileOutputStream("linhas.bin"), linhas, 2);
out.enviarDados();

Linha[] restauradas = new LinhaInputStream(
    new FileInputStream("linhas.bin")).lerDados();
```

## Arquitetura RMI
