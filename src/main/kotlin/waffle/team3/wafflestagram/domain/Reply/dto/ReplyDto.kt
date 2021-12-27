package waffle.team3.wafflestagram.domain.Reply.dto

import waffle.team3.wafflestagram.domain.Reply.model.Reply
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class ReplyDto {
    data class Response(
        val id: Long,
        val createdAt: LocalDateTime?,
        val updatedAt: LocalDateTime? = null,
        val writer: String,
        val text: String,
    ) {
        constructor(reply: Reply) : this(
            id = reply.id,
            createdAt = reply.createdAt,
            updatedAt = reply.updatedAt,
            writer = reply.writer,
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
