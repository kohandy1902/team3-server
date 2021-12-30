package waffle.team3.wafflestagram.global.s3.controller

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.global.s3.service.S3Service

@RestController
@RequestMapping("/api/v1/s3")
class S3Controller(
    private val s3Service: S3Service
) {

    @DeleteMapping("/")
    fun deletePhoto(objectKey: String) {
        s3Service.deleteObj(objectKey)
    }
}
