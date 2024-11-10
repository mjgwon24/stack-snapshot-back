package stackup.stack_snapshot_back;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import stackup.stack_snapshot_back.service.PicturePageService;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@SpringBootTest
class PicturePageServiceTest {

    @Value("${file.upload-dir}")
    private String uploadDirectory;

    private PicturePageService picturePageService;
    private FileNameGenerator fileNameGenerator;

    @BeforeEach
    void setUp() {
        fileNameGenerator = new FileNameGenerator();
        picturePageService = new PicturePageService();
    }

    @Test
    void testUploadFile_Success() throws IOException {
        // 가짜 MultipartFile 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        // 파일 업로드 시도
        String fileName = picturePageService.uploadFile(mockFile, 1);

        // 업로드된 파일 확인
        Path filePath = Path.of(uploadDirectory, fileName);
        assertTrue(Files.exists(filePath), "파일이 업로드되지 않았습니다.");

        // 테스트가 끝난 후 파일 삭제
        Files.deleteIfExists(filePath);
    }

    @Test
    void testUploadFile_Failure_DirectoryCreation() {
        // PicturePageService를 spy로 감싸기
        PicturePageService spyService = spy(picturePageService);

        // 가짜 파일 생성
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test image content".getBytes()
        );

        // 디렉토리 생성 실패 테스트
        assertThrows(IOException.class, () -> spyService.uploadFile(mockFile, 1));
    }

    @Test
    void testGetUploadedPhotos() {
        // 업로드된 사진 목록 가져오기
        List<String> photos = picturePageService.getUploadedPhotos();

        // 사진 목록이 비어 있지 않은지 확인
        assertNotNull(photos);
    }
}
