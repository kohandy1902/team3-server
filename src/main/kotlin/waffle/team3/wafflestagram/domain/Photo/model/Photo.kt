package waffle.team3.wafflestagram.domain.Photo.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Photo(
    @Column
    val key: String,
    @Column
    val url: String
) : BaseTimeEntity()
