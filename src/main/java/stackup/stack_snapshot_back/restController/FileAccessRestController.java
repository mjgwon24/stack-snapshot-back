package stackup.stack_snapshot_back.restController;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stackup.stack_snapshot_back.service.FileAccessService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@RestController
@RequestMapping("/api")
@PropertySource("classpath:application.yml") //application.yml에 들어있는 데이터 사용, Service에서는 사용 못하기 때문에 넘겨줘야함
public class FileAccessRestController {
    @Value("${file.upload-dir}")
    String STATIC_DIR;
    String UPLOAD_PATH, OUTPUT_PATH, FRAME_PATH;



    // 프로그램이 실행되면 변수 초기화
    @PostConstruct
    public void initPaths() {
        UPLOAD_PATH = STATIC_DIR + "/original-photo/";
        OUTPUT_PATH = STATIC_DIR + "/final-photo/";
        FRAME_PATH = STATIC_DIR + "/frames/";
    }
    @CrossOrigin(origins = "${server.cross-origin-url}")
    @GetMapping("/file")
    public ResponseEntity<Resource> getFile(@RequestParam String date, @RequestParam String groupid, @RequestParam String index) throws IOException {
        // group_test_20241101_010321_0.jpg
        System.out.println(date);
        System.out.println(groupid);
        System.out.println(index);
        System.out.println();
        try{
            FileAccessService fileAccessService = new FileAccessService();
            return fileAccessService.read_File(groupid,date,index,UPLOAD_PATH,false);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin(origins = "${server.cross-origin-url}")
    // 사용자가 접근 할 수 있어야 하는 경로이므로 Cross Origin 적용 x
    @GetMapping("/final_file")
    public ResponseEntity<Resource> getFinalFile(@RequestParam String date, @RequestParam String groupid) throws IOException {
        // 파일 경로 설정 (프로젝트 외부의 파일 경로)
        // group_test_20241101_010321_0.jpg
        // group_test_final_20241101_224410.png
        try{
            FileAccessService fileAccessService = new FileAccessService();
            return fileAccessService.read_File(groupid,date,null,OUTPUT_PATH,true);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
}
