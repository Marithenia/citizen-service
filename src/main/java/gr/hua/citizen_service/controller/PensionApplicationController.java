package gr.hua.citizen_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.hua.citizen_service.model.PensionApplication;
import gr.hua.citizen_service.repository.PensionApplicationRepository;

@RestController
@RequestMapping("/applications")
public class PensionApplicationController {

    @Autowired
    private PensionApplicationRepository pensionApplicationRepository;

    @PostMapping
    public PensionApplication submitApplication(@RequestBody PensionApplication pensionApplication, Authentication authentication) {
        pensionApplication.setCitizenUsername(authentication.getName());
        return pensionApplicationRepository.save(pensionApplication);
    }

    @GetMapping("/my")
    public List<PensionApplication> getMyApplications(Authentication authentication) {
        return pensionApplicationRepository.findByCitizenUsername(authentication.getName());
    }
}