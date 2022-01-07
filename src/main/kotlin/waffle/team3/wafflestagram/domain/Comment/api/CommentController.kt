package waffle.team3.wafflestagram.domain.Comment.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.Comment.dto.CommentDto
import waffle.team3.wafflestagram.domain.Comment.service.CommentService
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.auth.CurrentUser
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/comment")
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("/{feed_id}/")
    fun createComment(
        @Valid @RequestBody createRequest: CommentDto.CreateRequest,
        @CurrentUser user: User,
        @PathVariable("feed_id") feedId: Long
    ): ResponseEntity<CommentDto.Response> {
        val newComment = commentService.create(createRequest, user, feedId)
        return ResponseEntity.ok().body(CommentDto.Response(newComment))
    }

    @GetMapping("/{comment_id}/")
    fun getComment(@PathVariable("comment_id") commentId: Long): ResponseEntity<CommentDto.Response> {
        val comment = commentService.get(commentId)
        return ResponseEntity.ok().body(CommentDto.Response(comment))
    }

    @GetMapping("/list/{feed_id}/")
    fun getCommentList(
        @PathVariable("feed_id") feedId: Long,
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "number", defaultValue = "30") limit: Int,
    ): ResponseEntity<Page<CommentDto.Response>> {
        val commentList = commentService.getList(feedId, PageRequest.of(offset, limit))
        return ResponseEntity.ok().body(commentList.map { CommentDto.Response(it) })
    }

    @PutMapping("/{comment_id}/")
    fun updateComment(
        @Valid @RequestBody updateRequest: CommentDto.UpdateRequest,
        @PathVariable("comment_id") commentId: Long,
    ): ResponseEntity<CommentDto.Response> {
        val comment = commentService.update(updateRequest, commentId)
        return ResponseEntity.ok().body(CommentDto.Response(comment))
    }

    @DeleteMapping("/{comment_id}/")
    fun deleteComment(@PathVariable("comment_id") commentId: Long) {
        commentService.delete(commentId)
    }
}
