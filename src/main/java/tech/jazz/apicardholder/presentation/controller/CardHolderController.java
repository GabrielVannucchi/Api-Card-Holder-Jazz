package tech.jazz.apicardholder.presentation.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tech.jazz.apicardholder.applicationservice.cardholderservice.CreateCardHolderService;
import tech.jazz.apicardholder.applicationservice.cardholderservice.CreateCardService;
import tech.jazz.apicardholder.applicationservice.cardholderservice.SearchCardHolderService;
import tech.jazz.apicardholder.applicationservice.cardholderservice.SearchCardService;
import tech.jazz.apicardholder.presentation.dto.CardHolderRequest;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;
import tech.jazz.apicardholder.presentation.dto.CardRequest;
import tech.jazz.apicardholder.presentation.dto.CardResponse;

@RestController
@RequestMapping("v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {
    private final CreateCardHolderService createCardHolderService;
    private final SearchCardHolderService searchCardHolderService;
    private final CreateCardService createCardService;
    private final SearchCardService searchCardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardHolderResponse createCardHolder(@Valid @RequestBody CardHolderRequest cardHolderRequest) {
        return createCardHolderService.createCardHolder(cardHolderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardHolderResponse> listAllCardHolder(@RequestParam(required = false) String status) {
        return searchCardHolderService.listAll(status);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardHolderResponse findCardHolder(@PathVariable UUID id) {
        return searchCardHolderService.findCardHolder(id);
    }

    @PostMapping("{cardHolderId}/cards")
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse createCard(@PathVariable UUID cardHolderId,
                                   @RequestBody CardRequest cardRequest) {
        return createCardService.createCard(cardHolderId, cardRequest);
    }

    @GetMapping("{cardHolderId}/cards")
    @ResponseStatus(HttpStatus.OK)
    public List<CardResponse> listAllCards(@PathVariable UUID cardHolderId) {
        return searchCardService.listAllByCardHolder(cardHolderId);
    }

    @GetMapping("{cardHolderId}/cards/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CardResponse findCardById(@PathVariable UUID cardHolderId,
                                     @PathVariable UUID id) {
        return searchCardService.findById(cardHolderId, id);
    }
}
