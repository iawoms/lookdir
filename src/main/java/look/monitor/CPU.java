package look.monitor;

import look.model.CpuCombine;
import look.model.CpuPerc;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;

/**
 * Created by iawom on 17-8-30.
 */
public class CPU extends Watcher {

    CpuCombine cpuCombine = new CpuCombine();

    @Override
    public void output(String[] args) throws SigarException {
        org.hyperic.sigar.CpuInfo[] infos = this.sigar.getCpuInfoList();
        org.hyperic.sigar.CpuInfo info = infos[0];
        look.model.CpuInfo cpuInfo = cpuCombine.cpuInfo;
        cpuInfo.vendor = info.getVendor();
        cpuInfo.model = info.getModel();
        cpuInfo.mhz = info.getMhz();
        cpuInfo.totalCores = info.getTotalCores();
        cpuInfo.totalSockets = info.getTotalSockets();
        cpuInfo.coresPerSocket = info.getCoresPerSocket();
        cpuInfo.cacheSize = info.getCacheSize();

        CpuPerc[] cpuPercs = cpuCombine.cpuPercs;
        org.hyperic.sigar.CpuPerc[] cpus = this.sigar.getCpuPercList();
        if (cpuPercs == null || cpuPercs.length != cpus.length) {
            cpuPercs = new CpuPerc[cpus.length];
            for (int i = 0; i < cpuPercs.length; i++) {
                cpuPercs[i] = new CpuPerc();
            }
        }
        for (int i = 0; i < cpuPercs.length; i++) {
            reflashModel(cpuPercs[i], cpus[i]);
        }
        reflashModel(cpuCombine.totalCpuPercs, this.sigar.getCpuPerc());
    }

    private void reflashModel(CpuPerc model, org.hyperic.sigar.CpuPerc info) {
        model.user = info.getUser();
        model.sys = info.getSys();
        model.idle = info.getIdle();
        model.wait = info.getWait();
        model.nice = info.getNice();
        model.combined = info.getCombined();
        model.irq = info.getIrq();
        model.softIrq = info.getSoftIrq();
        model.stolen = info.getStolen();
    }

    @Override
    public Object getReportMessage() {
        return cpuCombine;
    }



    public static void main(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        CPU cpu = new CPU();
        cpu.startWatch();
    }
}
