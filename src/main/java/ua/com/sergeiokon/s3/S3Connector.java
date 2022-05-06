package ua.com.sergeiokon.s3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.stereotype.Component;
import ua.com.sergeiokon.repository.S3PropertyRepository;
import ua.com.sergeiokon.repository.entity.S3Property;

@Component
public class S3Connector {

    private final static String ACCESS_KEY = "accessKey";
    private final static String SECRET_KEY = "secretKey";

    private final S3PropertyRepository s3PropertyRepository;

    public S3Connector(S3PropertyRepository s3PropertyRepository) {
        this.s3PropertyRepository = s3PropertyRepository;
    }

    public AmazonS3 getS3client() {
        S3Property accessKey = s3PropertyRepository.findByKey(ACCESS_KEY)
                .orElseThrow(() -> new IllegalArgumentException("accessKey not found"));

        S3Property secretKey = s3PropertyRepository.findByKey(SECRET_KEY)
                .orElseThrow(() -> new IllegalArgumentException("secretKey not found"));

        AWSCredentials credentials = new BasicAWSCredentials(accessKey.getValue(), secretKey.getValue());

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }
}
