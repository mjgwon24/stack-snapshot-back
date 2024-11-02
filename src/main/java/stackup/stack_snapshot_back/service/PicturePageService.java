package stackup.stack_snapshot_back.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class PicturePageService {

    private static final Logger logger = Logger.getLogger(PicturePageService.class.getName());
    private final String uploadDirectory = "/Users/imseogjin/Desktop/stack-photo";
    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    /**
     * 사진 파일을 MultipartFile로 받아 저장
     *
     * @param file MultipartFile 객체 (이미지 파일)
     * @param photoNumber 사진 번호 (파일 이름에 포함됨)
     * @return 저장된 파일 이름
     * @throws IOException 파일 저장 실패 시
     */
    public String uploadFile(MultipartFile file, int photoNumber) throws IOException {
        // 업로드 디렉토리 확인 및 생성
        File directory = new File(uploadDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            logger.log(Level.SEVERE, "Failed to create upload directory: " + uploadDirectory);
            throw new IOException("Failed to create upload directory: " + uploadDirectory);
        }

        // 파일명 생성 및 저장 (photoNumber 포함)
        String fileName = fileNameGenerator.generateOriginalFileName("group", photoNumber, file.getOriginalFilename());
        Path filePath = Paths.get(uploadDirectory, fileName);

        try {
            file.transferTo(filePath.toFile());
            logger.log(Level.INFO, "File saved: " + fileName);
            return fileName;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save file", e);
            throw e;
        }
    }

    /**
     * 서버에 저장된 모든 PNG 파일의 URL을 반환
     *
     * @return 파일 URL 리스트
     */
    public List<String> getUploadedPhotos() {
        File directory = new File(uploadDirectory);
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        List<String> fileUrls = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                String fileUrl = "/files/" + file.getName();
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }
}
