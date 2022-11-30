import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.*;
import com.scrive.pdftools.tools.utils.KeyLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Map;

public class TestUtils {
    public static void initITextLicence() {
        KeyLoader.loadLicense();
    }

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

    private static String[] PDF_CHANGEABLE_KEYS = {"CreationDate", "ModDate"};

    private static boolean equalPdfObjects(PdfObject o1, PdfObject o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else if (o1.isStream() && o2.isStream()) {
            PdfStream s1 = (PdfStream) o1;
            PdfStream s2 = (PdfStream) o2;
            PdfObject t1 = s1.get(PdfName.Type);
            PdfObject t2 = s2.get(PdfName.Type) ;
            if (equalPdfObjects(t1, t2) && (t1 != null) && t1.equals(PdfName.Metadata)) {
                return true;
            }
            return Arrays.equals(s1.getBytes(),s2.getBytes());
        } else if (o1.isArray() && o2.isArray()) {
            PdfArray a1 = (PdfArray) o1;
            PdfArray a2 = (PdfArray) o2;
            if (a1.size() != a2.size()) {
                return false;
            } else {
                for(int i=0;i<a1.size();i++) {
                    if (!TestUtils.equalPdfObjects(a1.get(i,false),a2.get(i,false))) {
                        return false;
                    }
                }
            }
            return true;
        } else if (o1.isDictionary() && o2.isDictionary()) {
            PdfDictionary d1 = (PdfDictionary) o1;
            PdfDictionary d2 = (PdfDictionary) o2;
            if (d1.size() != d2.size()) {
                return false;
            } else {
                for(PdfName k: d1.keySet()) {
                    if ( Arrays.asList(PDF_CHANGEABLE_KEYS).contains(k.getValue())) {
                        continue;
                    } else if (!TestUtils.equalPdfObjects(d1.get(k,false),d2.get(k,false))) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return o1.getType() == o1.getType() && o1.toString().equals(o2.toString());
        }
    }

    public static boolean equalPdfs(InputStream s1, InputStream s2) throws IOException {
        PdfDocument pdf1 = new PdfDocument(new PdfReader(s1));
        PdfDocument pdf2 = new PdfDocument(new PdfReader(s2));

        if( pdf1.getNumberOfPdfObjects() != pdf2.getNumberOfPdfObjects() ) {
            return false;
        }

        for(int i=0;i<pdf1.getNumberOfPdfObjects();i++) {
            if (!TestUtils.equalPdfObjects( pdf1.getPdfObject(i),  pdf2.getPdfObject(i))) {
                return false;
            }
        }
        return true;

    }
    public static boolean checkDocumentIsFlat(InputStream fileFlattened) throws IOException {
        PdfReader reader = new PdfReader(fileFlattened);
        PdfDocument pdfDoc = new PdfDocument(reader);
        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);
        try{
            PdfFormField nameField = form.getField("name");
            System.out.println(nameField);
            return false;
        }catch (NullPointerException e) {
            return true;
        }

    }

}
