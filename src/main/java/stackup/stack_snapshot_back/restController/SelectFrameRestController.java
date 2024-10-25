package stackup.stack_snapshot_back.restController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.service.SelectFrameService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 완성 사진 생성 서비스
 * 4개의 이미지를 받아 하나의 이미지로 합쳐주는 서비스
 * @since 2024-10-22
 * @author 김이현
 */

@RestController
@RequestMapping("/api")
@PropertySource("classpath:application.yml") //application.yml에 들어있는 데이터 사용, Service에서는 사용 못하기 때문에 넘겨줘야함
public class SelectFrameRestController {
    @Value("${file.upload-dir}")
    String STATIC_DIR;
    String UPLOAD_PATH, OUTPUT_PATH, FRAME_PATH;


    // 프로그램이 실행되면 변수 초기화
    @PostConstruct
    public void initPaths() {
        UPLOAD_PATH = STATIC_DIR + "/original-photo/";
        OUTPUT_PATH = STATIC_DIR + "/final-photo/";
        FRAME_PATH = STATIC_DIR + "/frames/";
    }
    /**
     * DTO
     */
    @Data//자동으로 Getter, Setter, toString(), equals(), hashCode() 메서드 생성
    public static class SelectFrameRequestDTO {
        private int SelectedFrameID;
        private String GroupID;
        private List<MultipartFile> file;

        public SelectFrameRequestDTO(Integer SelectedFrameID,String GroupID,List<MultipartFile> file) {
            if(SelectedFrameID==null||GroupID==null||file==null) {
                throw new IllegalArgumentException("SelectedFrameID==null or file==null or GroupID==null");
            }
            this.SelectedFrameID = SelectedFrameID;
            this.GroupID = GroupID;
            this.file = file;
        }
    }

    @Data
    public static class FileUploadDTO {
        private List<MultipartFile> file;
        private String GroupID;
    }

    //JSON 객체를 반환하기 위한 클래스
    @Data
    private static class SelectFrameResponseData {
        String GroupID;
        List<String> FileNames = new ArrayList<>();
        String OutputPath;
        public SelectFrameResponseData() {}
    }


    /*
     * API
     */

    /**
     *
     * @param requestDto
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @Tag(name="Image Upload API",description = "찍은 이미지 업로드, GroupID값, FrameID 받아옴, 이미지 경로 리스트, 최종 이미지 경로, 그룹ID 반환")
    @PostMapping("/upload")
    public ResponseEntity<SelectFrameResponseData> uploadFile(@ModelAttribute SelectFrameRequestDTO requestDto) throws IllegalArgumentException,IOException {
        // DTO에서 값 가져오기
        List<MultipartFile> files = requestDto.getFile();
        String GroupID = requestDto.getGroupID();
        int FrameID = requestDto.getSelectedFrameID();

        // 파일 저장 경로 지정
        SelectFrameRestController.SelectFrameResponseData Response = new SelectFrameRestController.SelectFrameResponseData();

        Response.GroupID = GroupID;

        SelectFrameService selectFrameService = new SelectFrameService();
        List<String> FileNames = selectFrameService.FileUpload(UPLOAD_PATH,GroupID,files);

        String combinedImage_path = selectFrameService.mergeImages(FileNames,GroupID,FrameID,UPLOAD_PATH,FRAME_PATH,OUTPUT_PATH);
        Response.OutputPath = combinedImage_path;
        Response.FileNames = FileNames;
        return new ResponseEntity<>(Response, HttpStatus.OK);
    }
}

