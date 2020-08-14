var app = angular.module("app",
    [
        "ngRoute",
        "login",
        "lobby",
        "game"
    ]);

app.config(["$routeProvider", "$locationProvider",
    function ($routeProvider, $locationProvider) {

        $locationProvider.html5Mode({enabled: true});

        $routeProvider
            .when("/", {
                template: "<login-page></login-page>"
            })
            .when("/lobby", {
                template: "<lobby-page></lobby-page>"
            })
            .when("/game/:id", {
                template: "<game></game>"
            })
            .otherwise({
                redirectTo: "/"
            });

    }]);
