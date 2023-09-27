package com.jobs.CrudApi.jobOffer;


import com.jobs.CrudApi.company.Company;
import com.jobs.CrudApi.company.CompanyRepository;
import org.springframework.stereotype.Service;

// Rola mapperów sprowadza się najczęściej tylko do prostego przepisania
// pól jednego obiektu do innego. W naszym przypadku struktura ulega
// jeszcze spłaszczeniu i pozbywamy się zagnieżdżonego obiektu reprezentującego firmę.


@Service
public class JobOfferDtoMapper {
    private final CompanyRepository companyRepository;

    public JobOfferDtoMapper(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // metoda map to mechanizm, który przekształci obiekt encji na obiekt DTO

    JobOfferDto map(JobOffer jobOffer) {
        JobOfferDto dto = new JobOfferDto();
        dto.setId(jobOffer.getId());
        dto.setTitle(jobOffer.getTitle());
        dto.setDescription(jobOffer.getDescription());
        dto.setRequirements(jobOffer.getRequirements());
        dto.setDuties(jobOffer.getDuties());
        dto.setLocation(jobOffer.getLocation());
        dto.setMinSalary(jobOffer.getMinSalary());
        dto.setMaxSalary(jobOffer.getMaxSalary());
        dto.setDateAdded(jobOffer.getDateAdded());
        dto.setCompanyId(jobOffer.getCompany().getId());
        dto.setCompanyName(jobOffer.getCompany().getName());
        return dto;
    }


    // dodajemy metodę, która pozwoli przekształcić obiekt JobOfferDto na JobOffer.
    // Oferta pracy ma powiązany obiekt reprezentujący firmę, dlatego do mappera
    // wstrzykniemy repozytorium CompanyRepository, które musi być publiczne.

    // Nowa metoda map() przypisuje do nowo tworzonej oferty pracy obiekt reprezentujący firmę,
    // znaleziony na podstawie jej identyfikatora, który był przesłany w obiekcie DTO.
    // Nie ustawiamy w niej wartości pola dateAdded, ponieważ informacji tej z założenia
    // nie ma w obiekcie DTO, który ktoś do nas wyśle. To nie klient decyduje kiedy
    // obiekt zostanie utworzony, tylko robi to aplikacja, albo baza danych.

    JobOffer map(JobOfferDto dto) {
        JobOffer jobOffer = new JobOffer();
        jobOffer.setId(dto.getId());
        jobOffer.setTitle(dto.getTitle());
        jobOffer.setDescription(dto.getDescription());
        jobOffer.setRequirements(dto.getRequirements());
        jobOffer.setDuties(dto.getDuties());
        jobOffer.setLocation(dto.getLocation());
        jobOffer.setMinSalary(dto.getMinSalary());
        jobOffer.setMaxSalary(dto.getMaxSalary());
        jobOffer.setDateAdded(dto.getDateAdded());
        Company company = companyRepository.findById(dto.getCompanyId()).orElseThrow();
        jobOffer.setCompany(company);
        return jobOffer;
    }
}
