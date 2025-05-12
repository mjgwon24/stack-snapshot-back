package stackup.stack_snapshot_back.restController;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import stackup.stack_snapshot_back.entity.RewardStatus;
import stackup.stack_snapshot_back.service.RewardService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reward")
@CrossOrigin(origins = "${server.cross-origin-url}")
public class RewardRestController {
    private final RewardService rewardService;
    @GetMapping
    public ResponseEntity<RewardStatus> requestReward() {
        return ResponseEntity.ok(rewardService.getReward());
    }
}
