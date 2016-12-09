"use strict";

var websocket = websocket || {};

websocket.sendMessage = function(message){
    if (websocket.socket) {
        websocket.socket.send(JSON.stringify(message));
    }
};

websocket.connect = function(host){
    if ('WebSocket' in window) {
        websocket.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        websocket.socket = new MozWebSocket(host);
    } else {
        console.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    websocket.socket.onopen = function(){
        console.log("Info: connection opened");
        handler.onopen();
    };

    websocket.socket.onclose = function(){
        console.log("Info: connection closed");
    };

    websocket.socket.onmessage = function(message){
        handler.processMessage(JSON.parse(message.data));
    };
};

websocket.initialize = function(){
    var endpoint = handler.getEndpointName();
    var context = document.location.pathname.split('/')[1]; //todo...
    if (context.indexOf(".") !== -1) {
        context = "";
    }

    if (window.location.protocol == 'http:'){
        websocket.connect('ws://' + window.location.host + '/' + context + endpoint);
    } else {
        websocket.connect('wss://' + window.location.host + '/' + context + endpoint);
    }
};

websocket.initialize();
