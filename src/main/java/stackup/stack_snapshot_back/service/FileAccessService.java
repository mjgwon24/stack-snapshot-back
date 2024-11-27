package stackup.stack_snapshot_back.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class FileAccessService {
    final long EXPIRE = 300;//만료 기간 초 단위
    // 기한 지난 파일 삭제시 필요한 메소드
    public static long getCurrentTimestamp() {
        return Instant.now().getEpochSecond();
    }

    // 기한 지난 파일 삭제시 필요한 메소드
    public static long getTimestampFromString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }
    private ResponseEntity<Resource> make_response(File file) throws IOException {
        Resource resource = new FileSystemResource(file);

        String mimeType = Files.probeContentType(file.toPath());
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
    public ResponseEntity<Resource> read_File(String groupid,String date,String index,String OUTPUT_PATH,Boolean isFinal) throws IOException {
        String result_filename = OUTPUT_PATH;
        if(isFinal) {
            result_filename += "group_" + groupid + "_final_" + date + ".png";
        }
        else{
            result_filename += "group_" + groupid + "_" + date + "_" + index + ".png";
        }
        System.out.println(result_filename);
        File file = new File(result_filename);


        // 파일이 존재하지 않는 경우 처리
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 만료되었다면 삭제
//        if(isFinal&&getCurrentTimestamp()>getTimestampFromString(date)+EXPIRE){
//            file.delete();
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }


        // 파일을 Resource로 변환
        return make_response(file);
    }

}
