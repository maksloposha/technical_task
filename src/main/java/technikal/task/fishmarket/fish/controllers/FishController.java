package technikal.task.fishmarket.fish.controllers;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import technikal.task.fishmarket.fish.models.Fish;
import technikal.task.fishmarket.fish.models.FishDto;
import technikal.task.fishmarket.fish.services.FishRepository;

@Controller
@RequestMapping("/fish")
public class FishController {

    @Autowired
    private FishRepository repo;

    @GetMapping({"", "/"})
    public String showFishList(Model model) {
        List<Fish> fishlist = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("fishlist", fishlist);
        return "index";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        FishDto fishDto = new FishDto();
        model.addAttribute("fishDto", fishDto);
        return "createFish";
    }

    @GetMapping("/delete")
    public String deleteFish(@RequestParam int id) {

        try {

            Fish fish = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid fish Id:" + id));
            fish.getImageFileNames().forEach(this::deleteFishImage);
            repo.delete(fish);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/fish";
    }

    private void deleteFishImage(String path) {
        Path imagePath = Paths.get("public/images/" + path);
        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create")
    public String addFish(@Valid @ModelAttribute FishDto fishDto, BindingResult result) {

        if (fishDto.getImageFiles() == null || fishDto.getImageFiles().isEmpty()) {
            result.addError(new FieldError("fishDto", "imageFiles", "Потрібно завантажити хоча б одне фото"));
        }

        for (MultipartFile file : fishDto.getImageFiles()) {
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                result.addError(new FieldError("fishDto", "imageFiles", "Можна завантажувати лише зображення"));
            }
        }

        if (result.hasErrors()) {
            return "createFish";
        }

        Date catchDate = new Date();
        List<String> storedFileNames = new ArrayList<>();

        String uploadDir = "public/images/";
        Path uploadPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile image : fishDto.getImageFiles()) {

                if (image.isEmpty()) continue;

                String storageFileName = catchDate.getTime() + "_" + image.getOriginalFilename();

                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream,
                            Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                storedFileNames.add(storageFileName);
            }

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        Fish fish = new Fish();
        fish.setCatchDate(catchDate);
        fish.setName(fishDto.getName());
        fish.setPrice(fishDto.getPrice());
        fish.setImageFileNames(storedFileNames);

        repo.save(fish);

        return "redirect:/fish";
    }

}
