package look.core;

import org.hyperic.sigar.FileSystemUsage;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Look {
    static ConcurrentMap<String, LHub> lookMap = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                FileSystemView fsv = FileSystemView.getFileSystemView();
                String driveType = fsv.getSystemTypeDescription(aDrive);
                String drivePath  = aDrive.getPath();
                LHub hub = new LHub(drivePath);
                lookMap.put(drivePath,hub);
            }
        }
        for (Map.Entry<String, LHub> hubEntry : lookMap.entrySet()) {
            System.out.println(hubEntry.getKey() + "  " + hubEntry.getValue().maxPresent);
        }
    }
}
