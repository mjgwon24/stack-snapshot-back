package stackup.stack_snapshot_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stackup.stack_snapshot_back.dto.RewardDto.RewardResponse;
import stackup.stack_snapshot_back.entity.Reward;
import stackup.stack_snapshot_back.entity.RewardRecord;
import stackup.stack_snapshot_back.entity.RewardStatus;
import stackup.stack_snapshot_back.repository.RewardRecordRepository;
import stackup.stack_snapshot_back.repository.RewardRepository;

import java.time.Instant;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class RewardService {
    private final RewardRecordRepository rewardRecordRepository;
    private final RewardRepository rewardRepository;
    @Transactional
    public RewardResponse getReward(){

        RewardStatus[] rewards = RewardStatus.values();
        boolean canGetReward = false;
        //모든 상품의 남은 양이 0인지 확인
        for(RewardStatus rewardStatus : rewards){
            if(rewardStatus.equals(RewardStatus.FAIL)){
                continue;
            }
            // 상품을 뽑을 수 있는지 확인
            Reward reward = rewardRepository.findByRewardTypeAndIsAvailable(rewardStatus,true).orElse(null);
            if(reward==null){
                continue;
            }
            if(reward.getRemainingCount()!=0){
                canGetReward = true;
            }
        }
        //고를 수 있는 상품이 있으면
        if(canGetReward){
            Random random = new Random();
            RewardStatus rewardStatus = rewards[random.nextInt(rewards.length)];

            if(rewardStatus.equals(RewardStatus.FAIL)){
                return RewardResponse.builder().now(Instant.now()).rewardStatus(RewardStatus.FAIL).build();
            }

            Reward reward = rewardRepository.findByRewardType(rewardStatus);
            if(reward==null){
                return getReward();
            }

            // 확률 적용
            Integer randomInteger = random.nextInt(0,100);

            // 확률 검증(0~100까지 랜덤값이 확률 보다 크면 실패, ex: 5% 확률이면 0~4까지 100가지 중에 5가지 경우만 당첨)
            if(reward.getProbability()*100<randomInteger){
                return RewardResponse.builder().now(Instant.now()).rewardStatus(RewardStatus.FAIL).build();
            }

            // 당첨되었다면 해당하는 경품의 개수가 남아있는지 확인
            if(reward.getRemainingCount()!=0){
                reward.choice();//choice를 하면 totalCount는 증가, remainingCount는 감소함

                // 당첨 정보를 저장함
                Instant now = Instant.now();
                RewardRecord rewardRecord = RewardRecord.builder().reward(reward).drawAt(now).build();
                rewardRecordRepository.save(rewardRecord);

                // 당첨된 품복을 리턴
                return RewardResponse.builder().now(Instant.now()).rewardStatus(reward.getRewardType()).build();
            }
            else{

                reward.setAvailable(false);
                rewardRepository.save(reward);
                //남은 상품 수가 없으면 다시 뽑기
                return getReward();
            }
        }
        //고를 수 있는 상품이 없으면 바로 실패 반환
        return RewardResponse.builder().now(Instant.now()).rewardStatus(RewardStatus.FAIL).build();
    }
}
