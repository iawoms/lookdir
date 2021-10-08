package look.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.util.*;

import static look.t.LookUtils.DATETIME_PARTEN;

public class LDir {

    public String path;
    public String wildcard;
    public int rollDay = -1;
    private Vector<LFile> lFiles;

    public LDir(String path, String wildcard, int rollDay) {
        this.path = path;
        this.wildcard = wildcard;
        this.rollDay = rollDay;
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
        lFiles.clear();
        for (File file : files) {
            lFiles.add(new LFile(file));
        }
    }

    public List<LFile> listFiles() {
        return lFiles;
    }

    public void rollDel() {
        if (rollDay > -1) {
            long expireTime = getExpireTime().getTime();
            for (LFile lf : lFiles) {
                System.out.println(lf.name + " [ " + DateFormatUtils.format(lf.modTime, DATETIME_PARTEN) + " / " + DateFormatUtils.format(getExpireTime(), DATETIME_PARTEN));
                if (lf.modTime.getTime() < expireTime) {
                    lf.del();
                }
            }
        }
    }
    @JsonIgnore
    public Date getExpireTime() {
        Calendar cc = Calendar.getInstance();
        cc.add(Calendar.DATE, -rollDay);
        return cc.getTime();
    }

    public static void main(String[] args) {
        LDir ld = new LDir("/opt/gpdb/v2rayweb", "*.sig", 90);
        ld.loadFiles();
        for (LFile f : ld.lFiles) {
            System.out.println(f.name);
        }
    }
}
