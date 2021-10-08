package look.core;

import look.model.FileSystemUsage;
import look.monitor.Disk;
import look.t.LookUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static look.t.LookUtils.DATETIME_PARTEN;

public class Look {
    static ConcurrentMap<String, LHub> hubs = new ConcurrentHashMap<>();


    public static void initHubs() {
        Disk disk = new Disk();
        disk.load();

        for (FileSystemUsage fs : disk.fileSystemUsages) {
            if (fs.type.equals("local")) {
//                System.out.println(fs);
                String mount = fs.mountedOn;
                LHub hub = new LHub(mount);
                hubs.put(mount, hub);
            }
        }

//        for (Map.Entry<String, LHub> hubEntry : hubs.entrySet()) {
//            System.out.println(hubEntry.getKey() + "  " + hubEntry.getValue().threshold);
//        }
    }

    public static void addDir(String path,String wildcard,int rollDay) {
        List<String> hubList = new ArrayList<>();
        hubList.addAll(hubs.keySet());
        hubList.sort((h1, h2) -> {
            return h2.length() - h1.length();
        });
        for (String s : hubList) {
            if (path.startsWith(s)) {
                hubs.get(s).addDir(new LDir(path, wildcard,rollDay));
                break;
            }
        }
    }

    public static void startAllHubs(){
        for (LHub v : hubs.values()) {
            v.startWatch();
        }
    }
    public static void printHubs() {
        for (Map.Entry<String, LHub> en : hubs.entrySet()) {
            LHub val = en.getValue();
            System.out.println(en.getKey() + " , useage / threshold : " + val.thresholdUseage() );
            for (LDir lDir : val.lDirs) {
                System.out.println(" ┗━ " + lDir.path + ", wildcard : " + lDir.wildcard + ",  day : " + lDir.rollDay + ", expireTime : " + DateFormatUtils.format(lDir.getExpireTime(), DATETIME_PARTEN));
                lDir.loadFiles();
                for (LFile f : lDir.listFiles()) {
                    System.out.println("   - " + f.name + " " + f.length + " " + DateFormatUtils.format(f.modTime , DATETIME_PARTEN)+ " " + f.path);
                }
            }
        }
    }


    public static void main(String[] args) throws IOException {
        Look.initHubs();
        Look.addDir("/ss","*.log",60);
        Look.addDir( "/home/gpadmin/gpAdminLogs","gpstop*.log",60);
        Look.addDir( "/data1/master/gpseg-1/pg_log","gpdb-2021-06*.csv",-1);

        Look.printHubs();

//        Look.startAllHubs();
        LookUtils.saveTasks(hubs);
    }
}
