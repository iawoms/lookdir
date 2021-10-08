package look.monitor;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

/**
 * Created by iawom on 17-8-30.
 */
public class Memorry extends Watcher {
    @Override
    public Object getReportMessage() {
        return null;
    }


    @Override
    public void output(String[] args) throws SigarException {
        Mem mem   = this.sigar.getMem();
        Swap swap = this.sigar.getSwap();

        Object[] header = new Object[] { "total", "used", "free" };

        Object[] memRow = new Object[] {
                format(mem.getTotal()),
                format(mem.getUsed()),
                format(mem.getFree())
        };

        Object[] actualRow = new Object[] {
                format(mem.getActualUsed()),
                format(mem.getActualFree())
        };

        Object[] swapRow = new Object[] {
                format(swap.getTotal()),
                format(swap.getUsed()),
                format(swap.getFree())
        };

        printf("%18s %10s %10s", header);

        printf("Mem:    %10ld %10ld %10ld", memRow);

        //e.g. linux
        if ((mem.getUsed() != mem.getActualUsed()) ||
                (mem.getFree() != mem.getActualFree()))
        {
            printf("-/+ buffers/cache: " + "%10ld %10d", actualRow);
        }

        printf("Swap:   %10ld %10ld %10ld", swapRow);

        printf("RAM:    %10ls", new Object[] { mem.getRam() + "MB" });
    }

    public String getUsageShort() {
        return "Display information about free and used memory";
    }

    private static Long format(long value) {
        return new Long(value / 1024);
    }

    public static void main(String[] args) throws Exception {
        Memorry memorry = new Memorry();
        memorry.startWatch();
    }
}
