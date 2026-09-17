package gr.hua.citizen_service.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final int MINIMUM_RETIREMENT_AGE = 62;
    private static final int MINIMUM_YEARS_OF_SERVICE = 15;

    @Autowired
    private PensionApplicationRepository pensionApplicationRepository;

    @GetMapping("/my")
    public List<PensionApplication> getMyApplications(Authentication authentication) {
        return pensionApplicationRepository.findByCitizenUsername(authentication.getName());
    }

    @PostMapping
    public ResponseEntity<?> submitApplication(@RequestBody PensionApplication pensionApplication, Authentication authentication) {
        if (pensionApplication.getBirthDate() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Απαιτείται ημερομηνία γέννησης."));
        if (pensionApplication.getYearsOfService() == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Απαιτούνται τα έτη εργασίας."));

        int applicantAge = Period.between(pensionApplication.getBirthDate(), LocalDate.now()).getYears();
        if (applicantAge < MINIMUM_RETIREMENT_AGE) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Ο πολίτης πρέπει να έχει συμπληρώσει τουλάχιστον " + MINIMUM_RETIREMENT_AGE + " έτη ηλικίας (τρέχουσα ηλικία: " + applicantAge + ")."));
        if (pensionApplication.getYearsOfService() < MINIMUM_YEARS_OF_SERVICE) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Απαιτούνται τουλάχιστον " + MINIMUM_YEARS_OF_SERVICE + " έτη εργασίας (δηλωμένα έτη: " + pensionApplication.getYearsOfService() + ")."));

        pensionApplication.setCitizenUsername(authentication.getName());
        var saved = pensionApplicationRepository.save(pensionApplication);
        return ResponseEntity.ok(saved);
    }
}