# projeto-telefonia-
Modelagem de dominio de servicos telefonicos (Servico, Linha, ativacao de servicos); Streams customizados para serializar e desserializar objetos Linha em formato binario. Tambem existe um servico remoto simples de reclamacoes via RMI.
# 📞 Sistema de Linhas Telefônicas e Streams Customizados

Este projeto demonstra modelagem de domínio para serviços telefônicos (Serviço, Linha, ativação) com streams customizados para serialização/deserialização binária de objetos **Linha**. Inclui também um serviço RMI simples para reclamações.

[![Java CI](https://github.com/Biaald/Empresa-Prestadora-de-Servi-os-/workflows/Java%20CI/badge.svg)](https://github.com/Biaald/Empresa-Prestadora-de-Servi-os-/actions)

## ✨ Visão Geral

O projeto contém **dois blocos principais**:

1. **Modelagem de Domínio**: `Servico` (abstrato), `SigaMe`, `Secretaria`, `Linha`
2. **Streams Customizados**: Serialização binária própria para objetos `Linha` 
3. **Serviço RMI**: Gerenciamento de reclamações remotas

## 📁 Estrutura do Projeto
├── Servico.java # Modelo base de serviços (abstrato)
├── Linha.java # Linha telefônica + serviços contratados
├── LinhaOutputStream.java # Serialização binária → OutputStream
├── LinhaInputStream.java # Desserialização binária ← InputStream
├── Reclamacao.java # Interface RMI reclamações
├── ReclamacaoServiceImpl.java # Implementação RMI + GerenciadorLinha
└── TestesStreams.java # Demo: arquivo, TCP, System I/O
