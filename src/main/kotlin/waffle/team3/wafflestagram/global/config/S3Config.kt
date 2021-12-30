package waffle.team3.wafflestagram.global.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class S3Config {

    @Value("\${cloud.aws.credentials.access-key}")
    lateinit var accessKey: String
    @Value("\${cloud.aws.credentials.secret-key}")
    lateinit var secretKey: String
    @Value("cloud.aws.region.static")
    lateinit var region: String

    @Bean
    @Primary
    fun setS3Client(): AmazonS3Client {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withRegion(region)
            .build() as AmazonS3Client
    }
}
