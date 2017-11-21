package simple.ga;


/**
 *
 * @author calum
 */
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVUtils {

    private static final char DEFAULT_SEPARATOR = ',';

    public static void writeLines(Writer w, List<List<String>> csvData) throws IOException {
        for(List<String> row : csvData) {
            writeLine(w, row);
        }
        w.close();
    }
    public static void writeLine(Writer w, List<String> values) throws IOException {

        boolean first = true;

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(DEFAULT_SEPARATOR);
            }
            sb.append(value);
            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }

}
