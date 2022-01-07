package waffle.team3.wafflestagram.domain.Photo.dto

import waffle.team3.wafflestagram.domain.Photo.model.Photo

class PhotoDto {
    data class Response(
        val key: String,
        val url: String,
    ) {
        constructor(photo: Photo) : this(
            key = photo.key,
            url = photo.url
        )
    }
}
