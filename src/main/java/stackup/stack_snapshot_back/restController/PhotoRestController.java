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
@RequestMapping("/api/photos")
@CrossOrigin(origins = "${server.cross-origin-url}")
@PropertySource("classpath:application.yml")
@Tag(name = "Photo API", description = "사진 처리 API")
public class PhotoRestController {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final PhotoService photoService;
    String UPLOAD_PATH, OUTPUT_PATH, FRAME_PATH;

    @PostConstruct
    public void initPaths() {
        UPLOAD_PATH = uploadDirectory + "/original-photo/";
        OUTPUT_PATH = uploadDirectory + "/final-photo/";
        FRAME_PATH = uploadDirectory + "/frames/";
    }

    /**
     * 사진 업로드 API
     * @param images 업로드 할 사진 리스트
     * @return 업로드 된 사진 URL 리스트
     */
    @PostMapping
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam("images") List<MultipartFile> images) {
        try {
            List<String> fileUrls = photoService.uploadPhotos(images);
            return ResponseEntity.ok(fileUrls);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 업로드 사진 목록 조회 API
     * @return 업로드 된 사진 URL 리스트
     */
    @GetMapping
    public ResponseEntity<List<String>> getAllPhotos() {
        List<String> fileUrls = photoService.getUploadedPhotos();
        if (fileUrls.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(fileUrls);
    }

    /**
     * 개별 사진 다운로드 API
     * @param fileName 다운로드 할 사진 파일 이름
     * @return 사진 파일 리소스
     */
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String fileName) {
        try {
            return photoService.serveFile(fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 프레임 선택 및 사진 업로드 API
     * @param requestDto 업로드 요청 데이터 (selectedFrameID, groupID, List file)
     * @return groupId, outputPath
     * @throws IllegalArgumentException
     * @throws IOException
     */
    @PostMapping("/frames")
    public ResponseEntity<SelectFrameResponseData> uploadWithFrame(@ModelAttribute SelectFrameRequestDTO requestDto) throws IllegalArgumentException, IOException {
        SelectFrameResponseData response = photoService.uploadFile(requestDto, UPLOAD_PATH, FRAME_PATH, OUTPUT_PATH);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 특정 날짜 및 그룹 사진 조회 API
     * @param date 촬영 날짜
     * @param groupId 그룹 ID
     * @return 사진 파일 리소스
     * @throws IOException
     */
    @GetMapping("/groups/{groupId}/dates/{date}/photos/{index}")
    public ResponseEntity<Resource> getPhotoByGroupAndDate(@PathVariable("groupId") String groupId, @PathVariable("date") String date, @PathVariable("index") String index) throws IOException {
        try {
            return photoService.getFile(groupId, date, index, UPLOAD_PATH);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * 특정 날짜 및 그룹의 최종 합성 이미지 조회 API
     * @param groupId 그룹 ID
     * @param date 촬영 날짜
     * @return 최종 합성 이미지 파일 리소스
     * @throws IOException
     */
    @GetMapping("/groups/{groupId}/dates/{date}/final")
    public ResponseEntity<Resource> getFinalPhoto(@PathVariable("groupId") String groupId, @PathVariable("date") String date) throws IOException {
        try {
            return photoService.getFinalFile(date, groupId, OUTPUT_PATH);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}