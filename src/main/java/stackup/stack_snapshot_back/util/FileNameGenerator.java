package stackup.stack_snapshot_back.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 파일명 생성기
 */
public class FileNameGenerator {

    /**
     * 원본 파일명 생성
     * @param groupId - 사진 6개를 묶을 그룹 아이디
     * @param photoNumber - 사진 번호 (1~6)
     * @param originalFilename - 원본 파일명
     * @return 생성된 원본 파일명
     */
    public String generateOriginalFileName(String groupId, int photoNumber, String originalFilename) {
        // 현재 날짜/시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDate = sdf.format(new Date());

        // 파일 확장자
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 파일명 예: group_1_20241014_215154_1.png
        return "group_" + groupId + "_" + currentDate + "_" + photoNumber + fileExtension;
    }

    /**
     * 완성 파일명 생성
     * @param groupId - 사진 6개를 묶을 그룹 아이디
     * @return 생성된 완성 파일명
     */
    public String generateFinalFileName(String groupId) {
        // 현재 날짜/시간
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDate = sdf.format(new Date());

        // 완성된 사진 파일명 예: group_1_final_20241014_215154.png
        return "group_" + groupId + "_final_" + currentDate + ".png";
    }
}
