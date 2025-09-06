import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TabelaDeProcessos {

    private static final String NOME_ARQUIVO = "tabela_processos.txt";

    public static void gravar(List<Processo> processos) {
        try (FileWriter fileWriter = new FileWriter(NOME_ARQUIVO);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            printWriter.println("============================== TABELA DE PROCESSOS ==============================");
            printWriter.println("Gerado em: " + new java.util.Date());
            printWriter.println("---------------------------------------------------------");

            for (Processo p : processos) {
                printWriter.println(p.toString());
            }

            printWriter.println("==================================================================================");

        } catch (IOException e) {
            System.err.println("Erro ao gravar a tabela de processos: " + e.getMessage());
        }
    }
}