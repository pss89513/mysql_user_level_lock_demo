package woowa.demo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@ToString
@Table(name = "card")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "serial", nullable = false)
    private String serial;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Card(String serial) {
        this.serial = serial;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
