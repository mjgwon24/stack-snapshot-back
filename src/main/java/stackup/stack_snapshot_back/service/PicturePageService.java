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

/**
 * 사진 관리 API를 처리하는 컨트롤러
 * @since 2024.10.17
 * author 임석진
 */

@Service
public class PicturePageService {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    public String uploadFile(MultipartFile file, int photoNumber) throws IOException {
        File directory = new File(uploadDirectory);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException("Failed to create upload directory: " + uploadDirectory);
        }

        String groupId = "1";
        String fileName = fileNameGenerator.generateOriginalFileName(groupId, photoNumber, file.getOriginalFilename());
        Path filePath = Paths.get(uploadDirectory, fileName);

        file.transferTo(filePath.toFile());
        return fileName;
    }

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
}
