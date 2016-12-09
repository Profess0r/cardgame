"use strict";

var addCard = function(cardId) {
    var cardToRemove = document.getElementById("availableCards").getElementsByClassName(cardId)[0];

    cardToRemove.getElementsByClassName("addCardButton")[0].setAttribute("hidden", "hidden");
    cardToRemove.getElementsByClassName("removeCardButton")[0].removeAttribute("hidden");

    document.getElementById("availableCards").removeChild(cardToRemove);
    document.getElementById("deckCards").appendChild(cardToRemove);
};

var removeCard = function(cardId) {
    var cardToAdd = document.getElementById("deckCards").getElementsByClassName(cardId)[0];

    cardToAdd.getElementsByClassName("removeCardButton")[0].setAttribute("hidden", "hidden");
    cardToAdd.getElementsByClassName("addCardButton")[0].removeAttribute("hidden");

    document.getElementById("deckCards").removeChild(cardToAdd);
    document.getElementById("availableCards").appendChild(cardToAdd);
};

var saveChanges = function() {

    var name = $('#name').val();
    var active = document.getElementById("active").checked;

    var cardsInDeck = document.getElementById("deckCards").getElementsByClassName("card");

    var cards = "";
    console.log(active);

    for (var i = 0; i < cardsInDeck.length; i++) {
        cards += cardsInDeck[i].getAttribute("class").split(" ")[1] + " ";
    }

    window.location.href = "saveDeck.do?name=" + name + "&active=" + active + "&cards=" + cards;

};

