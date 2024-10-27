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

/**
 * 사진 업로드 및 조회를 처리하는 서비스
 * @since 2024.10.17
 * author 임석진
 */

@Service
public class PicturePageService {

    private final String uploadDirectory = "/Users/imseogjin/Desktop/stack-photo";  // 기본 저장 경로
    private final FileNameGenerator fileNameGenerator = new FileNameGenerator(); // 파일명 생성기 인스턴스

    /**
     * 팀 디렉토리 이름을 순차적으로 생성
     * @return 새로 생성할 팀 디렉토리 이름
     */
    public synchronized String generateNextTeamDirectoryName() {
        File baseDir = new File(uploadDirectory);
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        // 기존 팀 디렉토리 확인 (team1, team2, ... 형태)
        File[] teamDirs = baseDir.listFiles(File::isDirectory);
        int nextTeamNumber = 1;

        if (teamDirs != null) {
            // teamX 형식의 디렉토리 중 가장 큰 숫자를 찾음
            for (File teamDir : teamDirs) {
                String dirName = teamDir.getName();
                if (dirName.startsWith("team")) {
                    try {
                        int teamNumber = Integer.parseInt(dirName.substring(4));  // 'team' 이후 숫자만 추출
                        if (teamNumber >= nextTeamNumber) {
                            nextTeamNumber = teamNumber + 1;  // 가장 큰 숫자 다음 번호 할당
                        }
                    } catch (NumberFormatException e) {
                        // 'team' 다음에 숫자가 없는 경우 무시
                    }
                }
            }
        }

        return "team" + nextTeamNumber;
    }

    /**
     * 사진 파일을 업로드하고 서버에 저장
     * @param imageFiles 업로드할 이미지 파일 리스트
     * @return 저장된 파일 이름 리스트
     * @throws IOException 파일 저장 중 예외 발생 시
     */
    public List<String> uploadFiles(List<MultipartFile> imageFiles) throws IOException {
        List<String> fileNames = new ArrayList<>();

        // 새 팀 디렉토리 이름 생성 (team1, team2, team3, ...)
        String teamDirectoryName = generateNextTeamDirectoryName();
        String teamDirectoryPath = uploadDirectory + File.separator + teamDirectoryName;
        File teamDirectory = new File(teamDirectoryPath);

        // 팀별 디렉토리 생성 (없으면 생성)
        if (!teamDirectory.exists() && !teamDirectory.mkdirs()) {
            throw new IOException("Failed to create team directory: " + teamDirectoryPath);
        }

        // 각 이미지 파일 처리
        for (int i = 0; i < imageFiles.size(); i++) {
            MultipartFile imageFile = imageFiles.get(i);
            String originalFilename = imageFile.getOriginalFilename();

            // FileNameGenerator를 사용하여 파일명 생성 (teamDirectoryName과 photoNumber를 사용)
            String fileName = fileNameGenerator.generateOriginalFileName(teamDirectoryName, i + 1, originalFilename);
            Path filePath = Paths.get(teamDirectoryPath, fileName);  // 팀별 디렉토리에 저장

            try {
                // 파일 저장
                imageFile.transferTo(filePath.toFile());
                fileNames.add(fileName); // 저장된 파일명 리스트에 추가
            } catch (IOException e) {
                throw new IOException("Failed to save file: " + fileName + " in directory: " + teamDirectoryName, e);
            }
        }

        return fileNames;
    }

    /**
     * 서버에 저장된 모든 PNG 파일의 URL을 반환
     * @param teamDirectoryName 고유한 팀 디렉토리 이름 (team1, team2, team3, ...)
     * @return 파일 URL 리스트
     */
    public List<String> getUploadedPhotos(String teamDirectoryName) {
        String teamDirectoryPath = uploadDirectory + File.separator + teamDirectoryName;
        File teamDirectory = new File(teamDirectoryPath);
        File[] files = teamDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        List<String> fileUrls = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                // 팀 디렉토리 하위의 파일 URL 반환
                String fileUrl = "/files/" + teamDirectoryName + "/" + file.getName();  // 고유한 팀별 디렉토리 경로 반영
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }

    /**
     * 특정 팀 디렉토리가 존재하는지 확인
     * @param teamId 팀 ID
     * @return 존재 여부
     */
    public boolean doesTeamDirectoryExist(String teamId) {
        File teamDirectory = new File(uploadDirectory + File.separator + teamId);
        return teamDirectory.exists() && teamDirectory.isDirectory();
    }
}
