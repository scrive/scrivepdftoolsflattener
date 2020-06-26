package com.scrive.pdftools.tools;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.licensekey.LicenseKey;

import java.io.IOException;

public class Flattener {
    public static void main(String[] args) {
        if (args.length != 2) {

            System.err.println("Usage:");
            System.err.println("    java -jar scrivepdftoolsflattener.jar input output");
            System.exit(-1);
        }
        try {
            try {
                LicenseKey.loadLicenseFile( "scrivepdftools/itextkey.xml" );
            } catch (Exception e) {}
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(args[0]), new PdfWriter(args[1]));

            try {
              PdfAcroForm fields = PdfAcroForm.getAcroForm(pdfDoc, true);
              fields.flattenFields();
            } catch (NullPointerException e) {
                PdfDictionary acroFormDictionary = pdfDoc.getCatalog().getPdfObject().getAsDictionary(PdfName.AcroForm);
                acroFormDictionary.clear();
                PdfAcroForm fields = PdfAcroForm.getAcroForm(pdfDoc, true);
                fields.flattenFields();
            }

            pdfDoc.close();
            return;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return;
    }
}
