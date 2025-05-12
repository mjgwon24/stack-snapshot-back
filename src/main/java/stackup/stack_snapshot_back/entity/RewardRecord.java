package stackup.stack_snapshot_back.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RewardRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_record_id",unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @Column(nullable = false)
    private Instant drawAt;
}
