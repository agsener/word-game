angular.module("game")
    .component("game", {
        templateUrl: "/app/template/game/game.html",
        controller: function ($scope, GameApi, LoginApi, WebsocketClient, $routeParams) {

            var whosTurn = null;

            $scope.selectWord = function (guess) {
                GameApi.select({id: $routeParams.id, letter: guess.name, turn: whosTurn}, {}, function (response) {
                    console.log("kimin sirasi:" + whosTurn)
                    $scope.secretWord = response.letters;
                    LoginApi.me(function(rsp){
                        if (rsp.username === response.whosTurn){
                            $scope.whosTurn = response.whosTurn;
                            $scope.isMyTurn = true;
                        }
                        else{
                            $scope.isMyTurn = false;
                            $scope.whosTurn = response.whosTurn;
                        }
                    })
                    console.log("letters: " + response.letters[0].name);
                });
            };

            var makeLetters = function (word) {
                return _.map(word.split(''), function (character) {
                    return {name: character, chosen: false};
                });
            };

            $scope.init = function () {
                WebsocketClient.subs("/topic/game/" + $routeParams.id, function (response) {
                    $scope.secretWord = response.letters;
                    LoginApi.me(function(rsp){
                        if (rsp.username === response.whosTurn){
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = true;
                        }
                        else{
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = false;
                        }
                    })
                })
                $scope.letters = makeLetters("abcdefghijklmnopqrstuvwxyz");
                GameApi.game({id: $routeParams.id}, function (response) {
                    $scope.secretWord = response.letters;
                    LoginApi.me(function(rsp){
                        if (rsp.username === response.whosTurn){
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = true;
                        }
                        else{
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = false;
                        }
                    })
                });
            };

            $scope.init();
        }
    });