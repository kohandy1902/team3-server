package waffle.team3.wafflestagram.domain.Reply.dto

import com.fasterxml.jackson.annotation.JsonFormat
import waffle.team3.wafflestagram.domain.Reply.model.Reply
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class ReplyDto {
    data class Response(
        val id: Long,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        val createdAt: LocalDateTime?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        val updatedAt: LocalDateTime? = null,
        val writer: UserDto.Response,
        val text: String,
    ) {
        constructor(reply: Reply) : this(
            id = reply.id,
            createdAt = reply.createdAt,
            updatedAt = reply.updatedAt,
            writer = UserDto.Response(reply.writer),
            text = reply.text,
        )
    }
    data class CreateRequest(
        @field: NotBlank
        val text: String,
    )
    data class UpdateRequest(
        @field: NotBlank
        val text: String,
    )
}
