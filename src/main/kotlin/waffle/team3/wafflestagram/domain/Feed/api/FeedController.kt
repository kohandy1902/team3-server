package waffle.team3.wafflestagram.domain.Feed.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import waffle.team3.wafflestagram.domain.Feed.dto.FeedDto
import waffle.team3.wafflestagram.domain.Feed.service.FeedService
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.global.auth.CurrentUser
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/feed")
class FeedController(
    private val feedService: FeedService,
) {
    @PostMapping("/")
    fun upload(@Valid @RequestBody uploadRequest: FeedDto.UploadRequest, @CurrentUser user: User): ResponseEntity<FeedDto.Response> {
        val feed = feedService.upload(uploadRequest, user)
        return ResponseEntity
            .status(201)
            .body(FeedDto.Response(feed))
    }

    @PutMapping("/{feed_id}/")
    fun update(
        @PathVariable("feed_id") feedId: Long,
        @Valid @RequestBody updateRequest: FeedDto.UpdateRequest,
        @CurrentUser user: User
    ): ResponseEntity<FeedDto.Response> {
        val feed = feedService.update(feedId, updateRequest, user)
        return ResponseEntity
            .status(201)
            .body(FeedDto.Response(feed))
    }

    @GetMapping("/{feed_id}")
    fun getFeedById(@PathVariable("feed_id") feedId: Long): ResponseEntity<FeedDto.Response> {
        val feed = feedService.get(feedId)
        return ResponseEntity
            .status(200)
            .body(FeedDto.Response(feed))
    }

    @DeleteMapping("/{feed_id}/")
    fun delete(
        @PathVariable("feed_id") feedId: Long,
        @CurrentUser user: User
    ) {
        feedService.delete(feedId, user)
    }
}
