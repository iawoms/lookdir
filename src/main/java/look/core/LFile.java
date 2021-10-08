package look.core;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Date;

public class LFile {
    public String name;
    public Date modTime;
    public String length;
    public String path;
    public boolean delleted;
    private File file;

    public LFile(File f) {
        name = f.getName();
//        modTime = DateFormatUtils.format(new Date(f.lastModified()), DATETIME_PARTEN);
        modTime = new Date(f.lastModified());
        length = FileUtils.byteCountToDisplaySize(f.length());
        path = f.getPath();
        file = f;
    }

    public boolean del() {
        try {
            System.out.println(path + " deleted .");
            delleted = file.delete();
            return delleted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
