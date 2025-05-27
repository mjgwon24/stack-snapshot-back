package stackup.stack_snapshot_back.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectFrameRequestDto {
    private Integer selectedFrameId;
    private Integer groupId;
    private List<String> selectPhotoNames;
}