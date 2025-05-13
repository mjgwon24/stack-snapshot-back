package stackup.stack_snapshot_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stackup.stack_snapshot_back.entity.Reward;
import stackup.stack_snapshot_back.entity.RewardRecord;

public interface RewardRecordRepository extends JpaRepository<RewardRecord,Long> {
}
