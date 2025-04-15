package stackup.stack_snapshot_back.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.dto.GeneratedFileInfo;
import stackup.stack_snapshot_back.dto.GroupPhotosResponseDto;
import stackup.stack_snapshot_back.dto.SelectFrameRequestDto;
import stackup.stack_snapshot_back.dto.PhotoResponseDto;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 사진 처리 서비스
 * @ latest update 2025.04.08
 */
@Slf4j
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
     * @return GroupPhotosResponseDto groupId, date, timeStamp, (List)fileNames
     */
    public GroupPhotosResponseDto uploadPhotos(List<MultipartFile> images) throws IOException {
        List<String> photos = new ArrayList<>();
        File uploadDir = new File(uploadDirectory);
        int groupId = 1;

        // 업로드 디렉토리 존재 여부 확인 및 생성
        if (uploadDir.exists()) {
            File[] groupDirs = uploadDir.listFiles(File::isDirectory);
            if (groupDirs != null) {
                for (File groupDir : groupDirs) {
                    String dirName = groupDir.getName();
                    if (dirName.startsWith("group")) {
                        try {
                            int existingGroupId = Integer.parseInt(dirName.substring(5));
                            groupId = Math.max(groupId, existingGroupId + 1);
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
            }
        }

        // 공통 date와 timeStamp 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = sdf.format(new Date());
        String timeStamp = date.split("_")[1];
        date = date.split("_")[0];

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            File groupDirectory = new File(uploadDirectory, "group" + groupId);

            if (!groupDirectory.exists() && !groupDirectory.mkdirs()) {
                throw new IOException("Failed to create group directory: " + groupDirectory.getAbsolutePath());
            }

            GeneratedFileInfo fileInfo = fileNameGenerator.generateOriginalFileName(groupId, i + 1, Objects.requireNonNull(image.getOriginalFilename()), date, timeStamp);
            Path filePath = Paths.get(groupDirectory.getAbsolutePath(), fileInfo.getFileName());
            image.transferTo(filePath.toFile());

            photos.add(fileInfo.getFileName());
        }

        return GroupPhotosResponseDto.builder()
                .groupId(groupId)
                .date(date)
                .timeStamp(timeStamp)
                .fileNames(photos)
                .build();
    }

    /**
     * 업로드된 groupId 기반 사진 리스트 반환
     * @param groupId 그룹 ID
     * @return GroupPhotosResponseDto groupId, date, timeStamp, (List)fileNames
     */
    public GroupPhotosResponseDto getUploadedPhotos(int groupId) {
        List<String> fileNames = new ArrayList<>();
        File groupDirectory = new File(uploadDirectory, "group" + groupId);
        File[] files = groupDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        // 업로드된 사진이 있는 경우
        if (files != null) {
            for (File file : files) {
//                String fileUrl = "/stack-photo/" + file.getName();
                fileNames.add(file.getName());
            }
        }

        String date = fileNames.get(1).split("_")[2];
        String timeStamp = fileNames.get(1).split("_")[3];

        return GroupPhotosResponseDto.builder()
                .groupId(groupId)
                .date(date)
                .timeStamp(timeStamp)
                .fileNames(fileNames)
                .build();
    }

    /**
     * 업로드된 groupId, index 기반 사진 반환
     * @param groupId 그룹 ID
     * @param index 사진 인덱스
     * @return ResponseEntity<Resource> 사진 파일 리소스
     */
    public ResponseEntity<Resource> getUploadedPhoto(int groupId, Integer index) throws IOException {
        File groupDirectory = new File(uploadDirectory, "group" + groupId);
        File[] files = groupDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        // 업로드된 사진이 있는 경우
        if (files != null) {
            for (File file : files) {
                // index가 동일한지 확인
                if (file.getName().contains("_" + index + ".png")) {
                    return fileAccessService.make_response(file);
                }
            }
        }

        return ResponseEntity.notFound().build();
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
     * 선택된 프레임 기반 사진 합성
     * @param requestDto 업로드 요청 데이터 (selectedFrameId, groupId, List<String> selectPhotos)
     * @param UPLOAD_PATH 업로드 경로
     * @param FRAME_PATH 프레임 경로
     * @param OUTPUT_PATH 출력 경로
     * @return PhotoResponseDto date, timeStamp, fileName
     */
    public PhotoResponseDto uploadFile(SelectFrameRequestDto requestDto, String UPLOAD_PATH, String FRAME_PATH, String OUTPUT_PATH) throws IOException {
        List<String> selectPhotoNames = requestDto.getSelectPhotoNames();
        int groupId = requestDto.getGroupId();
        int frameId = requestDto.getSelectedFrameId();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = sdf.format(new Date());
        String timeStamp = date.split("_")[1];
        date = date.split("_")[0];

        // 이미지 병합
        String combinedImagePath = selectFrameService.mergeImages(
                selectPhotoNames, groupId, frameId, date, timeStamp, UPLOAD_PATH, FRAME_PATH, OUTPUT_PATH
        );

        return PhotoResponseDto.builder()
                .groupId(groupId)
                .date(date)
                .timeStamp(timeStamp)
                .fileName(combinedImagePath)
                .build();
    }

    /**
     * 최종 합성 이미지 반환
     * @param fileName 최종 합성 이미지 파일 이름
     * @param OUTPUT_PATH 출력 경로
     * @return 다운로드할 최종 합성 이미지 파일 리소스
     */
    public ResponseEntity<Resource> getFinalFile(String fileName, String OUTPUT_PATH) throws IOException {
        File file = new File(OUTPUT_PATH + fileName);
        return fileAccessService.make_response(file);
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
}