package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SpringBootApplication
public class Application {
    public static final String UPLOAD_DIR = "src/main/resources/public/uploads";

    public static void main(String[] args) {
        SpringApplication.run((Application.class));
        System.out.println("http://localhost:8080");
    }

    @GetMapping("/")
    public String uploadForm(Model model) {
        Path dir = Paths.get(UPLOAD_DIR);

        List<String> files = new ArrayList<>();
        try {
            files = Files.walk(dir, 1)
                    .map(path -> dir.relativize(path).toString())
                    .filter(path -> !path.equals(""))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("files", files);
        return "index";
    }

    @PostMapping("/")
    public String upload(
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String path = UPLOAD_DIR + "/" + file.getOriginalFilename();
            System.out.println("Copying file " + path);
            FileCopyUtils.copy(file.getBytes(), new File(path));
        } catch (IOException e) {
            System.out.println("Error uploading file.");
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
