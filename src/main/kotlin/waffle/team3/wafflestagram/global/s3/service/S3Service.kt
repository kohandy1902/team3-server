package waffle.team3.wafflestagram.global.s3.service

import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class S3Service(
    private val s3Client: AmazonS3Client
) {

    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    fun deleteObj(objectKey: String) {
        s3Client.deleteObject(bucket, objectKey)
    }
}
