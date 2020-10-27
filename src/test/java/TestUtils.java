import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.scrive.pdftools.tools.utils.PdfReaderAutoclosable;
import com.scrive.pdftools.tools.utils.PdfStamperAutoclosable;

import java.io.*;

public class TestUtils {

    public static boolean checkDocumentIsFlat(byte[] fileFlattened) throws IOException, DocumentException {
        boolean isflat;
        try (PdfReaderAutoclosable reader = new PdfReaderAutoclosable(fileFlattened);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             PdfStamperAutoclosable stamper = new PdfStamperAutoclosable(reader, bos)) {
            AcroFields fields = stamper.getAcroFields();
            isflat = fields.setField("name", "ChangedName");
        }
        return isflat;
    }
}
