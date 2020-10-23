import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class TestUtils {

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
