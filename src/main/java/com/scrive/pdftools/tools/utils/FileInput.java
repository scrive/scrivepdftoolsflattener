package com.scrive.pdftools.tools.utils;

import com.itextpdf.text.pdf.codec.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class FileInput {
    final String localFilePath;
    final String base64Content;
    private byte[] content;

    public FileInput(String localFilePath, String base64Content) throws IOException {
        this.localFilePath = Objects.requireNonNull(localFilePath);
        this.base64Content = Objects.requireNonNull(base64Content);
        this.content = getContent();
    }

    public byte[] getContent() throws IOException {
        if (this.content != null) {
            return content;
        } else if (this.localFilePath != null && this.localFilePath != "") {
            RandomAccessFile f = new RandomAccessFile(this.localFilePath, "r");
            this.content = f.toString().getBytes();
            f.read(this.content);
        } else if (this.base64Content != null && this.base64Content != "") {
            this.content = Base64.decode(this.base64Content);
        }
        return this.content;
    }

    static public FileInput FromJSON(JSONObject obj) throws JSONException, IOException {
        String localFilePath = obj.optString("localFilePath");
        String base64Content = obj.optString("base64Content");
        FileInput fi = new FileInput(localFilePath, base64Content);
        return fi;
    }
}
