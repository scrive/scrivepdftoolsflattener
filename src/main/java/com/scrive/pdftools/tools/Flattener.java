package com.scrive.pdftools.tools;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.*;


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

            PdfDocument pdfDoc = new PdfDocument(new PdfReader(args[0]), new PdfWriter(args[1]));
            PdfAcroForm fields = PdfAcroForm.getAcroForm(pdfDoc, true);

            fields.flattenFields();
            pdfDoc.close();
            return;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return;
    }
}
