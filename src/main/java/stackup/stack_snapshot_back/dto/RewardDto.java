package stackup.stack_snapshot_back.dto;

import lombok.Builder;
import stackup.stack_snapshot_back.entity.RewardStatus;

import java.time.Instant;

public final class RewardDto{
    private RewardDto(){};
    @Builder
    public record RewardResponse(
        Instant now,
        RewardStatus rewardStatus
    ){}
}
