package com.scrive.pdftools.tools;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.licensekey.LicenseKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
            PdfWriter writer = new PdfWriter(args[1]);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter tempWriter = new PdfWriter(bos);
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(args[0]), tempWriter);
            if(pdfDoc.isTagged()) {
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
                byte[] originalPdf = bos.toByteArray();
                PdfReader originalreader = new PdfReader(new ByteArrayInputStream(originalPdf));
                pdfDoc = new PdfDocument(originalreader);
                PdfDocument untaggedPdf = new PdfDocument(writer);
                pdfDoc.copyPagesTo(1, pdfDoc.getNumberOfPages(), untaggedPdf, new PdfPageFormCopier());
                untaggedPdf.close();
                pdfDoc.close();

            }else {
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
                byte[] pdfOut = bos.toByteArray();
                writer.write(pdfOut);

            }

            return;

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return;
    }
}
