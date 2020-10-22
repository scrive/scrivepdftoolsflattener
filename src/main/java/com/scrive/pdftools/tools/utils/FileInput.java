package com.scrive.pdftools.tools.utils;

import com.itextpdf.io.codec.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileInput {
    private String localFilePath;
    private String base64Content;
    private byte[] content;

    private boolean isValid() {
        return this.localFilePath != null || this.base64Content != null;
    }
    public byte[] getContent() throws IOException {
        if (this.content != null ) {
            return content;
        } else if (this.localFilePath != null && this.localFilePath != "") {
            RandomAccessFile f = new RandomAccessFile(this.localFilePath , "r");
            this.content = new byte[(int)f.length()];
            f.read(this.content);
            f.close();;
        } else if (this.base64Content != null  && this.base64Content != "") {
            this.content = Base64.decode(this.base64Content);
        }
        return this.content;
    }

    static public FileInput FromJSON(JSONObject obj) throws JSONException {
        FileInput fi = new FileInput();

        fi.localFilePath = obj.optString("localFilePath");
        fi.base64Content = obj.optString("base64Content");
        if (!fi.isValid()) {
            throw new JSONException("Not valid FileInput");
        }
        return fi;
    }
}
