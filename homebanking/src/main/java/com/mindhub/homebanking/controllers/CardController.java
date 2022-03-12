package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientService clientService;
    @Autowired
    ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
                                             @RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());



        List<Card> cardList = currentClient.getCards().stream().filter(card -> {
            return card.getType() == cardType;
        }).collect(toList());

        List<Card> cardlistTrue = cardList.stream().filter(card -> {
            return card.isCardStatus();
        }).collect(toList());

        if (cardlistTrue.size() >= 3 ) {
            return new ResponseEntity<>("Forbidden, you can only register 3 Cards", HttpStatus.FORBIDDEN);
        }
        //GENERATE CARD NUMBER
        String cardNumber = CardUtils.getCardNumber();

        int cvv = CardUtils.getCVV();

        LocalDate thruDate = LocalDate.now();
        LocalDate fromDate = thruDate.plusYears(5);

        Card newCard = new Card(currentClient.getFirstName() + " " + currentClient.getFirstName(), cardType, cardColor, cardNumber, cvv, thruDate, fromDate, currentClient,true);
        cardRepository.save(newCard);
        return new ResponseEntity<>("Card created!", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id){
        Card card = cardRepository.findById(id).orElse(null);
        card.setCardStatus(false);
        cardRepository.save(card);
        return new ResponseEntity<>("Card created!", HttpStatus.CREATED);
    }
}

//ELIMINA POR ID, LA ELIMINA COMPLETAMENTE
//    @DeleteMapping("/clients/current/cards/{id}")
//    public void deleteCard(@PathVariable Long id) {
//        cardRepository.deleteById(id);
//    }
//
//

//}
