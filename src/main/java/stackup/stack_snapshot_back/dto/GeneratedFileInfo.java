package stackup.stack_snapshot_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GeneratedFileInfo {
    private final String date;
    private final String timeStamp;
    private final Integer index;
    private final String fileName;
}
