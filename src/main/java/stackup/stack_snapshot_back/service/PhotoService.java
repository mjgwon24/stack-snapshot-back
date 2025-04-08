package stackup.stack_snapshot_back.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.dto.SelectFrameRequestDto;
import stackup.stack_snapshot_back.dto.SelectFrameResponseDto;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 사진 처리 서비스
 * @ latest update 2025.04.08
 */
@Service
@RequiredArgsConstructor
public class PhotoService {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @PostConstruct
    public void initPaths() {
        uploadDirectory = uploadDirectory + "/original-photo/";
    }

    private final SelectFrameService selectFrameService;
    private final FileAccessService fileAccessService;
    private final FileNameGenerator fileNameGenerator;

    /**
     * 원본 사진 업로드 API
     * @param images 업로드할 사진 리스트
     * @return 업로드된 사진 URL 리스트
     */
    public List<String> uploadPhotos(List<MultipartFile> images) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            File directory = new File(uploadDirectory);

            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException("Failed to create upload directory: " + uploadDirectory);
            }

            String fileName = fileNameGenerator.generateOriginalFileName("1", i + 1, image.getOriginalFilename());
            Path filePath = Paths.get(uploadDirectory, fileName);
            image.transferTo(filePath.toFile());

            String fileUrl = "/original-photo/" + fileName;
            fileUrls.add(fileUrl);
        }
        return fileUrls;
    }

    /**
     * 업로드된 전체 사진 리스트 가져오기
     * @return 업로드된 전체 사진 URL 리스트
     */
    public List<String> getUploadedPhotos() {
        List<String> fileUrls = new ArrayList<>();
        File directory = new File(uploadDirectory);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {
                String fileUrl = "/stack-photo/" + file.getName();
                fileUrls.add(fileUrl);
            }
        }

        return fileUrls;
    }

    /**
     * 사진 다운로드 API
     * @param fileName 다운로드할 사진 파일 이름
     * @return 다운로드할 사진 파일 리소스
     * @throws MalformedURLException 잘못된 URL 예외
     */
    public ResponseEntity<Resource> serveFile(String fileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDirectory).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 사진 업로드 및 프레임 선택 API
     * @param requestDto 업로드 요청 데이터 (그룹 ID, 선택된 프레임 ID 포함)
     * @param UPLOAD_PATH 업로드 경로
     * @param FRAME_PATH 프레임 경로
     * @param OUTPUT_PATH 출력 경로
     * @return 업로드된 사진과 선택된 프레임을 합성한 결과 데이터
     * @throws IOException 입출력 예외
     */
    public SelectFrameResponseDto uploadFile(SelectFrameRequestDto requestDto, String UPLOAD_PATH, String FRAME_PATH, String OUTPUT_PATH) throws IOException {
        List<MultipartFile> files = requestDto.getFile();
        String GroupID = requestDto.getGroupID();
        int FrameID = requestDto.getSelectedFrameID();

        SelectFrameResponseDto response = new SelectFrameResponseDto();
        response.setGroupID(GroupID);

        List<String> fileNames = selectFrameService.FileUpload(UPLOAD_PATH, GroupID, files);
        String combinedImagePath = selectFrameService.mergeImages(fileNames, GroupID, FrameID, UPLOAD_PATH, FRAME_PATH, OUTPUT_PATH);
        response.setOutputPath(combinedImagePath);

        for (String fileName : fileNames) {
            File file = new File(UPLOAD_PATH + fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        return response;
    }

    /**
     * 업로드된 사진 다운로드 API
     * @param date 촬영 날짜
     * @param groupid 그룹 ID
     * @param index 사진 인덱스
     * @param UPLOAD_PATH 업로드 경로
     * @return 다운로드할 사진 파일 리소스
     * @throws IOException 입출력 예외
     */
    public ResponseEntity<Resource> getFile(String groupid, String date, String index, String UPLOAD_PATH) throws IOException {
        return fileAccessService.read_File(groupid, date, index, UPLOAD_PATH, false);
    }

    /**
     * 최종 합성 이미지 다운로드 API
     * @param date 촬영 날짜
     * @param groupid 그룹 ID
     * @param OUTPUT_PATH 출력 경로
     * @return 다운로드할 최종 합성 이미지 파일 리소스
     * @throws IOException 입출력 예외
     */
    public ResponseEntity<Resource> getFinalFile(String date, String groupid, String OUTPUT_PATH) throws IOException {
        return fileAccessService.read_File(groupid, date, null, OUTPUT_PATH, true);
    }
}