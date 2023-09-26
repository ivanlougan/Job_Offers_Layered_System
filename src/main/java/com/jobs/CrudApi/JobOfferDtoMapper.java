package com.jobs.CrudApi;


import org.springframework.stereotype.Service;

// Rola mapperów sprowadza się najczęściej tylko do prostego przepisania
// pól jednego obiektu do innego. W naszym przypadku struktura ulega
// jeszcze spłaszczeniu i pozbywamy się zagnieżdżonego obiektu reprezentującego firmę.


@Service
public class JobOfferDtoMapper {

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
}
