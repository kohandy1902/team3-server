package waffle.team3.wafflestagram.domain.Comment.api

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
    @PostMapping("/")
    fun createComment(
        @Valid @RequestBody createRequest: CommentDto.CreateRequest,
        @CurrentUser user: User,
        @RequestParam("feedId") feedId: Long
    ): ResponseEntity<CommentDto.Response> {
        val newComment = commentService.create(createRequest, user, feedId)
        return ResponseEntity.ok().body(CommentDto.Response(newComment))
    }

    @GetMapping("/")
    fun getComment(@RequestParam("id") id: Long): ResponseEntity<CommentDto.Response> {
        val comment = commentService.get(id)
        return ResponseEntity.ok().body(CommentDto.Response(comment))
    }

    @GetMapping("/list/")
    fun getCommentList(@RequestParam("feedId") feedId: Long, pageable: Pageable): ResponseEntity<Page<CommentDto.Response>> {
        val commentList = commentService.getList(feedId, pageable)
        return ResponseEntity.ok().body(commentList.map { CommentDto.Response(it) })
    }

    @PutMapping("/")
    fun updateComment(@Valid @RequestBody updateRequest: CommentDto.UpdateRequest,
                      @RequestParam("id") id: Long): ResponseEntity<CommentDto.Response> {
        val comment = commentService.update(updateRequest, id)
        return ResponseEntity.ok().body(CommentDto.Response(comment))
    }

    @DeleteMapping("/")
    fun deleteComment(@RequestParam("id") id: Long) {
        commentService.delete(id)
    }
}
