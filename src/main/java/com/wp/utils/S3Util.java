package com.wp.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Basic S3 operations
 *
 */
public class S3Util {

    /**
     * Amazon S3 connection object ClientConfiguration clientConfiguration=new
     * ClientConfiguration(); Amazon S3 SDK provides http retry by default.
     * 
     * Default request retry policy, including the maximum retry count of 3, the
     * default retry condition and the default back-off strategy. Reference:
     * http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/
     * ClientConfiguration.html
     */
    private AmazonS3 s3;

    /**
     * Connect to S3
     *
     * @param key
     * @param secrect
     */

    public void connect(String key, String secrect)
            throws TargetingFrameworkException {
        try {

            // myAppConfig.getAWS_KEY(),
            // myAppConfig.getAWS_SECRET()
            AWSCredentials credentials = new BasicAWSCredentials(key, secrect);

            s3 = new AmazonS3Client(credentials);
        } catch (Exception e) {
            throw new TargetingFrameworkException(
                    "Error in createing AmazonS3Client " + e, e);
        }
    }

    /**
     * write content to S3 file location
     *
     * @param bucketName
     *            bucketName
     * @param filePath
     *            file path
     * @param content
     *            content to be written to file
     * @throws com.wp.utils.TargetingFrameworkException
     */
    public void writeToFile(String bucketName, String filePath, String content)
            throws TargetingFrameworkException {
        ByteArrayInputStream input = new ByteArrayInputStream(
                content.getBytes());
        try {
            s3.putObject(bucketName, filePath, input, new ObjectMetadata());
        } catch (Exception e) {
            throw new TargetingFrameworkException(
                    "Error in S3 writeToFile : bucketName=[" + bucketName
                            + "];filePath=[" + filePath + "]", e);
        }
    }

    /**
     * read content to S3 file location
     *
     * @param bucketName
     *            bucketName
     * @param key
     *            key
     * @throws com.psl.wp.personalization.targeting.util.TargetingFrameworkException
     */
    public List<String> readFromFile(String bucketName, String key)
            throws TargetingFrameworkException {
        try {
            S3Object s3object = s3.getObject(new GetObjectRequest(bucketName,
                    key));
            // String theString = IOUtils.toString(s3object.getObjectContent(),
            // "UTF-8");
            // System.out.println("Content-Type: "
            // + s3object.getObjectMetadata().getContentType());
            List<String> theStrings = getLinesFromInputStream(s3object
                    .getObjectContent());
            s3object.close();
            return theStrings;
        } catch (Exception e) {
            throw new TargetingFrameworkException(
                    "Error in S3 writeToFile : readFromFile=[" + bucketName
                            + "];key=[" + key + "]", e);
        }

    }

    // convert InputStream to String
    private List<String> getLinesFromInputStream(InputStream is)
            throws IOException {

        BufferedReader br = null;
        List<String> theStrings = new ArrayList<String>();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                theStrings.add(line);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }

        return theStrings;

    }

    public String getMostUpdatedFilename(String bucketName, String prefix) {
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName).withPrefix(prefix));
        SortedMap<Long, String> block = new TreeMap<>();

        for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
            block.put(summary.getLastModified().getTime(), summary.getKey());
        }

        return block.get(block.lastKey());
    }
}
