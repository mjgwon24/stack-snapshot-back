package stackup.stack_snapshot_back.util;

import org.springframework.stereotype.Service;
import stackup.stack_snapshot_back.dto.GeneratedFileInfo;

/**
 * 파일명 생성기
 */
@Service
public class FileNameGenerator {

    /**
     * 원본 파일명 생성
     * @param groupId - 사진 6개를 묶을 그룹 아이디
     * @param index - 사진 번호 (1~6)
     * @param originalFilename - 원본 파일명
     * @param date - 현재 날짜
     * @param timeStamp - 현재 시간
     * @return GeneratedFileInfo date, timeStamp, index, fileName
     */
    public GeneratedFileInfo generateOriginalFileName(int groupId, int index, String originalFilename, String date, String timeStamp) {
        // 파일 확장자
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 파일명 예: group_1_20241014_215154_1.png
        String fileName = "group_" + groupId + "_" + date + "_" + timeStamp + "_" + index + fileExtension;

        return GeneratedFileInfo.builder()
                .date(date)
                .timeStamp(timeStamp)
                .fileName(fileName)
                .index(index)
                .build();
    }

    /**
     * 완성 파일명 생성
     * @param groupId - 사진 6개를 묶을 그룹 아이디
     * @param date - 현재 날짜
     * @param timeStamp - 현재 시간
     * @return 생성된 완성 파일명
     */
    public String generateFinalFileName(Integer groupId, String date, String timeStamp) {
        // 완성된 사진 파일명 예: group_1_final_20241014_215154.png
        return "group_" + groupId + "_final_" + date + "_" + timeStamp + ".png";
    }
}
