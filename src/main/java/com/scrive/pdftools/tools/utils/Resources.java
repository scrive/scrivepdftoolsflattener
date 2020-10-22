package com.scrive.pdftools.tools.utils;

import java.io.InputStream;

public class Resources {
    public static InputStream getResource(String res) {
        InputStream str = Resources.class.getResourceAsStream( "/" + res);
        return str;
    }
}
