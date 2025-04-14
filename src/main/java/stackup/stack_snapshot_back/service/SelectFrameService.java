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
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.multipart.MultipartFile;
import stackup.stack_snapshot_back.util.FileNameGenerator;
/**
 * 완성 사진 생성 서비스
 * 4개의 이미지를 받아 하나의 이미지로 합쳐주는 서비스
 * @since 2024.10.22
 * @author 김이현
 */
@Service
public class SelectFrameService {
    // 프레임 이미지 캐시
    private final ConcurrentHashMap<Integer, BufferedImage> frameImageCache = new ConcurrentHashMap<>();

    final String FONTNAME = "Pretendard";
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
     * @param imageFiles 선택된 이미지 경로
     * @param groupId 그룹 ID
     * @param frameId 프레임 ID
     * @param UPLOAD_PATH 업로드 경로
     * @param FRAME_PATH 프레임 경로
     * @param OUTPUT_PATH 출력 경로
     * @return 파일명
     */
    public String mergeImages(List<String> imageFiles,
                              int groupId,
                              int frameId,
                              String date,
                              String timeStamp,
                              String UPLOAD_PATH,
                              String FRAME_PATH,
                              String OUTPUT_PATH) throws IOException {
        if(FRAME_PATH == null) throw new IllegalArgumentException("FRAME_PATH가 null입니다.");
        if(frameId < 1 || frameId > 4) throw new IllegalArgumentException("FramdId의 범위는 1~4입니다.");

        // 프레임에 따라 이미지 개수 결정
        int imageCount = OFFSET[frameId - 1].length;
        if (imageFiles.size() != imageCount) {
            throw new IllegalArgumentException("이미지 파일의 개수가 프레임에 맞지 않습니다. : " + imageFiles.size() + "/" + imageCount);
        }

        try {
            // 프레임 이미지 로드 (loadFrameImage - 캐싱된 이미지 사용)
            BufferedImage baseImage = loadFrameImage(FRAME_PATH, frameId);
            BufferedImage secondBaseImage = frameId != 4 ? baseImage : loadFrameImage(FRAME_PATH, frameId);

            // Graphics2D 객체 생성
            Graphics2D frame = baseImage.createGraphics();

            // 프레임에 들어갈 이미지 그리기
            for (int i = 0; i < imageCount; i++) {
                BufferedImage image = ImageIO.read(new File(UPLOAD_PATH + "group" + groupId + "/" + imageFiles.get(i)));
                frame.drawImage(image, OFFSET[frameId - 1][i][0], OFFSET[frameId - 1][i][1], null);
            }

            // 두 번째 프레임 이미지 덮어쓰기
            frame.drawImage(secondBaseImage, 0, 0, null);

            // 텍스트 추가 (frameId가 4가 아닌 경우)
            if (frameId != 4) {
                String currentDate = generateCurrentDate();
                addTextToFrame(frame, currentDate, frameId);
            }

            frame.dispose();

            // 파일명 생성
            String fileName = "group_" + groupId + "_final_" + date + "_" + timeStamp + ".png";

            // 최종 파일 생성 및 저장
            File outputFile = new File(OUTPUT_PATH + fileName);
            ImageIO.write(baseImage, EXT, outputFile);

            return fileName;
        } catch (Exception e) {
            throw new IOException("이미지 파일 읽기중 문제가 생겼습니다.:"+e);
        }
    }

    /** 프레임 이미지 로드
     * @param FRAME_PATH 프레임 경로
     * @param frameId 프레임 ID
     * @return BufferedImage 프레임 이미지
     */
    private BufferedImage loadFrameImage(String FRAME_PATH, int frameId) throws IOException {
        // 캐싱된 이미지가 있으면 반환
        if (frameImageCache.containsKey(frameId)) return frameImageCache.get(frameId);

        // 캐싱된 이미지가 없으면 로드
        BufferedImage frameImage;
        if (frameId != 4) {
            frameImage = ImageIO.read(new File(FRAME_PATH + frameId + "." + EXT));
        } else {
            String suffix = (new Date().getDate() != 1) ? "-1" : "-2";
            frameImage = ImageIO.read(new File(FRAME_PATH + frameId + suffix + "." + EXT));
        }

        frameImageCache.put(frameId, frameImage); // 캐싱
        return frameImage;
    }

    /** 프레임에 텍스트 추가
     * @param frame 프레임 이미지
     * @param text 추가할 텍스트
     * @param frameId 프레임 ID
     */
    private void addTextToFrame(Graphics2D frame, String text, int frameId) {
        int frameIdex = frameId - 1;

        Font font = new Font(FONTNAME, Font.PLAIN, FONT_SIZE_AT_FRAME[frameIdex]);

//        Rectangle textRect = getFontrect(text, font);
//        BufferedImage textImage = new BufferedImage(textRect.width, textRect.height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2d = getG2D(textImage);
//        g2d.setFont(font);
//        g2d.setColor(TEXT_COLOR[frameIdex]);
//        g2d.drawString(text, 0, g2d.getFontMetrics().getAscent());
//        g2d.dispose();
//        frame.drawImage(textImage, TEXT_OFFSET[frameIdex][0], TEXT_OFFSET[frameIdex][1], null);

        Rectangle textRect = getFontrect(text, font);
        frame.setFont(font);
        frame.setColor(TEXT_COLOR[frameIdex]);
        frame.drawString(text, TEXT_OFFSET[frameIdex][0], TEXT_OFFSET[frameIdex][1] + textRect.height);
    }

    /**
     * 파일을 업로드 해서 저장된 경로 리스트를 반환하는 서비스
     * @param UPLOAD_PATH
     * @param groupId
     * @param files
     * @return FileNames
     */
    public List<String> FileUpload(String UPLOAD_PATH, Integer groupId,List<MultipartFile> files){
        List<String> FileNames = new ArrayList<>();

        Path uploadPath = Paths.get(UPLOAD_PATH);

        // uploadPath에 해당하는 위치가 없거나 파일이라면
        if (!Files.exists(uploadPath) || !Files.isDirectory(uploadPath)) {
            // 폴더 생성 및 검사
            Path groupPath = uploadPath.resolve(groupId.toString());
            if (new File(groupPath.toString()).mkdirs()) {
                System.out.println("Directory created. : " + groupPath);
            }
        }

        //파일명 생성기 객체 생성
        FileNameGenerator fileNameGenerator = new FileNameGenerator();

        // 파일명 생성에 사용될 index
        int index = 0;

        // 파일명 생성에 사용될 현재 날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = sdf.format(new Date());
        String timeStamp = date.split("_")[1];
        date = date.split("_")[0];

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
                String filename = fileNameGenerator.generateOriginalFileName(groupId, index, file.getOriginalFilename(), date, timeStamp).getFileName();
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
