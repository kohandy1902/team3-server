package waffle.team3.wafflestagram.domain.Reply.dto

import waffle.team3.wafflestagram.domain.Reply.model.Reply
import waffle.team3.wafflestagram.domain.User.dto.UserDto
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.validation.constraints.NotBlank

class ReplyDto {
    data class Response(
        val id: Long,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime? = null,
        val writer: UserDto.Response,
        val text: String,
    ) {
        constructor(reply: Reply) : this(
            id = reply.id,
            createdAt = reply.createdAt!!.truncatedTo(ChronoUnit.SECONDS),
            updatedAt = reply.updatedAt!!.truncatedTo(ChronoUnit.SECONDS),
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
