package com.scrive.pdftools.tools.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.IOException;
import java.io.OutputStream;

public class PdfStamperAutoclosable extends PdfStamper implements AutoCloseable {
    public PdfStamperAutoclosable(PdfReader reader, OutputStream os) throws IOException, DocumentException {
        super(reader, os);
    }
}
