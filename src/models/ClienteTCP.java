import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClienteTCP {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        
        try {
            System.out.println("=== CLIENTE TCP TELECOM - Conectando: localhost:8080 ===");
            
            int opcao = 0;
            while (opcao != 6) {
                System.out.println("\n--- TELECOM ATENDIMENTO ---");
                System.out.println("1. Registrar Reclamação");
                System.out.println("2. Consultar Status");
                System.out.println("3. Ativar Linha Simples");
                System.out.println("4. Múltiplas Linhas");
                System.out.println("5. Linha + Serviços");
                System.out.println("6. Sair");
                System.out.print("Escolha: ");
                
                opcao = Integer.parseInt(leitor.nextLine());
                
                if (opcao >= 1 && opcao <= 5) {
                    try (Socket socket = new Socket("localhost", 8080);
                         DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                         DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                        
                        switch (opcao) {
                            case 1 -> enviarRegistrar(dos, dis, leitor);
                            case 2 -> enviarConsultar(dos, dis, leitor);
                            case 3 -> enviarAtivarSimples(dos, dis, leitor);
                            case 4 -> enviarMultiplas(dos, dis);
                            case 5 -> enviarCompleta(dos, dis);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        } finally {
            leitor.close();
        }
    }
    
    private static void enviarRegistrar(DataOutputStream dos, DataInputStream dis, Scanner leitor) throws IOException {
        System.out.print("Número da Linha: ");
        String linha = leitor.nextLine();
        System.out.print("Motivo: ");
        String motivo = leitor.nextLine();
        
        dos.writeInt(1); // Tipo: registrar
        dos.writeUTF(linha);
        dos.writeUTF(motivo);
        
        String resposta = dis.readUTF();
        System.out.println("\n [SERVIDOR TCP]: " + resposta);
    }
    
    private static void enviarConsultar(DataOutputStream dos, DataInputStream dis, Scanner leitor) throws IOException {
        System.out.print("Protocolo: ");
        String protocolo = leitor.nextLine();
        
        dos.writeInt(2); // Tipo: consultar
        dos.writeUTF(protocolo);
        
        String status = dis.readUTF();
        System.out.println("\n [STATUS]: " + status);
    }
    
    private static void enviarAtivarSimples(DataOutputStream dos, DataInputStream dis, Scanner leitor) throws IOException {
        System.out.print("Titular: ");
        String titular = leitor.nextLine();
        System.out.print("Número: ");
        String numero = leitor.nextLine();
        
        Linha[] linhas = { new Linha(numero, titular) };
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LinhaOutputStream los = new LinhaOutputStream(linhas, 1, baos);
        los.enviarDados();
        
        dos.writeInt(3); // Tipo: ativar stream
        dos.writeInt(baos.size());
        dos.write(baos.toByteArray());
        
        String resposta = dis.readUTF();
        System.out.println("\n [TCP+STREAM]: " + resposta);
    }
    
    private static void enviarMultiplas(DataOutputStream dos, DataInputStream dis) throws IOException {
        Linha[] multiLinhas = {
            new Linha("85999991111", "João Silva"),
            new Linha("11988882222", "Maria Oliveira")
        };
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LinhaOutputStream los = new LinhaOutputStream(multiLinhas, 2, baos);
        los.enviarDados();
        
        dos.writeInt(3);
        dos.writeInt(baos.size());
        dos.write(baos.toByteArray());
        
        String resposta = dis.readUTF();
        System.out.println("\n[TCP+2 LINHAS]: " + resposta);
    }
    
    private static void enviarCompleta(DataOutputStream dos, DataInputStream dis) throws IOException {
        Linha linha = new Linha("85991234567", "Pedro Santos");
        SigaMe sigaMe = new SigaMe(15.99);
        sigaMe.setNumeroDestino("11987654321");
        linha.adicionarServico(sigaMe);
        linha.adicionarServico(new Secretaria(9.99, 50));
        
        Linha[] pacote = { linha };
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        LinhaOutputStream los = new LinhaOutputStream(pacote, 1, baos);
        los.enviarDados();
        
        dos.writeInt(3);
        dos.writeInt(baos.size());
        dos.write(baos.toByteArray());
        
        String resposta = dis.readUTF();
        System.out.println("\n [TCP+POLIMORFISMO]: " + resposta);
    }
}
