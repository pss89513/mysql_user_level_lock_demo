package woowa.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import woowa.demo.model.Card;
import woowa.demo.model.User;
import woowa.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public int addNewCard(Long userId) {
        log.info("user 조회");
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디 입니다."));

        log.info("카드 추가");
        String serial = UUID.randomUUID().toString();
        user.addCard(new Card(serial));
        userRepository.saveAndFlush(user);

        log.info("추가 완료");
        int cardCount = user.getCardCount();

        log.info("cardCount : {}", cardCount);
        return cardCount;
    }

}
