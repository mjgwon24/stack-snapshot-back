package stackup.stack_snapshot_back.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.util.FileNameGenerator;
import org.slf4j.Logger; // 변경
import org.slf4j.LoggerFactory; // 변경

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PicturePageService {

    private static final Logger logger = LoggerFactory.getLogger(PicturePageService.class); // 변경

    @Value("${file.upload-dir}")
    private String uploadDirectory; // @Value 애너테이션으로 파일 업로드 경로 주입

    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    public String uploadFile(MultipartFile file, int photoNumber) throws IOException {
        File directory = new File(uploadDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            logger.error("Failed to create upload directory: " + uploadDirectory);
            throw new IOException("Failed to create upload directory: " + uploadDirectory);
        }

        String fileName = fileNameGenerator.generateOriginalFileName("group", photoNumber, file.getOriginalFilename());
        Path filePath = Paths.get(uploadDirectory, fileName);

        try {
            file.transferTo(filePath.toFile());
            logger.info("File saved: " + fileName);
            return fileName;
        } catch (IOException e) {
            logger.error("Failed to save file", e);
            throw e;
        }
    }

    public List<String> getUploadedPhotos() {
        List<String> fileUrls = new ArrayList<>();
        try {
            File directory = new File(uploadDirectory);
            File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

            if (files == null) {
                logger.error("Failed to list files in directory: " + uploadDirectory);
                return fileUrls; // 빈 리스트 반환
            }

            for (File file : files) {
                String fileUrl = "/stack-photo/" + file.getName();
                fileUrls.add(fileUrl);
                logger.info("File URL added: " + fileUrl); // 변경
            }

            logger.info("Total file URLs returned: " + fileUrls.size());
        } catch (Exception e) {
            logger.error("An error occurred while retrieving uploaded photos: " + e.getMessage(), e);
        }

        return fileUrls;
    }
}
