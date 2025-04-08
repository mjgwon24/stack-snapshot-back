package stackup.stack_snapshot_back.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stackup.stack_snapshot_back.service.QRService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * QR 코드 및 사진 다운로드 API 컨트롤러
 * @since 2024.10.27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qrs")
@CrossOrigin(origins = "${server.cross-origin-url}")
@Tag(name = "QR 코드 API", description = "QR 코드 생성 및 사진 다운로드 API")
public class QRRestController {

    private final QRService qrService;

    /**
     * QR 코드 생성 API
     * @param groupId 그룹 ID
     * @param date 날짜
     * @return QR 코드 이미지 데이터
     */
    @PostMapping
    public ResponseEntity<byte[]> createQrCode(@RequestParam String groupId, @RequestParam String date) {
        try {
            byte[] qrCodeImage = qrService.generateQrCode(groupId, date);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"qrcode.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeImage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 사진 다운로드 API
     * @param groupId 그룹 ID
     * @param fileName 다운로드할 사진 파일 이름
     * @return 사진 파일 리소스
     */
    @Operation(summary = "사진 다운로드", description = "그룹별 사진 파일을 다운로드합니다.")
    @GetMapping("/{groupId}/photos/{fileName}")
    public ResponseEntity<FileSystemResource> downloadPhotoByQrCode(@PathVariable("groupId") String groupId, @PathVariable("fileName") String fileName) {
        try {
            FileSystemResource fileResource = qrService.downloadPhoto(groupId, fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName)
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}