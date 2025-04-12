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
import stackup.stack_snapshot_back.dto.GroupPhotosResponseDto;
import stackup.stack_snapshot_back.dto.SelectFrameRequestDto;
import stackup.stack_snapshot_back.dto.PhotoResponseDto;
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
     * @return GroupPhotosResponseDto groupId, date, timeStamp, (List)fileNames
     */
    @PostMapping
    public ResponseEntity<GroupPhotosResponseDto> uploadPhotos(List<MultipartFile> images) {
        try {
            GroupPhotosResponseDto responseDto = photoService.uploadPhotos(images);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 업로드 사진 groupId 기반 목록 조회 API
     * @param groupId 그룹 ID
     * @return GroupPhotosResponseDto groupId, date, timeStamp, (List)fileNames
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupPhotosResponseDto> getPhotosByGroupId(@PathVariable("groupId") Integer groupId) {
        try {
            GroupPhotosResponseDto photos = photoService.getUploadedPhotos(groupId);

            if (photos == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(photos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 업로드 사진 groupId 기반 개별 조회 API
     * @param groupId 그룹 ID
     * @param index 사진 인덱스
     * @return 사진 파일 리소스
     */
    @GetMapping("/group/{groupId}/{index}")
    public ResponseEntity<Resource> getOriginPhotoByGroupAndIndex(@PathVariable("groupId") Integer groupId, @PathVariable("index") Integer index) {
        try {
            return photoService.getUploadedPhoto(groupId, index);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
     * @param SelectFrameRequestDto 업로드 요청 데이터 (selectedFrameId, groupId, file)
     * @return PhotoResponseDto date, timeStamp, fileName
     */
    @PostMapping("/frames")
    public ResponseEntity<PhotoResponseDto> uploadWithFrame(@ModelAttribute SelectFrameRequestDto requestDto) {
        try {
            PhotoResponseDto response = photoService.uploadFile(requestDto, UPLOAD_PATH, FRAME_PATH, OUTPUT_PATH);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 최종 합성 이미지 조회 API
     * @param fileName 최종 합성 이미지 파일 이름
     * @return 최종 합성 이미지 파일 리소스
     */
    @GetMapping("/final/{fileName:.+}")
    public ResponseEntity<Resource> getFinalPhoto(@PathVariable("fileName") String fileName) {
        try {
            return photoService.getFinalFile(fileName, OUTPUT_PATH);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}