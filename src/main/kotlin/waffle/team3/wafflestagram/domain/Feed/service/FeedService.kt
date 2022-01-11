package waffle.team3.wafflestagram.domain.Feed.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import waffle.team3.wafflestagram.domain.Feed.dto.FeedDto
import waffle.team3.wafflestagram.domain.Feed.exception.FeedDoesNotExistException
import waffle.team3.wafflestagram.domain.Feed.exception.FeedNotAllowedException
import waffle.team3.wafflestagram.domain.Feed.model.Feed
import waffle.team3.wafflestagram.domain.Feed.repository.FeedRepository
import waffle.team3.wafflestagram.domain.Like.model.Like
import waffle.team3.wafflestagram.domain.Like.repository.LikeRepository
import waffle.team3.wafflestagram.domain.Photo.model.Photo
import waffle.team3.wafflestagram.domain.Tag.model.Tag
import waffle.team3.wafflestagram.domain.Tag.repository.TagRepository
import waffle.team3.wafflestagram.domain.User.exception.FollowingUserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserDoesNotExistException
import waffle.team3.wafflestagram.domain.User.exception.UserException
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository
import waffle.team3.wafflestagram.domain.UserTag.model.UserTag
import waffle.team3.wafflestagram.domain.UserTag.repository.UserTagRepository
import waffle.team3.wafflestagram.global.s3.controller.S3Controller
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class FeedService(
    private val feedRepository: FeedRepository,
    private val userRepository: UserRepository,
    private val userTagRepository: UserTagRepository,
    private val tagRepository: TagRepository,
    private val likeRepository: LikeRepository,
    private val s3Controller: S3Controller,
) {

    @Transactional
    // 사람 태그하기, 위치 추가 기능
    fun upload(uploadRequest: FeedDto.UploadRequest, user: User): Feed {
        val feed = Feed(content = uploadRequest.content, user = user)

        val photoList = mutableListOf<Photo>()
        for (key in uploadRequest.imageKeys) {
            val photo = Photo(key, "https://waffle-team3-bucket.s3.ap-northeast-2.amazonaws.com/$key", feed)
            photoList.add(photo)
        }

        val userTagList = mutableListOf<UserTag>()
        for (nickname in uploadRequest.userTags) {
            val findUser = userRepository.findByNickname(nickname)
                ?: throw UserDoesNotExistException("User with this nickname does not exist.")
            if (findUser.id == user.id) throw UserException("You cannot tag yourself.")
            val userTag = UserTag(user = findUser, feed = feed)
            userTagList.add(userTag)
        }

        val tagList = mutableListOf<Tag>()
        for (tag in uploadRequest.tags) {
            val newTag = Tag(content = tag, feed = feed)
            tagList.add(newTag)
        }

        feed.tags = tagList
        feed.userTags = userTagList
        feed.photos = photoList

        return feedRepository.save(feed)
    }

    @Transactional
    fun update(id: Long, updateRequest: FeedDto.UpdateRequest, user: User): Feed {
        val feed = feedRepository.findByIdOrNull(id)
            ?: throw FeedDoesNotExistException("Feed with this ID does not exist.")

        if (feed.user.id != user.id) throw FeedNotAllowedException("You are not allowed to update this feed.")

        val photoList = mutableListOf<Photo>()
        for (key in updateRequest.imageKeys) {
            val photo = Photo(key, "https://waffle-team3-bucket.s3.ap-northeast-2.amazonaws.com/$key", feed)
            photoList.add(photo)
        }

        val userTagList = mutableListOf<UserTag>()
        for (nickname in updateRequest.userTags) {
            val findUser = userRepository.findByNickname(nickname)
                ?: throw UserDoesNotExistException("User with this nickname does not exist.")
            if (findUser.id == user.id) throw UserException("You cannot tag yourself.")
            val userTag = UserTag(user = findUser, feed = feed)
            userTagList.add(userTag)
        }

        //  string tag feature
        val tagList = mutableListOf<Tag>()
        for (tag in updateRequest.tags) {
            val newTag = Tag(content = tag, feed = feed)
            tagList.add(newTag)
        }

        for (userTag in feed.userTags) {
            userTagRepository.delete(userTag)
        }
        for (tag in feed.tags) {
            tagRepository.delete(tag)
        }

        feed.apply {
            content = updateRequest.content
            tags = tagList
            userTags = userTagList
            updatedAt = LocalDateTime.now()
            photos = photoList
        }

        feedRepository.save(feed)

        return feed
    }

    fun get(id: Long, user: User): Feed {
        val feed = feedRepository.findByIdOrNull(id)
            ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
        val currUser = userRepository.findByIdOrNull(user.id)

        if (!feed.user.public && !feed.user.follower.any { it.user.id == currUser!!.id })
            throw FollowingUserDoesNotExistException("You are not follower of this user.")

        return feed
    }

    fun getPage(offset: Int, number: Int, user: User): Page<Feed> {
        val feeds = mutableListOf<Feed>()
        val currUser = userRepository.findByIdOrNull(user.id) ?: throw UserDoesNotExistException("User with this ID does not exist.")
        for (followingUser in currUser.following) {
            for (f in followingUser.user.feeds) {
                feeds.add(f)
            }
        }
        val sortedFeeds = feeds.sortedBy { it.updatedAt }.reversed()

        val pageable = PageRequest.of(offset, number)
        val start = pageable.offset.toInt()
        val end = (start + pageable.pageSize).coerceAtMost(sortedFeeds.size)
        if (start > sortedFeeds.size) return PageImpl(listOf(), pageable, sortedFeeds.size.toLong())

        return PageImpl(sortedFeeds.subList(start, end), pageable, sortedFeeds.size.toLong())
    }

    fun getSelfFeeds(offset: Int, number: Int, user: User): Page<Feed> {
        val currUser = userRepository.findByIdOrNull(user.id) ?: throw UserDoesNotExistException("User with this ID does not exist.")
        return feedRepository.findByUserOrderByUpdatedAtDesc(PageRequest.of(offset, number), currUser)
    }

    fun getOtherUserFeeds(offset: Int, number: Int, user: User, id: Long): Page<Feed> {
        val currUser = userRepository.findByIdOrNull(user.id) ?: throw UserDoesNotExistException("User with this ID does not exist.")
        val otherUser = userRepository.findByIdOrNull(id) ?: throw UserDoesNotExistException("User with this ID does not exist.")

        if (!otherUser.public && !otherUser.follower.any { it.user.id == currUser.id })
            throw FollowingUserDoesNotExistException("You are not follower of this user.")

        return feedRepository.findByUserOrderByUpdatedAtDesc(PageRequest.of(offset, number), otherUser)
    }

    @Transactional
    fun delete(id: Long, user: User) {
        val feed = feedRepository.findByIdOrNull(id)
            ?: throw FeedDoesNotExistException("Feed with this key does not exist.")

        if (feed.user.id != user.id) throw FeedNotAllowedException("You are not allowed to update this feed.")

        for (photo in feed.photos) {
            s3Controller.deletePhoto(photo.key)
        }

        feedRepository.delete(feed)
    }

    @Transactional
    fun addLike(id: Long, user: User): Feed {
        val feed = feedRepository.findByIdOrNull(id)
            ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
        val currUser = userRepository.findByIdOrNull(user.id)

        if (!feed.user.public && !feed.user.follower.any { it.user.id == currUser!!.id })
            throw FollowingUserDoesNotExistException("You are not follower of this user.")

        if (!feed.likes.any { it.user.id == user.id }) {
            val like = Like(feed, user)
            feed.likes.add(like)
        }

        return feed
    }

    @Transactional
    fun deleteLike(id: Long, user: User): Feed {
        val feed = feedRepository.findByIdOrNull(id)
            ?: throw FeedDoesNotExistException("Feed with this key does not exist.")
        val currUser = userRepository.findByIdOrNull(user.id)

        if (!feed.user.public && !feed.user.follower.any { it.user.id == currUser!!.id })
            throw FollowingUserDoesNotExistException("You are not follower of this user.")

        val likes = feed.likes
        val deletedLike = likes.find { it.user.id == user.id }
            ?: throw UserDoesNotExistException("You did not add like to this feed.")

        likes.remove(deletedLike)
        likeRepository.delete(deletedLike)

        feed.likes = likes

        return feed
    }
}
