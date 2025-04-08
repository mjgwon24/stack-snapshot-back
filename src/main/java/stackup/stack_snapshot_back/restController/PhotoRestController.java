package stackup.stack_snapshot_back.restController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.dto.SelectFrameRequestDTO;
import stackup.stack_snapshot_back.dto.SelectFrameResponseData;
import stackup.stack_snapshot_back.service.PhotoService;

import java.io.IOException;
import java.util.List;

/**
 * 사진 처리 API 공통 컨트롤러
 * @since 2024.11.03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "${server.cross-origin-url}")
@PropertySource("classpath:application.yml")
public class PhotoRestController {

    private final PhotoService photoService;
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Value("${server.cross-origin-url}")
    private String crossOriginUrl;

    String UPLOAD_PATH, OUTPUT_PATH, FRAME_PATH;

    @PostConstruct
    public void initPaths() {
        UPLOAD_PATH = uploadDirectory + "/original-photo/";
        OUTPUT_PATH = uploadDirectory + "/final-photo/";
        FRAME_PATH = uploadDirectory + "/frames/";
    }

    /**
     * 원본 사진 업로드 API
     * @param images 업로드할 사진 리스트
     * @return 업로드된 사진 URL 리스트
     */
    @PostMapping("/origin-upload")
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> fileUrls = photoService.uploadPhotos(images);
            return ResponseEntity.ok(fileUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 업로드된 전체 사진 리스트 조회 API
     * @return 업로드된 전체 사진 URL 리스트
     */
    @GetMapping("/photos")
    public ResponseEntity<List<String>> getUploadedPhotos() {
        List<String> fileUrls = photoService.getUploadedPhotos();
        if (fileUrls.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileUrls);
    }

    /**
     * 업로드된 사진 다운로드 API
     * @param fileName 다운로드할 사진 파일 이름
     * @return 다운로드할 사진 파일 리소스
     */
    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            return photoService.serveFile(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 최종 합성 이미지 업로드 API
     * @param requestDto
     * @return
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @Tag(name = "Image Upload API", description = "찍은 이미지 업로드, GroupID값, FrameID 받아옴, 이미지 경로 리스트, 최종 이미지 경로, 그룹ID 반환")
    @PostMapping("/upload")
    public ResponseEntity<SelectFrameResponseData> uploadFile(@ModelAttribute SelectFrameRequestDTO requestDto) throws IllegalArgumentException, IOException {
        SelectFrameResponseData response = photoService.uploadFile(requestDto, UPLOAD_PATH, FRAME_PATH, OUTPUT_PATH);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 원본 사진 조회 API
     * @param date 촬영 날짜
     * @param groupid 그룹 ID
     * @return 사진 파일 리소스
     * @throws IOException 입출력 예외
     */
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @GetMapping("/file")
    public ResponseEntity<Resource> getFile(@RequestParam String groupid, @RequestParam String date, @RequestParam String index) throws IOException {
        try {
            return photoService.getFile(groupid, date, index, UPLOAD_PATH);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 최종 합성 이미지 조회 API
     * @param date
     * @param groupid
     * @return
     * @throws IOException
     */
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @GetMapping("/final_file")
    public ResponseEntity<Resource> getFinalFile(@RequestParam String date, @RequestParam String groupid) throws IOException {
        try {
            return photoService.getFinalFile(date, groupid, OUTPUT_PATH);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}