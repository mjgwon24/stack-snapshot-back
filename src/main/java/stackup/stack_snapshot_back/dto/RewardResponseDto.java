package stackup.stack_snapshot_back.dto;

import lombok.Builder;
import lombok.Getter;
import stackup.stack_snapshot_back.entity.RewardStatus;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class RewardResponseDto {
    private Instant now;
    private RewardStatus rewardStatus;
}
