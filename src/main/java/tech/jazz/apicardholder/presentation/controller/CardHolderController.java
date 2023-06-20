package tech.jazz.apicardholder.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import tech.jazz.apicardholder.infrastructure.domain.CardHolder;

public class CardHolderController {
    @PostMapping
    public CardHolder createCardHolder(){
        return null;
    }
}
