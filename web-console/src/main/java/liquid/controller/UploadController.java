package liquid.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by redbrick9 on 6/11/14.
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(Model model) {
        return "upload/main";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                FileOutputStream fos = new FileOutputStream("/tmp/" + file.getOriginalFilename());
                fos.write(bytes);
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/upload";
    }
}
