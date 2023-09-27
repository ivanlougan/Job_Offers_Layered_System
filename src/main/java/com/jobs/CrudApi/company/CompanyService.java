package com.jobs.CrudApi.company;

import org.springframework.stereotype.Service;

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

    // Metoda save() przyjmuje obiekt typu CompanyDto, który jest zamieniany na encję.
    // Ta jest zapisywana w bazie, gdzie jest ustawiony jej identyfikator.
    // Po zapisie zamieniamy obiekt encji z powrotem na CompanyDto i zwracamy w wyniku.
    CompanyDto saveCompany (CompanyDto companyDto) {
        Company company = companyDtoMapper.map(companyDto);
        Company savedCompany = companyRepository.save(company);
        return companyDtoMapper.map(savedCompany);
    }


    Optional<CompanyDto> replaceCompany (Long companyId, CompanyDto companyDto) {
        if (!companyRepository.existsById(companyId)) {
            return Optional.empty();
        }
        companyDto.setId(companyId);
        Company companyToUpdate = companyDtoMapper.map(companyDto);
        Company updatedEntity = companyRepository.save(companyToUpdate);
        return Optional.of(companyDtoMapper.map(updatedEntity));
    }

    void deleteCompany (Long id) {
        companyRepository.deleteById(id);
    }
}
