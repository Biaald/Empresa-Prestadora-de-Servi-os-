import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Reclamacao extends Remote {
    String registrarReclamacao(String linha, String motivo) throws RemoteException;
    String consultarStatusReclamacao(String protocolo) throws RemoteException;
    
    // Novo método: recebe os dados da linha serializados pelo seu stream
    String ativarServicoOtimizado(byte[] dadosBinarios) throws RemoteException;
}