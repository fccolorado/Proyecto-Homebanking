package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

//    //GENERATE CVV
//    int min = 100;
//    int max = 999;
//
//    public int getCvvNumber(int max, int min) {
//        return (int) ((Math.random() * (max - min)) + min);
//    }

    //    //GENERATE CARD NUMBER
//    int min2 = 1000;
//    int max2 = 9999;
//
//    public int getRandomCardNumber(int max2, int min2) {
//        return (int) ((Math.random() * (max2 - min2)) + min2);
//    }
//
//    public String getStringCardNumber() {
//        int randomCardNumber = getRandomCardNumber(max2, min2);
//        return String.valueOf(randomCardNumber);
//    }
//
//    public String getCardNumber() {
//        String cardNumber = "";
//        for (int i = 0; i < 4; i++) {
//            String aux = getStringCardNumber();
//            cardNumber += ("-" + aux);
//        }
//        return cardNumber.substring(1);
//    }
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(Authentication authentication,
                                             @RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());

        List<Card> cardList = currentClient.getCards().stream().filter(card -> {
            return card.getType() == cardType;
        }).collect(toList());

        List<Card> cardListDelete = cardList.stream().filter(card -> {
            return card.isCardStatus();
        }).collect(toList());

        if (cardListDelete.size() == 3 ) {
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
