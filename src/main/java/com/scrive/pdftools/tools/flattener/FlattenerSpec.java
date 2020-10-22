package com.scrive.pdftools.tools.flattener;

import com.scrive.pdftools.tools.utils.FileInput;
import org.json.JSONObject;

public class FlattenerSpec {
    public FileInput mainFileInput = null;
    public String documentNumberText;

    static public FlattenerSpec fromJson (final JSONObject obj) {
        final FlattenerSpec spec = new FlattenerSpec();

        final JSONObject mainFileInputObject = obj.getJSONObject("mainFileInput");
        spec.mainFileInput = FileInput.FromJSON(mainFileInputObject);
        spec.documentNumberText = obj.optString("documentNumberText");
        return spec;
    }
}
