package stackup.stack_snapshot_back.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import stackup.stack_snapshot_back.service.PicturePageService;

import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 사진 관리 API를 처리하는 컨트롤러
 * @since 2024.10.17
 * author 임석진
 */
@RestController
@RequestMapping("/api")
@Tag(name = "사진 관리 API", description = "사진을 업로드하고, 사진 목록을 조회하는 API")
public class PicturePageRestController {

    private final PicturePageService picturePageService;

    @Autowired
    public PicturePageRestController(PicturePageService picturePageService) {
        this.picturePageService = picturePageService;
    }

    /**
     * 사진을 업로드하는 엔드포인트
     *
     * @param imageFiles 업로드할 이미지 파일 리스트
     * @return 업로드 결과를 포함한 응답 (teamId 반환)
     */
    @PostMapping("/origin-upload")
    @Operation(summary = "사진 업로드", description = "사진을 업로드하는 API, 다중 파일 업로드 가능")
    public ResponseEntity<?> uploadPhotos(
            @RequestPart("image") List<MultipartFile> imageFiles) {

        if (imageFiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No images provided");
        }

        try {
            // teamId로 팀 식별자 생성 (team1, team2, ...)
            String teamId = picturePageService.generateNextTeamDirectoryName();

            // teamId로 파일을 업로드하고 파일명 리스트 반환
            List<String> fileNames = picturePageService.uploadFiles(imageFiles);

            // 응답 문자열을 구성하여 반환 (teamId와 파일명 리스트)
            String response = "Images uploaded successfully. Team ID: " + teamId + ", Files: " + String.join(", ", fileNames);

            return ResponseEntity.ok(response);  // 응답 문자열로 반환
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save image: " + e.getMessage());
        }
    }

    /**
     * 특정 팀의 업로드된 사진 목록을 반환하는 엔드포인트
     *
     * @param teamId 조회할 팀의 고유 ID (team1, team2, ...)
     * @return 특정 팀의 업로드된 사진의 URL 목록
     */
    @GetMapping("/photos")
    @Operation(summary = "업로드된 사진 목록 조회", description = "특정 팀의 업로드된 사진의 URL 목록을 반환하는 API")
    public ResponseEntity<List<String>> getUploadedPhotos(
            @RequestParam("teamId") String teamId) {  // teamId를 파라미터로 받아서 특정 팀의 사진 조회

        // 팀 디렉토리 존재 여부 확인
        if (!picturePageService.doesTeamDirectoryExist(teamId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 팀 디렉토리가 존재하지 않을 경우
        }

        List<String> fileUrls = picturePageService.getUploadedPhotos(teamId);
        if (fileUrls.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(fileUrls);
        }
        return ResponseEntity.ok().body(fileUrls);
    }
}
