var app = angular.module("login",
    ['ngResource']);

app.factory('LoginApi', ['$resource', function ($resource) {

    var baseUrl = "/user"
    return $resource(baseUrl, {}, {
        login: {
            method: "POST",
            url: baseUrl + "/login",
        }
    });
}]);