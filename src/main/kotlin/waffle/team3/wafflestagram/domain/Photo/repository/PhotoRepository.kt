package waffle.team3.wafflestagram.domain.Photo.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import waffle.team3.wafflestagram.domain.Photo.model.Photo

@Repository
interface PhotoRepository : JpaRepository<Photo, Long>
