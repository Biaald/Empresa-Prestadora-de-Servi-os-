import java.rmi.Naming;
import java.util.Scanner;
import java.io.ByteArrayOutputStream;

public class ClienteRMI {
    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        
        try {
            // Conecta ao servidor
            Reclamacao servico = (Reclamacao) Naming.lookup("rmi://localhost/ReclamacaoServiceImpl");
            
            int opcao = 0;
            while (opcao != 3) {
                System.out.println("\n--- TELECOM ATENDIMENTO ---");
                System.out.println("1. Registrar Nova Reclamação");
                System.out.println("2. Consultar Status de Protocolo");
                System.out.println("3. Nome do titular");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                
                opcao = Integer.parseInt(leitor.nextLine());

                if (opcao == 1) {
                    System.out.print("Número da Linha: ");
                    String linha = leitor.nextLine();
                    System.out.print("Descrição do Problema: ");
                    String motivo = leitor.nextLine();
                    
                    String resposta = servico.registrarReclamacao(linha, motivo);
                    System.out.println("\n[SERVIDOR]: " + resposta);

                } else if (opcao == 2) {
                    System.out.print("Digite o número do protocolo: ");
                    String protocolo = leitor.nextLine();
                    
                    String status = servico.consultarStatusReclamacao(protocolo);
                    System.out.println("\n[STATUS ATUAL]: " + status);
                // Dentro do switch/if de opções do ClienteRMI:
                } else if (opcao == 3) {
                    System.out.println("\n--- ATIVAÇÃO DE LINHA VIA STREAM CUSTOMIZADO ---");
                    System.out.print("Nome do Titular: ");
                    String nome = leitor.nextLine();
                    System.out.print("Número da Linha: ");
                    String num = leitor.nextLine();

                    // 1. Criar o objeto Linha (POJO)
                    Linha novaLinha = new Linha(num, nome);
                    Linha[] listaParaEnviar = { novaLinha };

                    // 2. Preparar o fluxo de bytes em memória
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    
                    // 3. Instanciar a SUA classe LinhaOutputStream
                    // Note que passamos '1' porque vamos enviar apenas 1 objeto
                    LinhaOutputStream los = new LinhaOutputStream(listaParaEnviar, 1, baos);
                    
                    // 4. Executar o seu método de serialização
                    los.enviarDados();

                    // 5. Pegar o resultado binário e enviar ao servidor
                    byte[] pacoteBinario = baos.toByteArray();
                    
                    // ATENÇÃO: Verifique se o nome do método na sua Interface é 'ativarServicoOtimizado'
                    String resposta = servico.ativarServicoOtimizado(pacoteBinario);
                    
                    System.out.println("\n[RESPOSTA]: " + resposta);
                } else if (opcao == 4) {
                    System.out.println("Saindo do sistema...");
                } else {
                    System.out.println("Opção inválida!");
                }
            }

        } catch (Exception e) {
            System.err.println("Erro na comunicação: " + e.getMessage());
        } finally {
            leitor.close();
        }
    }
}
