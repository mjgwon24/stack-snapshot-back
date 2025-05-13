package stackup.stack_snapshot_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stackup.stack_snapshot_back.entity.Reward;
import stackup.stack_snapshot_back.entity.RewardStatus;

import java.util.Optional;

public interface RewardRepository extends JpaRepository<Reward,Long> {
    Reward findByRewardType(RewardStatus rewardType);
    Optional<Reward> findByRewardTypeAndIsAvailable(RewardStatus rewardType, Boolean isAvailable);
}
