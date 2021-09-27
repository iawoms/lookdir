package look.core;

import look.model.LDir;
import look.model.LFile;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LHub {
    public Vector<LDir> lDirs;
    public double maxPresent = 0.35;
    private File drive;
    private Thread thr;
    private int checkInterval = 3000;
    private boolean runf = true;

    public LHub(String drivePath) {
        drive = new File(drivePath);
        lDirs = new Vector<>();
        FileSystemView fsv = FileSystemView.getFileSystemView();
    }

    public void addDir(LDir dir) {
        lDirs.add(dir);
    }

    public List<LFile> getSortedSubFiles() {
        List<LFile> lfiles = new ArrayList<>();
        for (LDir ld : lDirs) {
            ld.loadFiles();
            lfiles.addAll(ld.lFiles);
        }
        lfiles.sort((f1, t1) -> {
            return f1.modTime.getTime() > t1.modTime.getTime() ? 1 : -1;
        });
        return lfiles;
    }

    public boolean needDelFile() {
        double present = (double) drive.getFreeSpace() / (double) drive.getTotalSpace();
        System.out.println(present);
        return present < 1 - maxPresent;
    }

    public void startWatch() {
        runf = true;
        thr = new Thread(() -> {
            while (runf) {
                try {
                    if (needDelFile()) {
                        List<LFile> files = getSortedSubFiles();
                        for (LFile f : files) {
                            System.out.println("del " + f.name);
                            f.del();
                            if (!needDelFile()) {
                                break;
                            }
                        }
                    }
                    System.out.println("seek ..");
                    Thread.sleep(checkInterval);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thr.start();
    }

    public void stop() {
        runf = false;
    }

    public static void main(String[] args) throws IOException {
        LDir ld = new LDir("C:\\Users\\Administrator\\AppData\\Local\\Temp\\.opera\\Opera Installer", "*.log");
        ld.path = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\.opera\\Opera Installer";
        LHub hub = new LHub("C:");
        hub.addDir(ld);
        hub.startWatch();
    }
}
