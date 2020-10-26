package com.scrive.pdftools.tools;

import com.amazonaws.services.lambda.runtime.Context;
import com.scrive.pdftools.tools.flattener.FlattenerHandler;
import com.scrive.pdftools.tools.utils.S3ForLambda;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LambdaInterface {
    // Request parameters
    private static final String RQ_PARAM_ACTION = "action";
    private static final String RQ_PARAM_ACTION_FLATTEN = "flatten";
    private static final String RQ_PARAM_ACTION_BODY = "body";
    private static final String RQ_PARAM_S3_FILENAME = "s3FileName";

    // Response parameters
    private static final String RESP_FILE_NAME_PREFIX = "result_";
    private static final String RESP_RESULT_S3_FILE_NAME = "resultS3FileName";
    private static final String RESP_SUCCESS = "success";
    private static final String RESP_ERROR = "error";
    private static final String RESP_BODY = "body";
    private static final String RESP_STATUS_CODE = "statusCode";
    private static final String RESP_IS_B64_ENCODED = "isBase64Encoded";

    private static void handleFlattenAction(JSONObject callJSON, OutputStream outputStream) {
        getS3FileName(callJSON, outputStream).ifPresent(s3fileName -> {
            getFileContent(s3fileName, outputStream).ifPresent(fileContent -> {
                final JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(fileContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    LambdaInterface.respondWithError(outputStream, "Can't parse spec as JSON");
                    return;
                }

                try {
                    System.out.println("DEBUG: Started flattening action on document");
                    ByteArrayOutputStream out1 = FlattenerHandler.execute(jsonObject);
                    String resultFileName = RESP_FILE_NAME_PREFIX + s3fileName;
                    S3ForLambda.putStringToAmazonFile(resultFileName, out1.toByteArray());
                    JSONObject response = new JSONObject();
                    response.put(RESP_RESULT_S3_FILE_NAME, resultFileName);
                    response.put(RESP_SUCCESS, true);
                    respondWith(outputStream, response);
                    System.out.println("DEBUG: Finished flattening action on document");
                } catch (Exception e) {
                    e.printStackTrace();
                    respondWithError(outputStream, e.getMessage());
                }
            });
        });
    }

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {

        JSONObject callJSON;
        try {
            String fs = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONObject inputJSON = new JSONObject(fs);
            boolean hasRequestBodyHiddenInJSON = inputJSON.has(RQ_PARAM_ACTION_BODY);
            if (hasRequestBodyHiddenInJSON) {
                callJSON = new JSONObject(inputJSON.getString(RQ_PARAM_ACTION_BODY));
            } else {
                callJSON = inputJSON;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LambdaInterface.respondWithError(outputStream, "Invalid input - call spec has to be a json");
            return;
        }

        if (!callJSON.has(RQ_PARAM_ACTION)) {
            LambdaInterface.respondWithError(outputStream, "Invalid action. Action has to be set");
            return;
        }

        switch (callJSON.getString(RQ_PARAM_ACTION)) {
            case RQ_PARAM_ACTION_FLATTEN:
                System.out.println("DEBUG: Processing flatten action");
                LambdaInterface.handleFlattenAction(callJSON, outputStream);
                return;
            default:
                System.out.println(String.format("ERROR: Action %s isn't permitted", callJSON.getString(RQ_PARAM_ACTION)));
        }
    }

    private static void respondWith(OutputStream outputStream, JSONObject response) {
        JSONObject data = new JSONObject();
        data.put(RESP_STATUS_CODE, 200);
        data.put(RESP_BODY, response.toString());
        data.put(RESP_IS_B64_ENCODED, false);
        byte[] b = data.toString().getBytes();
        try {
            outputStream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static JSONObject errorResponse(String errorMessage) {
        JSONObject response = new JSONObject();
        response.put(RESP_SUCCESS, false);
        response.put(RESP_ERROR, errorMessage);
        return response;
    }

    private static void respondWithError(OutputStream outputStream, String response) {
        respondWith(outputStream, errorResponse(response));
    }

    private static Optional<String> getS3FileName(final JSONObject callJSON, final OutputStream outputStream) {
        try {
            return Optional.of(callJSON.getString(RQ_PARAM_S3_FILENAME));
        } catch (Exception e) {
            e.printStackTrace();
            LambdaInterface.respondWithError(outputStream, "Invalid input - can't fetch s3FileName");
            return Optional.empty();
        }
    }

    private static Optional<String> getFileContent(final String s3FileName, final OutputStream outputStream) {
        try {
            return Optional.of(S3ForLambda.getStringFromAmazonFile(s3FileName));
        } catch (Exception e) {
            e.printStackTrace();
            LambdaInterface.respondWithError(outputStream, "Failed to fetch spec file from S3");
            return Optional.empty();
        }
    }
}
