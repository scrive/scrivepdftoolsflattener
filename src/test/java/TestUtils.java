import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class TestUtils {

    public static boolean checkDocumentIsFlat(byte[] fileFlattened) throws IOException, DocumentException {
        boolean isflat;
        try (PdfReaderAutoclosable reader = new PdfReaderAutoclosable(fileFlattened); ByteArrayOutputStream bos = new ByteArrayOutputStream();
             PdfStamperAutoclosable stamper = new PdfStamperAutoclosable(reader, bos)) {
            AcroFields fields = stamper.getAcroFields();
            isflat = fields.setField("name", "ChangedName");
        }
        return isflat;
    }

    private static class PdfReaderAutoclosable extends PdfReader implements AutoCloseable {
        public PdfReaderAutoclosable(byte[] pdfIn) throws IOException {
            super(pdfIn);
        }
    }

    private static class PdfStamperAutoclosable extends PdfStamper implements AutoCloseable {
        public PdfStamperAutoclosable(PdfReader reader, OutputStream os) throws IOException, DocumentException {
            super(reader, os);
        }
    }
}
