"use strict";

var MessagesTypes = MessagesTypes || {};

MessagesTypes.MESSAGE = 1;
MessagesTypes.USERJOIN = 2;
MessagesTypes.USERLEAVE = 3;
MessagesTypes.BATTLESTART = 4;

var accountId = document.getElementById("accountId").textContent;
var userName = document.getElementById("userName").textContent;
var userLevel = document.getElementById("userLevel").textContent;

function Message(type, accountId, userName, userLevel, message) {
    this.type = type;
    this.accountId = accountId;
    this.userName = userName;
    this.userLevel = userLevel;
    this.message = message;
    this.timeSent = new Date();
}

function addPlayerToList(accountId, userName, userLevel) {
    var list = document.getElementById("playerList");
    var li = document.createElement('li');
    li.setAttribute("id", "player " + accountId);
    li.innerHTML = "name: " + userName + ", level: " + userLevel;
    list.appendChild(li);
}

function removePlayerFromList(accountId) {
    var list = document.getElementById("playerList");
    var li = document.getElementById("player " + accountId);
    list.removeChild(li);
}

var leaveBattle = function() {
    websocket.sendMessage(new Message(MessagesTypes.USERLEAVE, accountId, userName, userLevel, ""));
    window.location = "leaveBattle.do";
};

var startBattle = function() {
    // отключить все кнопки "старт" кроме одной
    // подготовка к битве (раздача карт) должна заканчиваться раньше чем переход на поле боя
    // - ввести задержку (не надежно)
    // - когда все готово => battle.start = true, считывать из servletContext в chatEndpoint и только тогда рассылать сообщения

    websocket.sendMessage(new Message(MessagesTypes.BATTLESTART, accountId, userName, userLevel, ""));
    //window.location = "startBattle.do"
};


var handler = {

    onopen: function() {
        $('#message').keydown(function (evt) {
            if (evt.keyCode == 13) {
                websocket.sendMessage(new Message(MessagesTypes.MESSAGE, accountId, userName, userLevel, $('#message').val()));
                $('#message').val('');
            }
        });

        websocket.sendMessage(new Message(MessagesTypes.USERJOIN, accountId, userName, userLevel, ""));
    },

    getEndpointName: function() {
        return '/prebattle/' + document.getElementById("battleId").textContent;
    },

    processMessage: function(message) {

        var resultMessage = "<br\>" + message.userName;

        if (message.type == MessagesTypes.MESSAGE){
            resultMessage = resultMessage + ": " + message.message;
        } else if (message.type == MessagesTypes.USERJOIN){
            if (message.userName != userName) {
                addPlayerToList(message.accountId, message.userName, message.userLevel);
            }
            resultMessage = resultMessage + " joined battle"
        } else if (message.type == MessagesTypes.USERLEAVE){
            removePlayerFromList(message.accountId);
            resultMessage = resultMessage + " leaved battle"
        } else if (message.type == MessagesTypes.BATTLESTART){
            window.location = "battlefield.jsp";
            resultMessage = "battle start!"
        } else {
            // unknown message
        }

        var chatLog = document.getElementById('chat');

        chatLog.innerHTML += resultMessage;

        //var p = document.createElement('p');
        ////p.style.wordWrap = 'break-word';
        //p.innerHTML = resultMessage;
        //chatLog.appendChild(p);
        //while (chatLog.childNodes.length > 15) {
        //    chatLog.removeChild(chatLog.firstChild);
        //}
        //chatLog.scrollTop = chatLog.scrollHeight;
    }
};






