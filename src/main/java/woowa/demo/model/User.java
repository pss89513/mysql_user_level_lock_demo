package woowa.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    public static final int MAXIMUM_CARD_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "user",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Card> cards = new HashSet<>();

    public User(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        if (isMaxCardCount()) {
            throw new IllegalStateException("최대 수량을 초과하였습니다. 최대수량 : "+ MAXIMUM_CARD_COUNT +", 현재 크기 : "+getCardCount());
        }
        card.setUser(this);
        this.cards.add(card);
    }

    private boolean isMaxCardCount() {
        return getCardCount() >= MAXIMUM_CARD_COUNT;
    }

    public int getCardCount() {
        return this.cards.size();
    }

}
