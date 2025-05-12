package stackup.stack_snapshot_back.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id",unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RewardStatus rewardType;

    @Column(nullable = false)
    private Integer maxCount;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer remainingCount;

    @DecimalMax("1.0")
    @DecimalMin("0.0")
    @Column(nullable = false)
    private Double probability;

    @Column(nullable = false)
    private Boolean isAvailable;

    public void choice(){
        this.remainingCount -= 1;
        this.totalCount += 1;
    }

    public void setAvailable(Boolean available){
        this.isAvailable = available;
    }
}
