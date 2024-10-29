package stackup.stack_snapshot_back.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import stackup.stack_snapshot_back.service.PicturePageService;

import java.io.IOException;
import java.util.List;

/**
 * 사진 관리 API를 처리하는 컨트롤러
 * @since 2024.10.17
 * author 임석진
 */
@RestController
@RequestMapping("/api")
@Tag(name = "사진 관리 API", description = "사진을 경로에 저장하고, 사진 목록을 조회하는 API")
public class PicturePageRestController {

    private final PicturePageService picturePageService;

    @Autowired
    public PicturePageRestController(PicturePageService picturePageService) {
        this.picturePageService = picturePageService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/origin-upload")
    @Operation(summary = "사진 업로드", description = "여러 사진 파일을 업로드하는 API입니다.")
    public ResponseEntity<?> uploadPhotos(
            @Parameter(description = "업로드할 이미지 파일 리스트", required = true)
            @RequestParam("images") List<MultipartFile> images) {
        try {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                picturePageService.uploadFile(image, i + 1);
            }
            return ResponseEntity.ok("이미지 정상 업로드");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save images: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/photos")
    @Operation(summary = "업로드된 사진 목록 조회", description = "서버에 업로드된 모든 사진의 URL 목록을 반환합니다.")
    public ResponseEntity<List<String>> getUploadedPhotos() {
        List<String> fileUrls = picturePageService.getUploadedPhotos();

        if (fileUrls.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fileUrls);
        }
        return ResponseEntity.ok().body(fileUrls);
    }
}
