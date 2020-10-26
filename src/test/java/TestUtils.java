import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class TestUtils {

    public static boolean checkDocumentIsFlat(InputStream fileFlattened) throws IOException, DocumentException {
        boolean isflat;
        PdfReader reader = new PdfReader(fileFlattened);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, bos);
        AcroFields fields = stamper.getAcroFields();
        isflat = fields.setField("name", "ChangedName");
        return isflat;

    }

}
