package com.scrive.pdftools.tools.utils;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

public class PdfReaderAutoclosable extends PdfReader implements AutoCloseable {
    public PdfReaderAutoclosable(byte[] pdfIn) throws IOException {
        super(pdfIn);
    }
}
