package waffle.team3.wafflestagram.global

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import waffle.team3.wafflestagram.domain.Comment.model.Comment
import waffle.team3.wafflestagram.domain.Comment.repository.CommentRepository
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.Reply.model.Reply
import waffle.team3.wafflestagram.domain.Reply.repository.ReplyRepository
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository

@Component
class DataLoader(
    private val userRepository: UserRepository,
    private val feedRepository: FeedRepository,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
) : ApplicationRunner {
    // 어플리케이션 동작 시 실행
    override fun run(args: ApplicationArguments) {
        val newUser0 = User(email = "2harry@snu.ac.kr", password = "1234")
        val newUser1 = User(email = "970707zzang@naver.com", password = "1234")
        userRepository.save(newUser0)
        userRepository.save(newUser1)
        val newFeed = feedRepository.save(Feed())

        for (i in 0 until 10) {
            val comment = commentRepository.save(
                Comment(
                    writer = "lcm_harry",
                    text = "comment_test $i",
                    feed = newFeed,
                )
            )
            for (j in 0 until 10) {
                replyRepository.save(
                    Reply(
                        writer = "counterPart",
                        text = "reply_test ${10 * i + j}",
                        comment = comment
                    )
                )
            }
        }
    }
}
