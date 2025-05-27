package stackup.stack_snapshot_back.config;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stackup.stack_snapshot_back.entity.Reward;
import stackup.stack_snapshot_back.entity.RewardStatus;
import stackup.stack_snapshot_back.repository.RewardRecordRepository;
import stackup.stack_snapshot_back.repository.RewardRepository;

@Configuration
@AllArgsConstructor
public class DataConfig {
    private final RewardRecordRepository rewardRecordRepository;
    private final RewardRepository rewardRepository;
    @Bean
    CommandLineRunner init() {
        return args -> {
            rewardRecordRepository.deleteAll();
            rewardRepository.deleteAll();

            Reward rewardCandy = Reward.builder()
                    .rewardType(RewardStatus.CANDY)
                    .maxCount(300)
                    .totalCount(0)
                    .remainingCount(300)
                    .probability(0.7)
                    .isAvailable(true)
                    .build();
            rewardRepository.save(rewardCandy);


            Reward rewardUSB = Reward.builder()
                    .rewardType(RewardStatus.USB)
                    .maxCount(20)
                    .totalCount(0)
                    .remainingCount(20)
                    .probability(0.4)
                    .isAvailable(true)
                    .build();
            rewardRepository.save(rewardUSB);


            Reward rewardGifticon10000 = Reward.builder()
                    .rewardType(RewardStatus.GIFTICON10000)
                    .maxCount(3)
                    .totalCount(0)
                    .remainingCount(3)
                    .probability(0.3)
                    .isAvailable(true)
                    .build();
            rewardRepository.save(rewardGifticon10000);


            Reward rewardGifticon30000 = Reward.builder()
                    .rewardType(RewardStatus.GIFTICON30000)
                    .maxCount(2)
                    .totalCount(0)
                    .remainingCount(2)
                    .probability(0.2)
                    .isAvailable(true)
                    .build();
            rewardRepository.save(rewardGifticon30000);


            Reward rewardGifticon50000 = Reward.builder()
                    .rewardType(RewardStatus.GIFTICON50000)
                    .maxCount(1)
                    .totalCount(0)
                    .remainingCount(1)
                    .probability(0.1)
                    .isAvailable(true)
                    .build();
            rewardRepository.save(rewardGifticon50000);
        };
    }
}
