angular.module("game")
    .component("game", {
        templateUrl: "/app/template/game/game.html",
        controller: function ($scope, GameApi, LoginApi, WebsocketClient, $routeParams, $location) {

            var whosTurn = null;

            $scope.selectWord = function (guess) {
                GameApi.select({id: $routeParams.id, letter: guess.name, turn: whosTurn}, {}, function (response) {
                    $scope.secretWord = response.letters;
                    LoginApi.me(function (rsp) {
                        if (response.winner !== null && response.winner === rsp.username) {
                            //kazandi
                            //tekrar oynamak istermisin
                            alert("Kazandin: " + response.winner);
                            console.log("Kazandin: " + response.winner);
                            Swal.fire({
                                title: 'Winner!',
                                text: ' Are play again?',
                                icon: 'info',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: 'Yes, do it again!'
                            }).then(function (result) {
                                if (result.value) {
                                    alert("Tekrar oy ist")
                                } else {
                                    $location.path("/lobby");
                                }
                            });
                        } else if (rsp.username === response.whosTurn) {
                            $scope.isMyTurn = true;
                            $scope.whosTurn = response.whosTurn;
                        } else {
                            $scope.isMyTurn = false;
                            $scope.whosTurn = response.whosTurn;
                        }
                    })
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
                    $scope.letters = response.alphabet;
                    LoginApi.me(function (rsp) {
                        if (response.winner !== null && response.winner !== rsp.username) {
                            //kaybetti
                            //tekrar oynamak istermisin
                            alert("Kaybettin: " + rsp.username);
                            console.log("Kaybettin: " + rsp.username);
                            Swal.fire({
                                title: 'Loser!',
                                text: ' Are play again?',
                                icon: 'warning',
                                showCancelButton: true,
                                confirmButtonColor: '#3085d6',
                                cancelButtonColor: '#d33',
                                confirmButtonText: 'Yes, try again!'
                            }).then(function (result) {
                                if (result.value) {
                                    alert("Tekrar oy ist")
                                } else {
                                    $location.path("/lobby");
                                }
                            });
                        }
                        else if (rsp.username === response.whosTurn) {
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = true;
                        } else {
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = false;
                        }
                    })
                })
                $scope.letters = makeLetters("abcdefghijklmnopqrstuvwxyz");
                GameApi.game({id: $routeParams.id}, function (response) {
                    $scope.secretWord = response.letters;
                    $scope.letters = response.alphabet;
                    LoginApi.me(function (rsp) {
                        if (rsp.username === response.whosTurn) {
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = true;
                        } else {
                            whosTurn = response.whosTurn;
                            $scope.isMyTurn = false;
                        }
                    })
                });
            };

            $scope.init();
        }
    });