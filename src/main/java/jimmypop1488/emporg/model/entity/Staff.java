package jimmypop1488.emporg.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@RequiredArgsConstructor
@Entity(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "staffid")
    public UUID staffid;

    @NonNull
    @Column(name = "staffname", nullable = false, unique = true)
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "stafforgid", nullable = false)
    public Organization stafforgid;

    @ManyToOne
    @JoinColumn(name = "headstaffid")
    private Staff headstaffid;
}
