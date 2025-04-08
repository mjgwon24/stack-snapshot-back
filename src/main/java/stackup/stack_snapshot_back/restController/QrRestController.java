package stackup.stack_snapshot_back.restController;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stackup.stack_snapshot_back.service.CreateQrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * QR 코드 생성 및 사진 다운로드 API 처리 컨트롤러
 * @since 2024.10.27
 * @author 이수헌
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "QR 코드 API", description = "QR 코드 생성 및 사진 다운로드 API")
public class QrRestController {

    private final CreateQrService createQrService;

    /**
     * QR 코드 생성 API
     * @param groupId QR 코드 생성 요청 데이터 (그룹 ID 포함)
     * @return QR 코드 이미지 데이터 응답
     */
    @Operation(summary = "QR 코드 생성", description = "최신 사진 파일을 다운로드할 수 있는 QR 코드를 생성하여 이미지를 반환합니다.")
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @PostMapping("/create-qr")
    public ResponseEntity<byte[]> createQrCode(@RequestParam String groupid,@RequestParam String date) {
        try {
            byte[] qrCodeImage = createQrService.generateQrCode(groupid,date);
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
    @Operation(summary = "사진 다운로드", description = "시스템 경로에 있는 그룹별 사진 파일을 다운로드합니다.")
    @Parameter(name = "groupId", description = "그룹 ID", required = true)
    @Parameter(name = "fileName", description = "다운로드할 파일 이름", required = true)
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @GetMapping("/download-photo")
    public ResponseEntity<FileSystemResource> downloadPhoto(@RequestParam String groupId, @RequestParam String fileName) {
        try {
            System.out.println(groupId);
            System.out.println(fileName);
            FileSystemResource fileResource = createQrService.downloadPhoto(groupId, fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName)
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
                    .body(fileResource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}