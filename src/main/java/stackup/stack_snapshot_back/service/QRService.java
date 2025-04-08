package stackup.stack_snapshot_back.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import stackup.stack_snapshot_back.util.FileNameGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * QR 코드 생성 및 사진 다운로드 서비스
 * @since 2024.10.27
 */
@Service
public class QRService {

    @Value("${file.upload-dir}")
    private String STATIC_DIR;

    @Value("${server.url}")
    private String serverUrl;

    private String qrCodeImagePath;

    private final FileNameGenerator fileNameGenerator = new FileNameGenerator();

    @PostConstruct
    public void initPaths() {
        qrCodeImagePath = STATIC_DIR + "/final-photo/";
    }

    /**
     * 특정 그룹 디렉토리에서 가장 최근에 생성된 파일 찾기
     * @param groupId 그룹 ID
     * @return 최신 파일 이름
     */
    private String getLatestFileName(String groupId) {
        File groupDirectory = new File(qrCodeImagePath);
        if (!groupDirectory.exists() || !groupDirectory.isDirectory()) {
            throw new RuntimeException("그룹 디렉토리가 존재하지 않거나 디렉토리가 아닙니다: " + groupDirectory.getAbsolutePath());
        }

        File[] files = groupDirectory.listFiles((dir, name) -> name.contains("_final_"));
        if (files == null || files.length == 0) {
            throw new RuntimeException("그룹 디렉토리에서 최신 _final_ 파일을 찾을 수 없습니다: " + groupDirectory.getAbsolutePath());
        }

        return Arrays.stream(files)
                .max(Comparator.comparingLong(File::lastModified))
                .orElseThrow(() -> new RuntimeException("최신 파일을 찾을 수 없습니다. 파일 정렬 과정에서 문제가 발생했습니다."))
                .getName();
    }

    /**
     * QR 코드 생성
     * @param groupId 그룹 ID
     * @param date 날짜
     * @return QR 코드 이미지 데이터 (byte[])
     */
    public byte[] generateQrCode(String groupId, String date) {
        try {
            String latestFileName = getLatestFileName(groupId);
            if (latestFileName == null) {
                throw new RuntimeException("파일 이름이 null입니다. groupId: " + groupId);
            }

            String downloadUrl = serverUrl + "/api/qrs/" + groupId + "/photos/" + latestFileName;

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(downloadUrl, BarcodeFormat.QR_CODE, 350, 350, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);

            return baos.toByteArray();

        } catch (IOException | WriterException e) {
            throw new RuntimeException("QR 코드 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 사진 다운로드
     * @param groupId 그룹 ID
     * @param fileName 다운로드할 사진 파일 이름
     * @return FileSystemResource로 파일 리소스 반환
     */
    public FileSystemResource downloadPhoto(String groupId, String fileName) {
        String filePath = qrCodeImagePath + "/" + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("파일을 찾을 수 없습니다: " + fileName);
        }

        return new FileSystemResource(file);
    }
}