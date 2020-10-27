package com.scrive.pdftools.tools.flattener;

import org.json.JSONObject;

public class FlattenerHandler {

    public static byte[] execute(JSONObject flatSpecJSON) throws Exception {
        FlattenerSpec flatSpec = FlattenerSpec.fromJson(flatSpecJSON);
        if (flatSpec.getFileContent() == null) {
            throw new Exception("Failed to parse spec from json");
        }
        return LambdaFlattener.execute(flatSpec);
    }
}
