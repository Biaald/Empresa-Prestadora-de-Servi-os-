import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            // Cria o objeto que implementa o serviço
            ReclamacaoServiceImpl servico = new ReclamacaoServiceImpl();
            
            // Registra o serviço no rmiregistry com um nome único
            Naming.rebind("rmi://localhost/ReclamacaoServiceImpl", servico);
            
            System.out.println("--- Servidor de Reclamações PRONTO ---");
        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}