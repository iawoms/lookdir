package look.model;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.util.Date;

import static look.t.LookUtils.DATETIME_PARTEN;

public class LFile {
    public String name;
    public Date modTime;
    public String length;
    public String path;

    private File file;

    public LFile(File f) {
        name = f.getName();
//        modTime = DateFormatUtils.format(new Date(f.lastModified()), DATETIME_PARTEN);
        modTime = new Date(f.lastModified());
        length = FileUtils.byteCountToDisplaySize(f.length());
        path = f.getPath();
        file = f;
    }

    public void del() {
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "LFile{" +
                "name='" + name + '\'' +
                ", modTime='" + modTime + '\'' +
                ", length='" + length + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public static void main(String[] args) {
        File f = new File("d:/presto-backup.zip");
        LFile lf = new LFile(f);
        System.out.println(lf);
    }
}
