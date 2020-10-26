package com.scrive.pdftools.tools.flattener;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LambdaFlattener {

    public static ByteArrayOutputStream execute(FlattenerSpec flatSpec) throws IOException, DocumentException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ByteArrayInputStream file = new ByteArrayInputStream(flatSpec.getFileContent())) {
            PdfReader reader = new PdfReader(file);
            try {
                flatten(reader, bos);
            } catch (BadPasswordException e) {
                PdfReader.unethicalreading = true;
                flatten(reader, bos);
            }
        }
        return bos;
    }

    private static void flatten(PdfReader reader, ByteArrayOutputStream bos) throws IOException, DocumentException {
        try (PdfStamperAutoclosable stamper = new PdfStamperAutoclosable(reader, bos)) {
            stamper.setFormFlattening(true);
            stamper.setAnnotationFlattening(true);
        }
    }

    private static class PdfStamperAutoclosable extends PdfStamper implements AutoCloseable {
        public PdfStamperAutoclosable(PdfReader reader, OutputStream os) throws IOException, DocumentException {
            super(reader, os);
        }
    }

}


