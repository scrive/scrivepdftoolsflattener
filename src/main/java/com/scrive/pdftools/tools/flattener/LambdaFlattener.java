package com.scrive.pdftools.tools.flattener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.scrive.pdftools.tools.utils.PdfStamperAutoclosable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LambdaFlattener {

    public static byte[] execute(FlattenerSpec flatSpec) throws IOException, DocumentException {
        try (ByteArrayInputStream file = new ByteArrayInputStream(flatSpec.getFileContent()); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            PdfReader reader = new PdfReader(file);
            try {
                flatten(reader, bos);
            } catch (BadPasswordException e) {
                PdfReader.unethicalreading = true;
                flatten(reader, bos);
            }
            return bos.toByteArray();
        }
    }

    private static void flatten(PdfReader reader, ByteArrayOutputStream bos) throws IOException, DocumentException {
        try (PdfStamperAutoclosable stamper = new PdfStamperAutoclosable(reader, bos)) {
            stamper.setFormFlattening(true);
            stamper.setAnnotationFlattening(true);
        }
    }
}


