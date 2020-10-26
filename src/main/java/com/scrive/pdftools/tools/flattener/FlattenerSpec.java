package com.scrive.pdftools.tools.flattener;

import com.scrive.pdftools.tools.utils.FileInput;
import org.json.JSONObject;

import java.io.IOException;

public class FlattenerSpec {
    final private byte[] fileContent;
    final private String documentNumberText;

    public FlattenerSpec(byte[] fileContent, String documentNumberText) {
        this.fileContent = fileContent;
        this.documentNumberText = documentNumberText;
    }

    static public FlattenerSpec fromJson(final JSONObject obj) throws IOException {
        final JSONObject mainFileInputObject = obj.getJSONObject("mainFileInput");
        byte[] mainFileInput = FileInput.getFileContent(mainFileInputObject);
        String documentNumberText = obj.optString("documentNumberText");
        final FlattenerSpec spec = new FlattenerSpec(mainFileInput, documentNumberText);
        return spec;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public String getDocumentNumberText() {
        return documentNumberText;
    }
}
