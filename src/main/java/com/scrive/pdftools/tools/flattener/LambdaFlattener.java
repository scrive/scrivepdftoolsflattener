package com.scrive.pdftools.tools.flattener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LambdaFlattener {

    public static ByteArrayOutputStream execute(FlattenerSpec flatSpec) throws IOException, DocumentException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ByteArrayInputStream file = new ByteArrayInputStream(flatSpec.mainFileInput.getContent())) {
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
        }
        return bos;
    }
}
