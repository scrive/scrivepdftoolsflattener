package com.scrive.pdftools.tools.flattener;

import com.scrive.pdftools.tools.utils.FileInput;
import org.json.JSONObject;

import java.io.IOException;

public class FlattenerSpec {
    private final byte[] fileContent;
    private final String documentNumberText;

    public FlattenerSpec(byte[] fileContent, String documentNumberText) {
        this.fileContent = fileContent;
        this.documentNumberText = documentNumberText;
    }

    static public FlattenerSpec fromJson(JSONObject obj) throws IOException {
        JSONObject mainFileInputObject = obj.getJSONObject("mainFileInput");
        byte[] mainFileInput = FileInput.getFileContent(mainFileInputObject);
        String documentNumberText = obj.optString("documentNumberText");
        return new FlattenerSpec(mainFileInput, documentNumberText);
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public String getDocumentNumberText() {
        return documentNumberText;
    }
}
