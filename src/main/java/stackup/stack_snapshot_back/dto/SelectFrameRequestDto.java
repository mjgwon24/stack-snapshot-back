package stackup.stack_snapshot_back.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SelectFrameRequestDto {
    private final Integer selectedFrameId;
    private final Integer groupId;
    private final List<String> selectPhotoNames;
}