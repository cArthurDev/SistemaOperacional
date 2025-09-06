public class Processo {
    // Atributos do Processo
    private int pid;
    private long tempoTotalExecucao;
    private long tp;
    private long cp;
    private Estado estado;
    private int nes;
    private int nCpu;

    public Processo(int pid, long tempoTotalExecucao) {
        this.pid = pid;
        this.tempoTotalExecucao = tempoTotalExecucao;
        this.tp = 0;
        this.cp = 1;
        this.estado = Estado.PRONTO;
        this.nes = 0;
        this.nCpu = 0;
    }

    public int getPid() {
        return pid;
    }

    public long getTempoTotalExecucao() {
        return tempoTotalExecucao;
    }

    public long getTp() {
        return tp;
    }

    public void setTp(long tp) {
        this.tp = tp;
        this.cp = this.tp + 1;
    }

    public long getCp() {
        return cp;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getNes() {
        return nes;
    }

    public void incrementarNes() {
        this.nes++;
    }

    public int getNCpu() {
        return nCpu;
    }

    public void incrementarNCpu() {
        this.nCpu++;
    }

    public boolean isTerminado() {
        return this.tp >= this.tempoTotalExecucao;
    }

    @Override
    public String toString() {
        return String.format(
                "PID: %d | Estado: %-10s | TP: %5d/%-5d | CP: %5d | N_CPU: %3d | NES: %3d",
                pid, estado, tp, tempoTotalExecucao, cp, nCpu, nes
        );
    }
}