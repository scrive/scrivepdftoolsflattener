package com.scrive.pdftools.tools.utils;

import com.itextpdf.licensekey.LicenseKey;

public class KeyLoader {
    public static void loadLicense() {
        LicenseKey.loadLicenseFile(Resources.getResource("itextkey.xml"));
    }
}
