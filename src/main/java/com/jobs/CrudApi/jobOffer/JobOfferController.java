package com.jobs.CrudApi.jobOffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/offers")
public class JobOfferController {
    private final JobOfferService jobOfferService;
    private final ObjectMapper objectMapper;

    public JobOfferController(JobOfferService jobOfferService, ObjectMapper objectMapper) {
        this.jobOfferService = jobOfferService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{id}")
    ResponseEntity<JobOfferDto> getOfferById(@PathVariable Long id) {
        return jobOfferService.getOfferById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<JobOfferDto> seveOffer (@RequestBody JobOfferDto jobOfferDto) {
        JobOfferDto savedJobOffer = jobOfferService.saveOffer(jobOfferDto);
        URI savedJobOfferUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(savedJobOffer.getId())
                .toUri();
        return ResponseEntity.created(savedJobOfferUri).body(savedJobOffer);
    }




    // Metoda updateJobOffer() obsługuje żądania PATCH wysyłane pod adres /offers/{id}.
    // Jej parametrami jest identyfikator aktualizowanej oferty oraz obiekt JsonMergePatch z opisem zmian.
    // Typ JsonMergePatch pochodzi z dodanej na początku biblioteki json-patch. Jackson nie obsługuje domyślnie tego typu.
    //
    //Najpierw wyszukujemy ofertę pracy, którą chcemy zaktualizować.
    // Jeżeli oferta o wskazanym id nie istnieje, to korzystając z metody orElseThrow() rzucany jest
    // wyjątek NoSuchElementException, który obsługujemy w osobnym bloku catch i zwracamy w takiej
    // sytuacji odpowiedź z kodem 404 Not Found.
    //
    //Metoda applyPatch() służy do tego, żeby zaaplikować zmiany otrzymane od klienta do obiektu pobranego z bazy danych.
    // ObjectMapper pozwala zamienić obiekt Javy na jego reprezentację JSON, która w Javie reprezentowana
    // jest jako JsonNode. Wywołanie metody apply() powoduje podmianę wartości pól w obiekcie jobOfferNode,
    // na takie, które przesłał do nas klient. Na końcu musimy ponownie zamienić obiekt z reprezentacji JSON na
    // JobOfferDto i ponownie wykorzystujemy do tego celu ObjectMappera.
    //
    //Po zaaplikowaniu zmian zaktualizowany obiekt przekazujemy do metody updateOffer()
    // w celu zapisania zmian w bazie danych.
    //
    //Podczas patchowania obiektu oraz zamianie obiekt z/do formatu JSON mogą wystąpić wyjątki.
    // Jeżeli takie wystąpią, to zwracamy do klienta odpowiedź z kodem 500 Internal Server Error.
    // Jeżeli wszystko się powiodło, to zwracamy odpowiedź z kodem 204 No Content.


    @PatchMapping("/{id}")
    ResponseEntity<?> updateJobOffer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        try {
            JobOfferDto jobOffer = jobOfferService.getOfferById(id).orElseThrow();
            JobOfferDto offerPatched = applyPatch (jobOffer, patch);
            jobOfferService.updateOffer(offerPatched);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.noContent().build();
    }

    private JobOfferDto applyPatch(JobOfferDto jobOffer, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode jobOfferNode = objectMapper.valueToTree(jobOffer);
        JsonNode jobOfferPatchedNode = patch.apply(jobOfferNode);
        return objectMapper.treeToValue(jobOfferPatchedNode, JobOfferDto.class);
    }


    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteJobOffer (@PathVariable Long id) {
        jobOfferService.deleteOffer(id);
        return ResponseEntity.noContent().build();

    }
}

