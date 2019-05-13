package woowa.demo.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import woowa.demo.model.Card
import woowa.demo.model.User

@SpringBootTest
class UserRepositoryTest extends Specification {

    @Autowired
    UserRepository userRepository

    def "save"() {
        given:
        User user = new User("name!!")

        when:
        User save = userRepository.saveAndFlush(user)

        then:
        User get = userRepository.findById(save.getId()).get()

        save.id == get.id
    }

    def "save and add card"() {
        given:
        User user = new User("name!!")
        Card card = new Card("cardName!!")

        when:
        user.addCard(card)
        User save = userRepository.saveAndFlush(user)

        then:
        User get = userRepository.findById(save.getId()).get()

        save.id == get.id
    }

}
