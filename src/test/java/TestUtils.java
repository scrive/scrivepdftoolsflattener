import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.*;
import java.util.ArrayList;

public class TestUtils {

    public static String inputFile(String fileName) {
        return System.getProperty("user.dir") + "/src/test/resources/" + fileName;
    }

    public static String outputFile(String fileName) {
        return System.getProperty("user.dir") + "/src/test/resources/" + fileName;
    }

    public static String fromStream(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    public static boolean checkDocumentIsFlat(InputStream fileFlattened) {
        boolean isflat;
        try {
            PdfReader reader = new PdfReader(fileFlattened);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, bos);
            AcroFields fields = stamper.getAcroFields();
            isflat = fields.setField("name", "ChangedName");
            return isflat;
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return false;
        }

    }
}
