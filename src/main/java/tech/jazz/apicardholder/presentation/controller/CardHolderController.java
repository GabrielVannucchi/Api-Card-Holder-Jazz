package tech.jazz.apicardholder.presentation.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tech.jazz.apicardholder.applicationservice.cardholderservice.CreateCardHolderService;
import tech.jazz.apicardholder.applicationservice.cardholderservice.SearchCardHolderService;
import tech.jazz.apicardholder.infrastructure.repository.entity.BankAccountEntity;
import tech.jazz.apicardholder.presentation.dto.CardHolderRequest;
import tech.jazz.apicardholder.presentation.dto.CardHolderResponse;

@RestController
@RequestMapping("card-holders/v1.0")
@RequiredArgsConstructor
public class CardHolderController {
    private final CreateCardHolderService createCardHolderService;
    private final SearchCardHolderService searchCardHolderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardHolderResponse createCardHolder(@RequestBody CardHolderRequest cardHolderRequest) {
        final CardHolderResponse request = createCardHolderService.createCardHolder(cardHolderRequest);
        return request;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardHolderResponse> listAll() {
        final List<CardHolderResponse> response = searchCardHolderService.listAll();
        return response;
    }

    @GetMapping("test")
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccountEntity> listAccount() {
        final List<BankAccountEntity> response = searchCardHolderService.listAccount();
        return response;
    }

}
