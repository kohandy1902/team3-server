package waffle.team3.wafflestagram.global

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import waffle.team3.wafflestagram.domain.User.model.User
import waffle.team3.wafflestagram.domain.User.repository.UserRepository

@Component
class DataLoader (
    private val userRepository: UserRepository
        ): ApplicationRunner {
    // 어플리케이션 동작 시 실행
    override fun run(args: ApplicationArguments) {
        val newUser0 = User(email = "2harry@snu.ac.kr")
        val newUser1 = User(email = "970707zzang@naver.com")
        userRepository.save(newUser0)
        userRepository.save(newUser1)
    }
}