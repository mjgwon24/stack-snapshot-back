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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    final String FONTNAME = "Pretendard";
    // 저장될 이미지 포멧
    final String EXT = "png";

    final int[] FONT_SIZE_AT_FRAME = {
            12,
            18,
            12,
            12,
    };

    // 프레임별 이미지 개별 크기
    final int[][] IMAGE_SIZE_AT_FRAME = {
            {273,373},//frame1 273x373
//            {272,205},//frame2 272x205
            {270,330},//frame2 272x205
            {272,328},//frame3 272x328
            {340,272}//frame4 340x273
    };
    // 프레임별 텍스트 색
    final Color[] TEXT_COLOR = {
            new Color(235,235,235),//frame1 EBEBEB 235 235 235
            new Color(113,122,127),//frame2 717A7F 113 122 127
            new Color(191, 170, 15),//frame3 BFAA0F 191 170 15
            new Color(255, 255, 255),//frame4
    };
    // 프레임별 텍스트 오프셋
    final int[][] TEXT_OFFSET = {
            {
                    517,790
            },//frame1
            {
                    28,860
            },//frame2
            {
                    514,885
            },//frame3
            {
                    0,0
            }//frame4
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
                    {20,80},
                    {310,80},
                    {20,430},
                    {310,430}
            },//frame2
            {
                    {21,34},
                    {21,383},
                    {308,204},
                    {308,551},
            },//frame3
            {
                    {31,20},
                    {388,20},
                    {31,307},
                    {388,307},
            },//frame4
    };


    /**
     * 현재 날짜 문자열을 출력합니다 (2000.01.01 형식)
     * @return 현재 날짜 문자열
     */
    public String generateCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        return sdf.format(new Date());
    }

    /**
     * 입력받은 문자열의 사각영역을 반환합니다
     * @param text
     * @param font
     * @return Rectangle
     */
    private Rectangle getFontrect(String text, Font font){
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        return new Rectangle(0, 0, width, height);
    }

    /**
     * 이미지에서 2D 객체를 얻어옵니다
     * @param img
     * @return BufferedImage
     */
    private Graphics2D getG2D(BufferedImage img)
    {
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        return g2d;
    }

    /** 선택된 이미지 경로와 GroupID, FrameID를 통해 완성 사진을 생성하는 서비스
     * @param imageFiles
     * @param GroupID
     * @param FrameId
     * @param UPLOAD_PATH
     * @param FRAME_PATH
     * @param OUTPUT_PATH
     * @return Date
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
            BufferedImage baseImage = null;
            // 프레임 이미지 로드
            if(FrameId!=4){
                baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"."+EXT));
            }
            else{
                // FrameId가 4일 현재 날짜(일)에 따라 불러옴
                if(new Date().getDate()!=1){
                    baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"-1."+EXT));
                }
                else{
                    baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"-2."+EXT));
                }
            }


            Graphics2D frame = baseImage.createGraphics();

            // 프레임에 들어갈 사진 저장하는 변수
            BufferedImage Image;


            // 프레임에 들어갈 이미지의 수 만큼 반복
            for(int i=0;i<ImageCount;i++){
                System.out.println(i);
                Image = ImageIO.read(new File(UPLOAD_PATH+imageFiles.get(i)));
                frame.drawImage(Image,OFFSET[FrameId-1][i][0],OFFSET[FrameId-1][i][1],null);

            }

            BufferedImage second_baseImage;
            // 프레임 이미지 로드
            if(FrameId!=4){
                second_baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"."+EXT));
            }
            else{
                if(new Date().getDate()!=1){
                    second_baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"-1."+EXT));
                }
                else{
                    second_baseImage = ImageIO.read(new File(FRAME_PATH +FrameId+"-2."+EXT));
                }
            }


            frame.drawImage(second_baseImage,0,0,null);
            if(FrameId!=4){
                String current = generateCurrentDate();
                // Font.PLAIN 부분 {Font. PLAIN,Font. BOLD,Font. ITALIC} 중 선택 가능
                Font font = new Font(FONTNAME, Font.PLAIN,FONT_SIZE_AT_FRAME[FrameId-1]);
                Rectangle r = getFontrect(current, font);

                int width = (int) r.getWidth();
                int height = (int) r.getHeight();

                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = getG2D(img);
                g2d.setFont(font);
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(TEXT_COLOR[FrameId-1]);
                g2d.drawString(current, 0, fm.getAscent());


                frame.drawImage(img,TEXT_OFFSET[FrameId-1][0],TEXT_OFFSET[FrameId-1][1],null);
                g2d.dispose();
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

            return Finalfilename.split("final_")[1].split("\\.")[0]; // date 부분만 반환
        } catch (Exception e) {
            throw new IOException("이미지 파일 읽기중 문제가 생겼습니다.:"+e);
        }
    }

    /**
     * 파일을 업로드 해서 저장된 경로 리스트를 반환하는 서비스
     * @param UPLOAD_PATH
     * @param GroupID
     * @param files
     * @return FileNames
     */
    public List<String> FileUpload(String UPLOAD_PATH,String GroupID,List<MultipartFile> files){
        List<String> FileNames = new ArrayList<>();

        Path uploadPath = Paths.get(UPLOAD_PATH);

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
                Path path = Paths.get(uploadPath+"/"+filename);
                // 파일에 데이터 쓰기
                Files.write(path, bytes);

                // 응답 데이터에 파일명 추가
                FileNames.add(filename);

                // 파일명에 필요한 index값 증가
                index++;
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to upload file."+e.toString());
            }
        }
        return FileNames;
    }
}
