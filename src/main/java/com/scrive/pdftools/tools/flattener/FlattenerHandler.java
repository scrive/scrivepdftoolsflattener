package com.scrive.pdftools.tools.flattener;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class FlattenerHandler {

    public static ByteArrayOutputStream execute(JSONObject flatSpecJSON) throws Exception {
        FlattenerSpec flatSpec = FlattenerSpec.fromJson(flatSpecJSON);
        if (flatSpec == null) {
            throw new Exception("Failed to parse spec from json");
        }
        ByteArrayOutputStream res = LambdaFlattener.execute(flatSpec);
        return res;
    }
}
