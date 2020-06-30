package jimmypop1488.emporg.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor
@Entity(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "orgid")
    public UUID orgid;

    @NonNull
    @Column(name = "orgname", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "headorgid")
    private Organization headorgid;
}
