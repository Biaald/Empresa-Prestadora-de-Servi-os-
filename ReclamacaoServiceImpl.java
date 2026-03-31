import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.ByteArrayInputStream;

// Serviço 1: Implementa a interface Reclamação
public class ReclamacaoServiceImpl extends UnicastRemoteObject implements Reclamacao {
    
    // Banco de dados simulado em memória (Protocolo -> Dados da Reclamação)
    private Map<String, String> chamadosAbertos;

    public ReclamacaoServiceImpl() throws RemoteException {
        super();
        this.chamadosAbertos = new HashMap<>();
    }

    // MANTEMOS APENAS ESTA VERSÃO (COM OS LOGS PARA O SERVIDOR)
    @Override
    public String registrarReclamacao(String numeroLinha, String motivo) throws RemoteException {
        // ESTA LINHA APARECERÁ NO TERMINAL DO SERVIDOR
        System.out.println("[INFO] Cliente conectado! Recebendo reclamação para a linha: " + numeroLinha);
        System.out.println("[DETALHE] Motivo: " + motivo);

        // Gera um número de protocolo único (8 caracteres em maiúsculo)
        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Salva no "banco de dados" em memória
        chamadosAbertos.put(protocolo, "Linha: " + numeroLinha + " - Motivo: " + motivo + " | Status: EM ANÁLISE");

        System.out.println("[SUCESSO] Protocolo " + protocolo + " gerado e enviado ao cliente.\n");
        return "Reclamação registrada com sucesso! Seu protocolo é: " + protocolo;
    }

    @Override
    public String consultarStatusReclamacao(String protocolo) throws RemoteException {
        System.out.println("[INFO] Cliente consultando status do protocolo: " + protocolo);
        return chamadosAbertos.getOrDefault(protocolo, "Protocolo não encontrado no sistema.");
    }

    @Override
    public String ativarServicoOtimizado(byte[] dadosBinarios) throws RemoteException {
        try {
            // Usa o SEU LinhaInputStream para ler do array de bytes
            ByteArrayInputStream bais = new ByteArrayInputStream(dadosBinarios);
            LinhaInputStream lis = new LinhaInputStream(bais);
            
            // Reconstrói o objeto Linha que veio do cliente
            Linha[] linhas = lis.lerDados();
            Linha l = linhas[0]; 

            System.out.println("[STREAM] Objeto reconstruído: " + l.getTitular() + " - " + l.getNumeroTelefone());
            return "Serviço ativado com sucesso para " + l.getTitular();
            
        } catch (Exception e) {
            return "Erro na desserialização customizada: " + e.getMessage();
        }
    }
}
class GerenciadorLinhaServiceImpl {
    
    private Map<String, Linha> bancoDeLinhas;

    public GerenciadorLinhaServiceImpl() {
        this.bancoDeLinhas = new HashMap<>();
    }

    // Método remoto simulado para cadastrar uma nova linha
    public void cadastrarLinha(String numero, String titular) {
        Linha novaLinha = new Linha(numero, titular);
        bancoDeLinhas.put(numero, novaLinha);
        System.out.println("Linha " + numero + " ativada para " + titular);
    }

    // Método remoto simulado para o cliente ativar um serviço (Siga-me, Secretaria)
    public String ativarServicoNaLinha(String numeroLinha, Servico servico) {
        Linha linha = bancoDeLinhas.get(numeroLinha);
        if (linha != null) {
            linha.adicionarServico(servico);
            return "Serviço " + servico.getNome() + " ativado com sucesso na linha " + numeroLinha;
        }
        return "Erro: Linha não encontrada.";
    }
}

