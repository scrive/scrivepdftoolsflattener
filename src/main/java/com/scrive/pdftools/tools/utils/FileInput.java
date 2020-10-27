package com.scrive.pdftools.tools.utils;

import com.itextpdf.text.pdf.codec.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileInput {

    static public byte[] getFileContent(JSONObject obj) throws IOException {
        String localFilePath = obj.optString("localFilePath");
        String base64Content = obj.optString("base64Content");
        if (!StringUtils.isEmpty(base64Content)) {
            return Base64.decode(base64Content);
        } else if (!StringUtils.isEmpty(localFilePath)) {
            try (RandomAccessFile f = new RandomAccessFile(localFilePath, "r")) {
                return f.toString().getBytes();
            }
        } else {
            throw new JSONException("Not valid file input from JSON");
        }
    }
}
