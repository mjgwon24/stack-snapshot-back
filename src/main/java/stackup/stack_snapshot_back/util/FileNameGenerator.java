package stackup.stack_snapshot_back.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 파일명 생성기
 * - String groupId: 사진 6개를 묶을 그룹 아이디
 * - int photoNumber: 사진 번호 (1~6)
 * - String originalFilename: 원본 파일명
 */
public class FileNameGenerator {
    public String generateFileName(String groupId, int photoNumber, String originalFilename) {
        // 현재 날짜/시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDate = sdf.format(new Date());

        // 파일 확장자
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 파일명 예: group_1_20241014_215154_1.jpg
        return "group_" + groupId + "_" + currentDate + "_" + photoNumber + fileExtension;
    }
}
