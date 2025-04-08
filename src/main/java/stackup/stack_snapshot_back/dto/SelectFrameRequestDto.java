package stackup.stack_snapshot_back.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class SelectFrameRequestDto {
    private int SelectedFrameID;
    private String GroupID;
    private List<MultipartFile> file;

    public SelectFrameRequestDto(Integer SelectedFrameID, String GroupID, List<MultipartFile> file) {
        if (SelectedFrameID == null || GroupID == null || file == null) {
            throw new IllegalArgumentException("SelectedFrameID==null or file==null or GroupID==null");
        }
        this.SelectedFrameID = SelectedFrameID;
        this.GroupID = GroupID;
        this.file = file;
    }
}