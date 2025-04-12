package stackup.stack_snapshot_back.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPhotosResponseDto {
    private Integer groupId;
    private String date;
    private String timeStamp;
    private List<String> fileNames;
}
