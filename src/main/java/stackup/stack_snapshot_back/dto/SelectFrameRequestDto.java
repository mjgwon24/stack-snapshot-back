package stackup.stack_snapshot_back.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Builder
public class SelectFrameRequestDto {
    private final Integer selectedFrameId;
    private final Integer groupId;
    private final List<MultipartFile> file;
}