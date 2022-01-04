package waffle.team3.wafflestagram.domain.Tag.model

import waffle.team3.wafflestagram.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "tags")
class Tag() : BaseTimeEntity()
