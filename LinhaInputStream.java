import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LinhaInputStream extends InputStream {
    private InputStream inSource;

    // Construtor recebe a origem dos dados
    public LinhaInputStream(InputStream inSource) {
        this.inSource = inSource;
    }

    // Método obrigatório por herdar de InputStream
    @Override
    public int read() throws IOException {
        return inSource.read();
    }

    // Método para ler e reconstruir os objetos
    public Linha[] lerDados() throws IOException {
        DataInputStream dis = new DataInputStream(inSource);
        
        int numObjetos = dis.readInt();
        Linha[] linhasLidas = new Linha[numObjetos];
        
        for (int i = 0; i < numObjetos; i++) {
            // Lendo Atributo 1
            int tamNumero = dis.readInt();
            byte[] numeroBytes = new byte[tamNumero];
            dis.readFully(numeroBytes);
            String numero = new String(numeroBytes);
            
            // Lendo Atributo 2
            int tamTitular = dis.readInt();
            byte[] titularBytes = new byte[tamTitular];
            dis.readFully(titularBytes);
            String titular = new String(titularBytes);
            
            // Lendo Atributo 3
            int tamQtd = dis.readInt(); // sabemos que é 4 bytes, lemos só pra descartar o cabeçalho
            int qtdServicos = dis.readInt();
            
            linhasLidas[i] = new Linha(numero, titular);
            // (A quantidade de serviços foi recuperada, mas os serviços em si
            // precisariam ser serializados da mesma forma para restaurá-los 100%).
        }
        return linhasLidas;
    }
}