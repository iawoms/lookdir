package look.model;

/**
 * Created by iawom on 17-8-31.
 */
public class FileSystemUsage {
    public String fileSystem;
    public long size;
    public long used;
    public long avail;
    public String mountedOn;
    public String type;
    public String sysType;

    @Override
    public String toString() {
        return "FileSystemUsage{" +
                "fileSystem='" + fileSystem + '\'' +
                ", size=" + size +
                ", used=" + used +
                ", avail=" + avail +
                ", mountedOn='" + mountedOn + '\'' +
                ", type='" + type + '\'' +
                ", sysType='" + sysType + '\'' +
                '}';
    }
}
