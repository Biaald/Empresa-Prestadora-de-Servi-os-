import java.net.*;
import java.io.*;
import java.util.*;

public class ServidorTCP {
    private static Map<String, String> chamadosAbertos = new HashMap<>();
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("=== SERVIDOR TCP TELECOM - Porta 8080 ===");
            System.out.println("Aguardando clientes...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress());
                
                // Thread por cliente (multiplos simultâneos)
                new Thread(() -> processarCliente(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void processarCliente(Socket clientSocket) {
        try {
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            
            // 1. Lê tipo de requisição (1=registrar, 2=consultar, 3=ativar)
            int tipoRequisicao = dis.readInt();
            
            switch (tipoRequisicao) {
                case 1: // Registrar reclamação
                    processarRegistrar(dis, dos);
                    break;
                case 2: // Consultar status
                    processarConsultar(dis, dos);
                    break;
                case 3: // Ativar serviço (stream custom)
                    processarAtivarStream(dis, dos);
                    break;
            }
            
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Erro cliente: " + e.getMessage());
        }
    }
    
    // ≡ registrarReclamacao()
    private static void processarRegistrar(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[INFO] Cliente conectado! Recebendo reclamação");
        
        String linha = dis.readUTF();
        String motivo = dis.readUTF();
        
        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        chamadosAbertos.put(protocolo, "Linha: " + linha + " - Motivo: " + motivo + " | EM ANÁLISE");
        
        System.out.println("[SUCESSO] Protocolo " + protocolo + " gerado");
        dos.writeUTF("Reclamação registrada! Protocolo: " + protocolo);
    }
    
    // ≡ consultarStatusReclamacao()
    private static void processarConsultar(DataInputStream dis, DataOutputStream dos) throws IOException {
        String protocolo = dis.readUTF();
        System.out.println("[INFO] Consultando: " + protocolo);
        String status = chamadosAbertos.getOrDefault(protocolo, "Protocolo não encontrado");
        dos.writeUTF(status);
    }
    
    // ≡ ativarServicoOtimizado()
    private static void processarAtivarStream(DataInputStream dis, DataOutputStream dos) throws IOException {
        System.out.println("[STREAM] Recebendo dados binários customizados");
        
        // Recebe tamanho do pacote + dados binários
        int tamanhoPacote = dis.readInt();
        byte[] dadosBinarios = new byte[tamanhoPacote];
        dis.readFully(dadosBinarios);
        
        // Usa LinhaInputStream (seu stream custom!)
        ByteArrayInputStream bais = new ByteArrayInputStream(dadosBinarios);
        LinhaInputStream lis = new LinhaInputStream(bais);
        Linha[] linhas = lis.lerDados();
        Linha l = linhas[0];
        
        System.out.println("[STREAM] Reconstruído: " + l.getTitular() + " - " + l.getNumeroTelefone());
        dos.writeUTF("Serviço ativado para " + l.getTitular());
    }
}
