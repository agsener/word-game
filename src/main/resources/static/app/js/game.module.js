var app = angular.module("game",
    ['ngResource']);

app.factory('GameApi', ['$resource', function ($resource) {

    var baseUrl = "/lobby";
    return $resource(baseUrl, {}, {
        onlinePlayers: {
            method: "POST",
            url: baseUrl + "/players"
        },
        gameRequest: {
            method: "POST",
            url: "/game/game-request"
        },
        reject: {
            method: "POST",
            url: "/game/reject"
        },
        accept: {
            method: "POST",
            url: "/game/accept"
        },
        word: {
            method: "GET",
            url: "/game/word",
        },
    });
}]);