package waffle.team3.wafflestagram.domain.Photo.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
class Photo(
    @Lob
    var photo: ByteArray,
) : BaseTimeEntity()