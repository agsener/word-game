var app = angular.module("lobby",
    ['ngResource']);

app.factory('GameApi', ['$resource', function ($resource) {

    var baseUrl = "/lobby"
    return $resource(baseUrl, {}, {
        onlinePlayers: {
            method: "POST",
            url: baseUrl + "/players"
        }
    });
}]);