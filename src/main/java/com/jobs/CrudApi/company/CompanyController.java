package com.jobs.CrudApi.company;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    ResponseEntity<CompanyDto> savedCompany(@RequestBody CompanyDto company) {
        CompanyDto savedCompany = companyService.saveCompany(company);
        URI savedCompanyUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedCompany.getId())
                .toUri();
        return ResponseEntity.created(savedCompanyUri).body(savedCompany);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceCompany(@PathVariable Long id, @RequestBody CompanyDto company) {
        return companyService.replaceCompany(id, company)
                .map( c -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}
