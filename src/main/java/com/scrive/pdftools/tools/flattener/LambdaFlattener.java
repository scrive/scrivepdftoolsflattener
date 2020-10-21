package com.scrive.pdftools.tools.flattener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LambdaFlattener {

    public static ByteArrayOutputStream execute(FlattenerSpec flatSpec) throws IOException {

        final ByteArrayInputStream file = new ByteArrayInputStream(flatSpec.mainFileInput.getContent());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            PdfStamper stamper;
            PdfReader reader = new PdfReader(file);
            try {
                stamper = new PdfStamper(reader, bos);
            } catch (BadPasswordException e) {
                PdfReader.unethicalreading = true;
                stamper = new PdfStamper(reader, bos);
            }
            stamper.setFormFlattening(true);
            stamper.setAnnotationFlattening(true);
            stamper.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return bos;
    }
}
