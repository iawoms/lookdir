package look.monitor;

import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;

/**
 * Created by iawom on 17-8-30.
 */
public abstract class Watcher extends SigarCommandBase {
    private boolean wattching;
    private Thread watcherThead;
    private int loadInterval = 1000;

    public void startWatch() {
        wattching = true;
        watcherThead = new Thread(() -> {
            try {
                while (wattching) {
                    load();
//                    report();
                    Thread.sleep(loadInterval);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        watcherThead.start();
    }

    public void stopWatch() {
        wattching = false;
    }

    public void load() {
        try {
            processCommand(new String[0]);
        } catch (ShellCommandUsageException e) {
            e.printStackTrace();
        } catch (ShellCommandExecException e) {
            e.printStackTrace();
        }
    }

    public abstract Object getReportMessage();

}
