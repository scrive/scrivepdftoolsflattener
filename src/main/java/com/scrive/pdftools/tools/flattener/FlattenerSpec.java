package com.scrive.pdftools.tools.flattener;

import com.scrive.pdftools.tools.utils.FileInput;
import org.json.JSONObject;

import java.io.IOException;

public class FlattenerSpec {
        final FileInput mainFileInput;
        final String documentNumberText;

    public FlattenerSpec(FileInput mainFileInput, String documentNumberText) {
        this.mainFileInput = mainFileInput;
        this.documentNumberText = documentNumberText;
    }

    static public FlattenerSpec fromJson (final JSONObject obj) throws IOException {
        final JSONObject mainFileInputObject = obj.getJSONObject("mainFileInput");
        FileInput mainFileInput = FileInput.FromJSON(mainFileInputObject);
        String documentNumberText = obj.optString("documentNumberText");
        final FlattenerSpec spec = new FlattenerSpec(mainFileInput, documentNumberText);
        return spec;
    }

    public FileInput getMainFileInput() {
        return mainFileInput;
    }
}
