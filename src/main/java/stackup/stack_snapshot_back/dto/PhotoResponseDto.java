package stackup.stack_snapshot_back.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PhotoResponseDto {
    private final Integer groupId;
    private final String date;
    private final String timeStamp;
    private final String fileName;
}