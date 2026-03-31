import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestesStreams {

    public static void main(String[] args) {
        // Preparando os dados de teste
        Linha[] linhasParaEnviar = new Linha[]{
            new Linha("85999991111", "João Silva"),
            new Linha("11988882222", "Maria Oliveira")
        };
        int numObjetos = linhasParaEnviar.length;

        System.out.println("=== INICIANDO TESTES ===");

        try {
            // ---------------------------------------------------------
            // TESTE 1: Arquivos (FileOutputStream / FileInputStream)
            // ---------------------------------------------------------
            System.out.println("\n[1] Teste com Arquivo (File IO):");
            File tempFile = new File("linhas_dados.bin");
            
            // Escrita
            FileOutputStream fos = new FileOutputStream(tempFile);
            LinhaOutputStream losFile = new LinhaOutputStream(linhasParaEnviar, numObjetos, fos);
            losFile.enviarDados();
            fos.close();
            
            // Leitura
            FileInputStream fis = new FileInputStream(tempFile);
            LinhaInputStream lisFile = new LinhaInputStream(fis);
            Linha[] linhasRecuperadasArq = lisFile.lerDados();
            fis.close();
            imprimirLinhas(linhasRecuperadasArq);

            // ---------------------------------------------------------
            // TESTE 2: Rede TCP (Sockets)
            // ---------------------------------------------------------
            System.out.println("\n[2] Teste com Servidor Remoto (TCP):");
            
            // Thread para o Servidor (Recebe os dados)
            Thread servidor = new Thread(() -> {
                try (ServerSocket serverSocket = new ServerSocket(8080)) {
                    Socket clientSocket = serverSocket.accept();
                    LinhaInputStream lisTCP = new LinhaInputStream(clientSocket.getInputStream());
                    Linha[] linhasRecebidasTCP = lisTCP.lerDados();
                    System.out.println("  (Servidor) Dados recebidos via rede:");
                    imprimirLinhas(linhasRecebidasTCP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            servidor.start();
            
            // Espera o servidor subir
            Thread.sleep(500);
            
            // Cliente (Envia os dados)
            Socket socket = new Socket("localhost", 8080);
            LinhaOutputStream losTCP = new LinhaOutputStream(linhasParaEnviar, numObjetos, socket.getOutputStream());
            losTCP.enviarDados();
            socket.close();
            
            // Aguarda a thread do servidor terminar
            servidor.join();

            // ---------------------------------------------------------
            // TESTE 3: Entrada e Saída Padrão (System.out / System.in)
            // ---------------------------------------------------------
            System.out.println("\n[3] Teste com System IO:");
            System.out.println("  Atenção: Os dados seriais escritos no System.out ficarão truncados no terminal.");
            System.out.println("  Os dados de origem para System.in precisarão ser encadeados corretamente (difícil teste manual).");
            
            // Escrita em System.out
            LinhaOutputStream losSys = new LinhaOutputStream(linhasParaEnviar, numObjetos, System.out);
            losSys.enviarDados();
            System.out.println("\n  (Dados binários impressos acima via System.out)");

            // NOTA PARA O SYSTEM.IN:
            // Para testar o System.in real, você precisaria direcionar um arquivo via prompt de comando 
            // (ex: java TestesStreams < linhas_dados.bin). Abaixo, instanciamos para cumprir o requisito, 
            // mas não chamamos o "lerDados" pois travaria esperando você digitar bytes legíveis.
            LinhaInputStream lisSys = new LinhaInputStream(System.in);
            // Linha[] sysLidas = lisSys.lerDados(); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método utilitário para checar se o dado reconstruído está correto
    private static void imprimirLinhas(Linha[] linhas) {
        for (Linha l : linhas) {
            System.out.println("  -> Restaurado: Titular: " + l.getTitular() + ", Tel: " + l.getNumeroTelefone());
        }
    }
}