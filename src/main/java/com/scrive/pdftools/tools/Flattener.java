package com.scrive.pdftools.tools;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

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
            PdfStamper stamper;
            PdfReader reader = new PdfReader(args[0]);
            FileOutputStream os = new FileOutputStream(args[1]);
            try {
                stamper = new PdfStamper(reader, os);
            } catch (BadPasswordException e) {
                PdfReader.unethicalreading = true;
                stamper = new PdfStamper(reader, os);
            }
            stamper.setFormFlattening(true);
            stamper.close();
            return;
        } catch (DocumentException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return;
    }
}
