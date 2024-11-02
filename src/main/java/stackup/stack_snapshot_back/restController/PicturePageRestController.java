package stackup.stack_snapshot_back.restController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.service.PicturePageService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;


/**
 * 사진 관리 API를 처리하는 컨트롤러
 * @since 2024.10.17
 * author 임석진
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PicturePageRestController {


    private static final Logger logger = LoggerFactory.getLogger(PicturePageRestController.class);

    private final PicturePageService picturePageService;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    public PicturePageRestController(PicturePageService picturePageService) {
        this.picturePageService = picturePageService;
    }

    @PostMapping("/origin-upload")
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam("images") List<MultipartFile> images) {
        logger.info("사진 업로드 요청 수신 - 이미지 수: {}", images.size());
        List<String> fileUrls = new ArrayList<>(); // URL 리스트 생성

        try {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                logger.info("이미지 업로드 시작 - 파일 이름: {}, 인덱스: {}", image.getOriginalFilename(), i + 1);

                // 파일 업로드 및 저장된 파일 이름 가져오기
                String fileName = picturePageService.uploadFile(image, i + 1);
                String fileUrl = "/stack-photo/" + fileName; // URL 생성
                fileUrls.add(fileUrl); // URL 리스트에 추가

                logger.info("이미지 업로드 성공 - 파일 이름: {}, 인덱스: {}", image.getOriginalFilename(), i + 1);
            }
            logger.info("모든 이미지 업로드 완료");
            return ResponseEntity.ok(fileUrls); // URL 리스트 반환
        } catch (IOException e) {
            logger.error("이미지 저장 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/photos")
    public ResponseEntity<List<String>> getUploadedPhotos() {
        logger.info("사진 목록 조회 요청 수신");
        List<String> fileUrls = picturePageService.getUploadedPhotos();
        if (fileUrls.isEmpty()) {
            logger.warn("사진 목록이 비어 있음");
            return ResponseEntity.notFound().build();
        }
        logger.info("사진 목록 조회 성공 - 사진 수: {}", fileUrls.size());
        return ResponseEntity.ok(fileUrls);
    }

    @GetMapping("/files/{fileName:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                logger.error("파일을 찾을 수 없거나 읽을 수 없습니다: " + fileName);
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            logger.error("파일 URL이 잘못되었습니다: " + fileName, e);
            return ResponseEntity.badRequest().build();
        }
    }
}
