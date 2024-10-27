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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "QR 코드 API", description = "QR 코드 생성 및 사진 다운로드 API")
public class CreateQrRestController {

    private final CreateQrService createQrService;

    /**
     * QR 코드 생성 API
     * @param groupId QR 코드 생성 요청 데이터 (그룹 ID 포함)
     * @return QR 코드 이미지 데이터 응답
     */
    @Operation(summary = "QR 코드 생성", description = "최신 사진 파일을 다운로드할 수 있는 QR 코드를 생성하여 이미지를 반환합니다.")
    @PostMapping("/create-qr")
    public ResponseEntity<byte[]> createQrCode(@RequestParam String groupId) {
        try {
            byte[] qrCodeImage = createQrService.generateQrCode(groupId);
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
    @GetMapping("/download-photo")
    public ResponseEntity<FileSystemResource> downloadPhoto(@RequestParam String groupId, @RequestParam String fileName) {
        try {
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