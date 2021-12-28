package waffle.team3.wafflestagram.domain.Feed.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
    @PostMapping("")
    fun upload(@Valid @RequestBody uploadRequest: FeedDto.UploadRequest, @CurrentUser user: User): ResponseEntity<FeedDto.Response> {
        val feed = feedService.upload(uploadRequest, user)
        return ResponseEntity
            .status(201)
            .body(FeedDto.Response(feed))
    }

    @PostMapping("/{feed_id}")
    fun update(@PathVariable("feed_id") feedId: Long,
               @Valid @RequestBody updateRequest: FeedDto.UpdateRequest,
               @CurrentUser user: User): ResponseEntity<FeedDto.Response> {
        val feed = feedService.update(feedId, updateRequest, user)
        return ResponseEntity
            .status(201)
            .body(FeedDto.Response(feed))
    }

    @DeleteMapping("/{feed_id}")
    fun delete(@PathVariable("feed_id") feedId: Long,
               @CurrentUser user: User): FeedDto.Response {
        val feed = feedService.delete(feedId, user)
        return FeedDto.Response(feed)
    }

}