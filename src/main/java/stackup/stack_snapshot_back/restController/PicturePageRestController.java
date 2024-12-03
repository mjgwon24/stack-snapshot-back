package stackup.stack_snapshot_back.restController;

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
 * 사진 관리 API 처리 컨트롤러
 * @since 2024.10.17
 * @author 임석진
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${server.cross-origin-url}")
public class PicturePageRestController {

    private final PicturePageService picturePageService;

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    public PicturePageRestController(PicturePageService picturePageService) {
        this.picturePageService = picturePageService;
    }

    @PostMapping("/origin-upload")
    public ResponseEntity<List<String>> uploadPhotos(@RequestParam("images") List<MultipartFile> images) {

        List<String> fileUrls = new ArrayList<>(); // URL 리스트 생성

        try {
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);


                String fileName = picturePageService.uploadFile(image, i + 1,"1");
                String fileUrl = "/original-photo/" + fileName;
                fileUrls.add(fileUrl);

            }
            return ResponseEntity.ok(fileUrls); // URL 리스트 반환
        } catch (IOException e) {

            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/photos")
    public ResponseEntity<List<String>> getUploadedPhotos() {

        List<String> fileUrls = picturePageService.getUploadedPhotos();
        if (fileUrls.isEmpty()) {

            return ResponseEntity.notFound().build();
        }

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

                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {

            return ResponseEntity.badRequest().build();
        }
    }
}
