package com.scrive.pdftools.tools;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.crypto.BadPasswordException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class Flattener {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage:");
            System.err.println("    java -jar scrivepdftoolsflattener.jar input output");
            System.exit(-1);
        }
        try {
            PdfReader reader;
            FileOutputStream os = new FileOutputStream(args[1]);
            try {
                reader = new PdfReader(args[0]);
            } catch (BadPasswordException e) {
               reader = new PdfReader(args[0]).setUnethicalReading(true);
            }

            PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(os));
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
            form.flattenFields();
            pdfDoc.close();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return;
    }
}
