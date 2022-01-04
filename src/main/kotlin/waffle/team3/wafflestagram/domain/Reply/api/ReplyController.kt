package waffle.team3.wafflestagram.domain.Reply.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.Reply.dto.ReplyDto
import waffle.team3.wafflestagram.domain.Reply.service.ReplyService
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.auth.CurrentUser
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/reply")
class ReplyController(
    private val replyService: ReplyService,
) {
    @PostMapping("/")
    fun createReply(
        @Valid @RequestBody createRequest: ReplyDto.CreateRequest,
        @CurrentUser user: User,
        @RequestParam("commentId") commentId: Long
    ): ResponseEntity<ReplyDto.Response> {
        val newReply = replyService.create(createRequest, user, commentId)
        return ResponseEntity.ok().body(ReplyDto.Response(newReply))
    }

    @GetMapping("/")
    fun getReply(@RequestParam("id") id: Long): ResponseEntity<ReplyDto.Response> {
        val reply = replyService.get(id)
        return ResponseEntity.ok().body(ReplyDto.Response(reply))
    }

    @GetMapping("/list/")
    fun getReplyList(
        @RequestParam("commentId") commentId: Long,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<ReplyDto.Response>> {
        val replyList = replyService.getList(commentId, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(replyList.map { ReplyDto.Response(it) })
    }

    @PutMapping("/")
    fun updateReply(
        @Valid @RequestBody updateRequest: ReplyDto.UpdateRequest,
        @RequestParam("id") id: Long
    ): ResponseEntity<ReplyDto.Response> {
        val reply = replyService.update(updateRequest, id)
        return ResponseEntity.ok().body(ReplyDto.Response(reply))
    }

    @DeleteMapping("/")
    fun deleteReply(@RequestParam("id") id: Long) {
        replyService.delete(id)
    }
}
