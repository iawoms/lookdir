package look.t;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.MINIMIZE_QUOTES;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.SPLIT_LINES;
import static com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature.WRITE_DOC_START_MARKER;

public class LookUtils {

    public static final String DATETIME_PARTEN = "yyyy-MM-dd HH:mm:ss";
    public static final DecimalFormat DECF = new DecimalFormat("#.00");
    public static final String usrdir = System.getProperty("user.dir");
    public static final String TASKCONF= usrdir + "/task.yml";
    static ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(WRITE_DOC_START_MARKER).disable(SPLIT_LINES).enable(MINIMIZE_QUOTES));
    public static void saveTasks(Map hubs) throws IOException {
        mapper.writeValue(new File(TASKCONF), hubs);
    }
}
