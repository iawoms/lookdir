package look.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.util.*;

public class LDir {

    public String path;
    public String wildcard;
    public int rollDays = 3;
    public Vector<LFile> lFiles;

    public LDir(String path, String wildcard) {
        this.path = path;
        this.wildcard = wildcard;
        lFiles = new Vector<>();
    }

    public void loadFiles() {
        List<File> files = new ArrayList<>();
        File f = new File(path);
        if (f.exists() && f.isDirectory()) {
            Collection<File> sfiles = FileUtils.listFiles(f, new WildcardFileFilter(wildcard, IOCase.SENSITIVE),
                    new NotFileFilter(DirectoryFileFilter.DIRECTORY));
            files.addAll(sfiles);
        }
        for (File file : files) {
            lFiles.add(new LFile(file));
        }
    }

    public void rollDel() {
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DATE, -rollDays);
        long expireTime = cc.getTime().getTime();
        for (LFile lf : lFiles) {
            if (lf.modTime.getTime() < expireTime) {
                lf.del();
            }
        }
    }
}
