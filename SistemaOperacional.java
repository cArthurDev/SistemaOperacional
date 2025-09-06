import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class SistemaOperacional {

    private static final int QUANTUM = 1000;
    private static final double CHANCE_IO = 0.01; // 1%
    private static final double CHANCE_DESBLOQUEIO = 0.30; // 30%

    private List<Processo> todosOsProcessos;
    private Queue<Processo> filaProntos;
    private List<Processo> listaBloqueados;
    private List<Processo> listaTerminados;
    private Random aleatorio;

    public SistemaOperacional() {
        this.todosOsProcessos = new ArrayList<>();
        this.filaProntos = new LinkedList<>();
        this.listaBloqueados = new ArrayList<>();
        this.listaTerminados = new ArrayList<>();
        this.aleatorio = new Random();
        inicializarProcessos();
    }

    private void inicializarProcessos() {
        int[] tempos = {10000, 5000, 7000, 3000, 3000, 8000, 2000, 5000, 4000, 10000};
        for (int i = 0; i < 10; i++) {
            Processo p = new Processo(i, tempos[i]);
            todosOsProcessos.add(p);
            filaProntos.add(p);
        }
        System.out.println(">>> Sistema Operacional iniciado com 10 processos na fila de prontos.");
        TabelaDeProcessos.gravar(todosOsProcessos);
    }

    public void simular() {
        while (listaTerminados.size() < 10) {
            verificarProcessosBloqueados();

            if (filaProntos.isEmpty()) {
                System.out.println("\nCPU Ociosa... Aguardando processos ficarem prontos.");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            Processo processoAtual = filaProntos.poll();

            processoAtual.setEstado(Estado.EXECUTANDO);
            processoAtual.incrementarNCpu();
            System.out.printf("\n[CPU] Restaurando contexto do PID %d. Iniciando execução (N_CPU: %d).\n",
                    processoAtual.getPid(), processoAtual.getNCpu());

            boolean sofreuPreempcao = executarCiclos(processoAtual);

            if (processoAtual.isTerminado()) {
                processoAtual.setEstado(Estado.TERMINADO);
                listaTerminados.add(processoAtual);
                System.out.println("\n*******************************************************");
                System.out.printf("PROCESSO PID %d TERMINOU SUA EXECUÇÃO!\n", processoAtual.getPid());
                System.out.println(processoAtual.toString());
                System.out.println("*******************************************************");
            }

            TabelaDeProcessos.gravar(todosOsProcessos);
        }
        System.out.println("\n>>> TODOS OS PROCESSOS FORAM CONCLUÍDOS. SIMULAÇÃO ENCERRADA. <<<");
    }

    private boolean executarCiclos(Processo processo) {
        boolean ocorreuIO = false;

        for (int ciclo = 0; ciclo < QUANTUM; ciclo++) {
            if (processo.isTerminado()) {
                return false;
            }

            processo.setTp(processo.getTp() + 1);

            if (aleatorio.nextDouble() < CHANCE_IO) {
                ocorreuIO = true;
                break;
            }
        }

        imprimirTrocaDeContexto(processo, ocorreuIO);
        return true;
    }

    private void imprimirTrocaDeContexto(Processo processo, boolean ocorreuIO) {
        System.out.println("\n<<<<< TROCA DE CONTEXTO - PID " + processo.getPid() + " >>>>>");

        if (ocorreuIO) {
            processo.setEstado(Estado.BLOQUEADO);
            processo.incrementarNes();
            listaBloqueados.add(processo);
            System.out.println(processo.toString());
            System.out.println("Motivo: Operação de E/S");
            System.out.println("Mudança de Estado: EXECUTANDO >>> BLOQUEADO");
        } else {
            processo.setEstado(Estado.PRONTO);
            filaProntos.add(processo);
            System.out.println(processo.toString());
            System.out.println("Motivo: Fim do Quantum (" + QUANTUM + " ciclos)");
            System.out.println("Mudança de Estado: EXECUTANDO >>> PRONTO");
        }
        System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    private void verificarProcessosBloqueados() {
        listaBloqueados.removeIf(processo -> {
            if (aleatorio.nextDouble() < CHANCE_DESBLOQUEIO) {
                processo.setEstado(Estado.PRONTO);
                filaProntos.add(processo);
                System.out.printf("[EVENTO] PID %d saiu do estado BLOQUEADO e foi para a fila de PRONTOS.\n", processo.getPid());
                return true;
            }
            return false;
        });
    }
}