package com.scrive.pdftools.tools.flattener;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.*;

import java.io.*;

public class LambdaFlattener {

    public static ByteArrayOutputStream execute(FlattenerSpec flatSpec) throws IOException {
        final ByteArrayInputStream file = new ByteArrayInputStream(flatSpec.mainFileInput.getContent());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            PdfReader reader = new PdfReader(file);
            PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(bos));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            form.flattenFields();
            pdfDoc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }catch (NullPointerException e) {
            try {
                PdfReader reader = new PdfReader(file);
                PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(bos));
                PdfDictionary acroFormDictionary = pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.AcroForm);
                acroFormDictionary.clear();
                PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
                form.flattenFields();
                pdfDoc.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return bos;
    }
}
