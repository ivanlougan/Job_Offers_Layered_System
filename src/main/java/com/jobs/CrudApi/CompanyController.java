package com.jobs.CrudApi;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// W kontrolerze wykorzystujemy klasę CompanyService do udostępnienia danych na temat firmy oraz jej ofert pracy.
// Metoda getCompanyOffers() ma dosyć rozbudowany typ zwracany, ponieważ mamy tutaj do czynienia z ciekawą sytuacją.
// Z założenia zwraca ona listę ofert pracy w danej firmie. Kolekcja może być pusta, więc zwrócenie jej z kodem 200 jest jak najbardziej ok.
// Gorzej jeżeli próbujemy pobrać oferty pracy nieistniejącej firmy. W takiej sytuacji powinniśmy zwrócić kod 404.
// Z tego powodu tak złożony typ zwracany ResponseEntity<List<CompanyJobOfferDto>>.

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/{id}")
    ResponseEntity<CompanyDto> getCompanyById(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/offers")
    ResponseEntity<List<CompanyJobOfferDto>> getCompanyOffers(@PathVariable Long id) {
        if (companyService.getCompanyById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyService.getJobOffersByCompanyId(id));
    }
}
