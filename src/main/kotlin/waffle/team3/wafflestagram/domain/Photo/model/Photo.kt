package waffle.team3.wafflestagram.domain.Photo.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
class Photo(
    val s3path : String,
    // need public key to access private photos?

) : BaseTimeEntity()