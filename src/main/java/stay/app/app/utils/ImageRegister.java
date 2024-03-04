package stay.app.app.utils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageRegister {
    public List<String> CreateImages(MultipartFile[] image) throws IOException {

        List<String> list = new ArrayList<>();

        for (MultipartFile image1 : image) {
            // 1. 파일 저장 경로 설정 : 실제 서비스되는 위치(프로젝트 외부에 저장)
            String uploadPath = "C:\\Users\\peter\\OneDrive\\바탕 화면\\localproject\\post\\src\\main\\java\\com\\example\\post\\imageUpload\\";
            // 2. 원본 파일 이름 알아오기
            String originalFileName = image1.getOriginalFilename();
            // 3. 파일 이름 중복되지 않게 이름 변경(서버에 저장할 이름) UUID 사용
            UUID uuid2 = UUID.randomUUID();
            String savedFileName = uuid2.toString() + "_" + originalFileName;
            // 4. 파일 생성
            java.io.File file1 = new File(uploadPath + savedFileName);
            // 5. 서버로 전송
            image1.transferTo(file1);
            // model로 저장
            list.add(uploadPath + savedFileName);
        }
        return list;
    }

    public void DeleteFile(String priorImage) throws Exception {
        try{
            if (priorImage != null) {
                List<String> test = List.of(priorImage.split(","));
                for (String image : test) {
                    Files.delete(Path.of(image));
                }
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
