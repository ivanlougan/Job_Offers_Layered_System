package com.jobs.CrudApi.company;

import com.jobs.CrudApi.jobOffer.JobOffer;
import org.springframework.stereotype.Service;

@Service
public class CompanyJobOfferDtoMapper {

    CompanyJobOfferDto map (JobOffer jobOffer) {
        CompanyJobOfferDto dto = new CompanyJobOfferDto();
        dto.setId(jobOffer.getId());
        dto.setTitle(jobOffer.getTitle());
        dto.setLocation(jobOffer.getLocation());
        dto.setMinSalary(jobOffer.getMinSalary());
        dto.setMaxSalary(jobOffer.getMaxSalary());
        return dto;
    }
}
