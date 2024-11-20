package stackup.stack_snapshot_back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 사진 관리 API를 처리하는 서비스
 * @since 2024.10.17
 * author 임석진
 */

@Service
public class PicturePageService {

    private static final Logger logger = LoggerFactory.getLogger(PicturePageService.class);
    private static final String ORIGINAL_DIR = "original-photo"; // 하위 디렉토리 이름 변경

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    /**
     * 파일 업로드 메서드
     * @param file 업로드할 파일
     * @param photoNumber 사진 번호
     * @return 저장된 파일명
     * @throws IOException 파일 저장 실패 시 예외
     */
    public String uploadFile(MultipartFile file, int photoNumber) throws IOException {

        Path originalDirectoryPath = Paths.get(uploadDirectory, ORIGINAL_DIR);
        File originalDirectory = originalDirectoryPath.toFile();


        if (!originalDirectory.exists()) {
            boolean dirsCreated = originalDirectory.mkdirs();
            if (dirsCreated) {
                logger.info("Created upload directory: {}", originalDirectoryPath);
            } else {
                logger.error("Failed to create upload directory: {}", originalDirectoryPath);
                throw new IOException("Failed to create upload directory: " + originalDirectoryPath);
            }
        }

        String groupId = "1";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IOException("Original filename is null");
        }
        String fileName = fileNameGenerator.generateOriginalFileName(groupId, photoNumber, originalFilename);
        Path filePath = originalDirectoryPath.resolve(fileName);


        try {
            file.transferTo(filePath.toFile());
            logger.info("File saved: {}", filePath);
        } catch (IOException e) {
            logger.error("Failed to save file: {}", filePath, e);
            throw e;
        }

        return fileName;
    }

    /**
     * 업로드된 사진 목록 조회 메서드
     * @return 파일 URL 목록
     */
    public List<String> getUploadedPhotos() {
        List<String> fileUrls = new ArrayList<>();
        Path originalDirectoryPath = Paths.get(uploadDirectory, ORIGINAL_DIR);
        File originalDirectory = originalDirectoryPath.toFile();

        if (!originalDirectory.exists()) {
            logger.warn("Upload directory does not exist: {}", originalDirectoryPath);
            return fileUrls;
        }

        File[] files = originalDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {
                String fileUrl = "/stack-photo/" + ORIGINAL_DIR + "/" + file.getName();
                fileUrls.add(fileUrl);
            }
        } else {
            logger.warn("No files found in directory: {}", originalDirectoryPath);
        }

        return fileUrls;
    }
}
