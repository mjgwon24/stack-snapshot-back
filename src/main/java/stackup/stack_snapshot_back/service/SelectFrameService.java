package stackup.stack_snapshot_back.service;

import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.util.FileNameGenerator;
/**
 * 완성 사진 생성 서비스
 * 4개의 이미지를 받아 하나의 이미지로 합쳐주는 서비스
 * @since 2024-10-22
 * @author 김이현
 */
@Service
public class SelectFrameService {
    private static final Logger log = LoggerFactory.getLogger(Log.class);

    // 저장될 이미지 포멧
    final String EXT = "png";

    // 프레임별 이미지 개별 크기
    final int[][] IMAGE_SIZE_AT_FRAME = {
            {273,373},//frame1 273x373
            {272,205},//frame2 272x205
            {272,328},//frame3 272x328
            {340,273}//frame4 340x273
    };
    // 프레임별 이미지 오프셋
    final int[][][] OFFSET = {
            {
                {20,22},
                {308,22},
                {20,412},
                {308,412},
            },//frame1
            {
                    {21,21},
                    {309,21},
                    {21,237},
                    {309,237},
                    {21,452},
                    {309,452},
            },//frame2
            {
                    {21,34},
                    {21,383},
                    {308,204},
                    {308,553},
            },//frame3
            {
                    {31,19},
                    {387,19},
                    {31,307},
                    {387,307},
            },//frame4
    };


    /**
     *
     * @param imageFiles
     * @param FrameId
     * @return Finalfilename
     * @throws IOException
     */
    public String mergeImages(List<String> imageFiles,String GroupID,int FrameId,String UPLOAD_PATH,String FRAME_PATH,String OUTPUT_PATH) throws IOException {
        if(FRAME_PATH ==null){
            throw new IllegalArgumentException("FRAME_PATH가 null입니다.");
        }
        if(FrameId>4||FrameId<1){
            throw new IllegalArgumentException("FramdId의 범위는 1~4입니다.");
        }

        //FrameId에 해당하는 프레임에 필요한 이미지의 수를 구함
        int ImageCount = OFFSET[FrameId-1].length;

        if (imageFiles.size() != ImageCount) {
            throw new IllegalArgumentException("이미지 파일의 개수가 프레임에 맞지 않습니다. : "+imageFiles.size()+"/"+ImageCount);
        }
        try {
            // 프레임 이미지 로드
            BufferedImage baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"."+EXT));
            Graphics2D frame = baseImage.createGraphics();

            // 프레임에 들어갈 사진의 크기
            int image_width,image_height;

            // 프레임에 들어갈 사진 저장하는 변수
            BufferedImage Image;

            // FrameId에 해당하는 프레임에 들어갈 이미지의 가로, 세로 크기를 가져옴
            int result_Image_Width=IMAGE_SIZE_AT_FRAME[FrameId-1][0];
            int result_Image_Height=IMAGE_SIZE_AT_FRAME[FrameId-1][1];

            int Offset_X;
            int Offset_Y;

            // 프레임에 들어갈 이미지의 수 만큼 반복
            for(int i=0;i<ImageCount;i++){
                Image = ImageIO.read(new File(UPLOAD_PATH+"/"+GroupID+"/"+imageFiles.get(i)));
                image_width = Image.getWidth(null);
                image_height = Image.getHeight(null);
                Offset_X = (image_width-result_Image_Width)/2;
                Offset_Y = (image_height-result_Image_Height)/2;

                Image = Image.getSubimage(Offset_X,Offset_Y,result_Image_Width,result_Image_Height);
                frame.drawImage(Image,OFFSET[FrameId-1][i][0],OFFSET[FrameId-1][i][1],null);
            }
            //프레임 위에 사진 이미지 작성 종료 후 닫기
            frame.dispose();

            //파일명 생성을 위한 FileNameGenerator 객체 생성
            FileNameGenerator filenamegenerator = new FileNameGenerator();

            //GroupID에 대해 최종 파일명을 할당 받음
            String Finalfilename = filenamegenerator.generateFinalFileName(GroupID);

            //최종 파일 생성 및 이미지 쓰기
            File outputFile = new File(OUTPUT_PATH+Finalfilename);
            ImageIO.write(baseImage, EXT, outputFile);

            return Finalfilename;
        } catch (Exception e) {
            throw new IOException("이미지 파일 읽기중 문제가 생겼습니다.:"+e);
        }
    }
    public List<String> FileUpload(String UPLOAD_PATH,String GroupID,List<MultipartFile> files){
        List<String> FileNames = new ArrayList<>();

        Path uploadPath = Paths.get(UPLOAD_PATH);
        System.out.println(UPLOAD_PATH);

        // uploadPath에 해당하는 위치가 없거나 파일이라면
        if (!Files.exists(uploadPath) || !Files.isDirectory(uploadPath)) {
            // 폴더 생성 및 검사
            if(new File(uploadPath+GroupID).mkdirs()){
                System.out.println("Directory created. : "+uploadPath+GroupID);
            }
        }

        //파일명 생성기 객체 생성
        FileNameGenerator fileNameGenerator = new FileNameGenerator();

        // 파일명 생성에 사용될 index
        int index = 0;

        // 받은 파일 목록 순회
        for(MultipartFile file:files) {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty");
            }
            try {
                // 파일로부터 데이터 가져오기
                byte[] bytes = file.getBytes();

                // 파일명 및 파일 경로 생성
                String filename = fileNameGenerator.generateOriginalFileName(GroupID,index,file.getOriginalFilename());
                Path path = Paths.get(uploadPath+"/"+GroupID+"/"+filename);
                System.out.println(uploadPath+"/"+GroupID+"/"+filename);
                // 파일에 데이터 쓰기
                Files.write(path, bytes);

                // 응답 데이터에 파일명 추가
                FileNames.add(filename);

                // 파일명에 필요한 index값 증가
                index++;
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to upload file.");
            }
        }
        return FileNames;
    }
}
