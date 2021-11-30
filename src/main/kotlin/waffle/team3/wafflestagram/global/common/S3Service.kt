package waffle.team3.wafflestagram.global.common

import com.amazonaws.AmazonServiceException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import kotlin.jvm.Throws

@Service
class S3Service(
    private val s3Client: AmazonS3
) {

    @Value("\${aws.s3.bucket}")
    lateinit var bucket: String

    /**
     * When feedService calls this function,
     * you should deliver username, feedCnt for making fileName and counting
     */
    @Throws(AmazonServiceException::class)
    fun upload(username: String, feedCnt: Int, file: MultipartFile): String {
        // if (!getUser(username)) throw UserDoesNotExistException()

        val fileName = username + feedCnt
        val objMeta = ObjectMetadata()

        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArray = ByteArrayInputStream(bytes)

        s3Client.putObject(
            PutObjectRequest(bucket, fileName, byteArray, objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        )

        return s3Client.getUrl(bucket, fileName).toString()
    }

    fun getList(username: String) {
        // if (!getUser(username)) throw UserDoesNotExistException()

        val list = s3Client.listObjectsV2(bucket)
        for (obj in list.objectSummaries) {
            println(obj.key)
        }
    }

    @Throws(AmazonServiceException::class)
    fun deleteObj(objectKey: String) {
        s3Client.deleteObject(bucket, objectKey)
    }
}
