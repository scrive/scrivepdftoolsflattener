package com.scrive.pdftools.tools.utils;

import com.itextpdf.text.pdf.codec.Base64;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileInput {

    static public byte[] getFileContent(JSONObject obj) throws IOException {
        String localFilePath = obj.optString("localFilePath");
        String base64Content = obj.optString("base64Content");
        if (base64Content != null && !base64Content.isEmpty()) {
            return Base64.decode(base64Content);
        } else {
            try (RandomAccessFile f = new RandomAccessFile(localFilePath, "r")) {
                return f.toString().getBytes();
            }
        }
    }
}
