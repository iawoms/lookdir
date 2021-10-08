package look.core;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static look.t.LookUtils.DECF;

public class LHub {
    public Vector<LDir> lDirs;
    public double threshold = 0.8;
    private File drive;
    private Thread thr;
    private int checkInterval = 5000;
    private boolean runf = true;

    public LHub(String drivePath) {
        drive = new File(drivePath);
        lDirs = new Vector<>();
        FileSystemView fsv = FileSystemView.getFileSystemView();
    }

    public void addDir(LDir dir) {
        lDirs.add(dir);
    }

    public List<LFile> sortedSubFiles() {
        List<LFile> lfiles = new ArrayList<>();
        for (LDir ld : lDirs) {
            List<LFile> lsfiles = ld.listFiles();
            for (LFile lf : lsfiles) {
                if (!lf.delleted) {
                    lfiles.add(lf);
                }
            }
        }
        lfiles.sort((f1, t1) -> {
            return f1.modTime.getTime() > t1.modTime.getTime() ? 1 : -1;
        });
        return lfiles;
    }

    public void lDirsLoadRollDel(){
        for (LDir ld : lDirs) {
            ld.loadFiles();
            ld.rollDel();
        }
    }
    public boolean needDelFile() {
        double present = (double) drive.getFreeSpace() / (double) drive.getTotalSpace();
        return present < 1 - threshold;
    }

    public String thresholdUseage() {
        return DECF.format(((1 - (double) drive.getFreeSpace() / (double) drive.getTotalSpace()) * 100)) + "% / " + threshold * 100 + "%";
    }

    public void startWatch() {
        runf = true;
        thr = new Thread(() -> {
            while (runf) {
                try {
                    lDirsLoadRollDel();
                    if (needDelFile()) {
                        List<LFile> files = sortedSubFiles();
                        for (LFile f : files) {
                            System.out.println("del " + f.name);
                            f.del();
                            if (!needDelFile()) {
                                break;
                            }
                        }
                    }
//                    System.out.println(drive.getPath()  + "  seek ..");
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
        LDir ld = new LDir("C:\\Users\\Administrator\\AppData\\Local\\Temp\\.opera\\Opera Installer", "*.log", 60);
        ld.path = "C:\\Users\\Administrator\\AppData\\Local\\Temp\\.opera\\Opera Installer";
        LHub hub = new LHub("C:");
        hub.addDir(ld);
        hub.startWatch();
    }
}
