package com.scrive.pdftools.tools.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import java.io.*;
import java.util.Optional;

public class S3ForLambda {
    private static final String ENV_PARAM_ACCESS_KEY = "access_key";
    private static final String ENV_PARAM_SECRET_KEY = "secret_key";
    private static final String ENV_PARAM_BUCKET = "bucket";
    private static final String ENV_PARAM_FAKES3_HOST = "fakes3_host";
    private static final String ENV_PARAM_FAKES3_PORT = "fakes3_port";
    private static final Regions S3_REGION = Regions.EU_WEST_1;

    public static String fromStreamToString(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    private static ByteArrayOutputStream fromStreamToByteArrayOutputStream(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final byte[] buffer = new byte[bufferSize];
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (; ; ) {
            int rsz = inputStream.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.write(buffer, 0, rsz);
        }
        return out;
    }

    private static AmazonS3 getS3Client() {
        final AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        getEnv(ENV_PARAM_ACCESS_KEY).ifPresent(accessKey ->
                getEnv(ENV_PARAM_SECRET_KEY).ifPresent(secretKey ->
                        builder.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))));

        getEnv(ENV_PARAM_FAKES3_HOST).ifPresent(s3Host ->
                getEnv(ENV_PARAM_FAKES3_PORT).ifPresent(s3Port ->
                        builder.disableChunkedEncoding()
                                .setEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + s3Host + ":" + s3Port, S3_REGION.getName()))));

        return builder.build();
    }

    public static String getStringFromAmazonFile(String name) throws IOException {
        S3Object object = S3ForLambda.getS3Client().getObject(
                new GetObjectRequest(System.getenv(ENV_PARAM_BUCKET), name));
        InputStream objectData = object.getObjectContent();
        String res = fromStreamToString(objectData);
        objectData.close();
        return res;
    }

    public static ByteArrayOutputStream getBytesFromAmazonFile(String name) throws IOException {
        S3Object object = S3ForLambda.getS3Client().getObject(
                new GetObjectRequest(System.getenv(ENV_PARAM_BUCKET), name));
        InputStream objectData = object.getObjectContent();
        ByteArrayOutputStream res = fromStreamToByteArrayOutputStream(objectData);
        objectData.close();
        return res;
    }

    public static void putStringToAmazonFile(String name, byte[] content) throws IOException {
        ByteArrayInputStream contentStream = new ByteArrayInputStream(content);
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(content.length);
        PutObjectResult object = S3ForLambda.getS3Client().putObject(
                new PutObjectRequest(System.getenv(ENV_PARAM_BUCKET), name, contentStream, meta));

    }

    private static Optional<String> getEnv(String name) {
        return Optional.ofNullable(System.getenv(name));
    }

}
