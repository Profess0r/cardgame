"use strict";

var MessagesTypes = MessagesTypes || {};

MessagesTypes.MESSAGE = 1;
MessagesTypes.USERJOIN = 2;
MessagesTypes.USERLEAVE = 3;
MessagesTypes.PLAYERTURN = 5;
MessagesTypes.TURNEND = 6;
MessagesTypes.BATTLEEND = 7;

var accountId = document.getElementById("accountId").textContent;
var userName = document.getElementById("userName").textContent;

var playerDefeated = false;

function Message(type, accountId, userName, message, card, target) {
    this.type = type;
    this.accountId = accountId;
    this.userName = userName;
    this.message = message;
    this.card = card;
    this.target = target;
    this.timeSent = new Date();
}

//todo: leave battle
var leaveBattle = function() {
    websocket.sendMessage(new Message(MessagesTypes.USERLEAVE, accountId, userName, ""));
    window.location = "leaveBattle.do";
};

var pullFromDrop = function() {
    usedCard = "drop";
    makeTurn();
};


var refreshBattlefield = function() {
    var xmlhttp;
    if (window.XMLHttpRequest) {
        //code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else {
        //code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            document.getElementById("battlefield").innerHTML=xmlhttp.responseText;
        }
    };
    xmlhttp.open("GET","battlefieldRefresh.jsp",false);
    xmlhttp.send();
};


var handler = {

    onopen: function() {
        $('#message').keydown(function (evt) {
            if (evt.keyCode == 13) {
                websocket.sendMessage(new Message(MessagesTypes.MESSAGE, accountId, userName, $('#message').val()));
                $('#message').val('');
            }
        });

        websocket.sendMessage(new Message(MessagesTypes.USERJOIN, accountId, userName, "", "", ""));
    },

    getEndpointName: function() {
        return '/battle/' + document.getElementById("battleId").textContent;
    },

    processMessage: function(message) {

        var resultMessage = "";
        var turnLog = "";

        if (message.type == MessagesTypes.MESSAGE){
            resultMessage = "\n" + message.userName + ": " + message.message;
        } else if (message.type == MessagesTypes.TURNEND){
            console.log("turn end message received");
            refreshBattlefield();

            if (!playerDefeated) {
                if (!checkPlayerDefeat()){
                    refreshScripts();
                } else {
                    endBattle();
                }
            }

            // todo: messages for battle log ...
            turnLog = "\n" + message.message;
        } else if (message.type == MessagesTypes.BATTLEEND){
            // todo: battle end

            if (!playerDefeated) {
                if (!checkPlayerDefeat()){
                    winBattle();
                } else {
                    endBattle();
                }
            }

            turnLog = "\nbattle ended!";
            // вывести результаты и кнопку "покинуть битву" => leaveBattle.do
        } else if (message.type == MessagesTypes.USERLEAVE){
            refreshBattlefield();

            resultMessage = "\n" + message.userName + ": leaved battle";
        } else {
            // unknown (invalid type) message
        }

        var chatLog = document.getElementById('chat');
        chatLog.innerHTML += resultMessage;

        var battleLog = document.getElementById('log');
        battleLog.innerHTML += turnLog;

    }

};


var usedCard;
var turnTarget;
var cardsInHand;
var targets;
var timeoutId;

function refreshScripts() {

    // нужно будет сделать отображение таймера
    timeoutId = setTimeout("makeTurn()",90000);

    usedCard = "undefined";
    turnTarget = "undefined";

    cardsInHand = document.getElementsByClassName('cardInHand');
    targets = document.getElementsByClassName('target');

    console.log(cardsInHand);
    console.log(targets);

    cardListenersOn();
    document.getElementById("pullFromDrop").disabled = false;
}

function cardListenersOn() {
    console.log(cardsInHand.length);
    for (var i = 0; i < cardsInHand.length; i++) {
        cardsInHand[i].addEventListener('click', setCard, false);
        console.log("listener added for " + cardsInHand[i]);
    }
    console.log("cardListenersOn");
}

function cardListenersOff() {
    for (var i = 0; i < cardsInHand.length; i++) {
        cardsInHand[i].removeEventListener('click', setCard, false);
    }
    console.log("cardListenersOff");
}

function targetListenersOn() {
    for (var i = 0; i < targets.length; i++) {
        targets[i].addEventListener('click', setTarget, false);
    }
    console.log("targetListenersOn");
}

function targetListenersOff() {
    for (var i = 0; i < targets.length; i++) {
        targets[i].removeEventListener('click', setTarget, false);
    }
    console.log("targetListenersOff");
}

function setCard() {
    usedCard = this.getAttribute('id');
    console.log(usedCard);
    // похоже, здесь должна быть проверка возможных целей и включение только возможных слушателей
    // или ставить проверку на самих слушателях
    cardListenersOff();
    targetListenersOn();
}

function setTarget() {
    turnTarget = this.getAttribute('id');
    console.log(turnTarget);
    targetListenersOff();
    makeTurn();

    // это неудавшаяся попытка предотвратить указание недопустимой цели
    // на данный момент можно указать любую цель, но если она недопустима, то ход равнозначен "do nothing"
    //if (${me.cardsInHand[usedCardIndex].target == true}) {
    //    turnTarget = this.getAttribute('id');
    //    console.log(turnTarget);
    //    targetListenersOff();
    //    makeTurn();
    //    } else {
    //    alert("not valid target");
    //    }
}

function makeTurn()
{
    cardListenersOff();
    targetListenersOff();
    document.getElementById("pullFromDrop").disabled = true;

    clearTimeout(timeoutId);
    websocket.sendMessage(new Message(MessagesTypes.PLAYERTURN, accountId, userName, "", usedCard, turnTarget));
    //window.location.href="makeTurn.do?card=" + usedCard + "&target=" + turnTarget;
}

function checkPlayerDefeat() {
    var health = document.getElementById("playerHealth").textContent;
    playerDefeated = health < 1;
    return playerDefeated;
}

function endBattle() {
    document.getElementById("endTurn").disabled = true;
    var exp = document.getElementById("exp").textContent;
    alert("You was defeated.\nYou got " + exp + " experience.\nYou got " + (exp-exp%10)/10 + " money.");
}

function winBattle() {
    document.getElementById("endTurn").disabled = true;
    var exp = document.getElementById("exp").textContent;
    cardListenersOff();
    targetListenersOff();
    clearTimeout(timeoutId);
    alert("You won battle!\nYou got " + exp + " experience.\nYou got " + (exp-exp%10)/10 + " money.");
}



refreshBattlefield();
refreshScripts();






