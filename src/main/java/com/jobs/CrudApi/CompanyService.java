package com.jobs.CrudApi;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyDtoMapper companyDtoMapper;
    private final CompanyJobOfferDtoMapper companyJobOfferDtoMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyDtoMapper companyDtoMapper, CompanyJobOfferDtoMapper companyJobOfferDtoMapper) {
        this.companyRepository = companyRepository;
        this.companyDtoMapper = companyDtoMapper;
        this.companyJobOfferDtoMapper = companyJobOfferDtoMapper;
    }

    // Metoda getCompanyById() zwraca pojedynczy obiekt reprezentujący firmę,
    // natomiast metoda getJobOffersByCompanyId() zwraca listę ofert dla firmy o wskazanym id.
    // Jeżeli firma o wskazanym id nie istnieje, to zwrócona będzie pusta lista.

    Optional<CompanyDto> getCompanyById(Long id) {
        return companyRepository.findById(id).map(companyDtoMapper::map);
    }

    List<CompanyJobOfferDto> getJobOffersByCompanyId (Long companyId) {
        return companyRepository.findById(companyId)
                .map(Company::getJobOffers)
                .orElse(Collections.emptyList())
                .stream()
                .map(companyJobOfferDtoMapper::map)
                .toList();
    }
}
