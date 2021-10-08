package look.monitor;

import look.model.FileSystemUsage;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.NfsFileSystem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by iawom on 17-8-30.
 */
public class Disk extends Watcher {
    public Vector<FileSystemUsage> fileSystemUsages = new Vector<>();

    @Override
    public void output(String[] args) throws SigarException {
        try {
            FileSystem[] fslist = this.proxy.getFileSystemList();
            fileSystemUsages.clear();
            for (FileSystem fs : fslist) {
                long used, avail, total;

                try {
                    org.hyperic.sigar.FileSystemUsage usage;
                    if (fs instanceof NfsFileSystem) {
                        NfsFileSystem nfs = (NfsFileSystem) fs;
                        if (!nfs.ping()) {
                            println(nfs.getUnreachableMessage());
                            return;
                        }
                    }
                    usage = this.sigar.getFileSystemUsage(fs.getDirName());
                    used = usage.getTotal() - usage.getFree();
                    avail = usage.getAvail();
                    total = usage.getTotal();
                } catch (SigarException e) {
                    //e.g. on win32 D:\ fails with "Device not ready"
                    //if there is no cd in the drive.
                    used = avail = total = 0;
                }
                FileSystemUsage fsu = new FileSystemUsage();
                fsu.fileSystem = fs.getDevName();
                fsu.size = total;
                fsu.used = used;
                fsu.avail = avail;
                fsu.mountedOn = fs.getDirName();
                fsu.type = fs.getTypeName();
                fsu.sysType = fs.getSysTypeName();
                fileSystemUsages.add(fsu);
            }
        } catch (SigarException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ShellCommandExecException, ShellCommandUsageException {
        Disk disk = new Disk();
        disk.load();

        for (FileSystemUsage fs : disk.fileSystemUsages) {
            if(fs.type.equals("local")){

                System.out.println(fs);
            }
        }

    }

    @Override
    public Object getReportMessage() {
        return fileSystemUsages;
    }

}
