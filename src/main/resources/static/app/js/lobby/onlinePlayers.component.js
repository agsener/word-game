angular.module("lobby")
    .component("onlinePlayers", {
        templateUrl: "/app/template/lobby/onlinePlayers.html",
        controller: function ($scope, $window, GameApi, LoginApi, WebsocketClient, $location) {

            $scope.newGameRequest = function (response) {
                switch (response.code) {
                    case 0:
                        Swal.fire({
                            title: 'New game request',
                            text: response.data.username + " wants to play.",
                            icon: 'warning',
                            showCancelButton: true,
                            confirmButtonColor: '#3085d6',
                            cancelButtonColor: '#d33',
                            confirmButtonText: 'Yes, I am in!'
                        }).then(function (result) {
                            if (result.value) {
                                GameApi.accept({username: response.data.username}, {}, function (response) {
                                    $location.path("/game/" + response.id);
                                });
                            } else {
                                GameApi.reject({username: response.data.username}, {});
                            }
                        });
                        $scope.$apply();
                        break;
                    case 100:
                        Swal.fire(response.data.username + " does not want to play");
                        break;
                    case 200:
                        console.log(response);
                        $location.path("/game/" + response.data.id);
                        break;
                }
            };

            $scope.gameRequest = function (user) {
                GameApi.gameRequest({username: user.username}, {}, function (response) {
                    Swal.fire('Please wait for response');
                });
            };

            $scope.init = function () {

                $scope.users = [];
                $scope.activeGames = [];
                WebsocketClient.subs('/topic/online-players', function (response) {
                    $scope.users = response;
                    $scope.$apply();
                });

                LoginApi.me(function (response) {
                    WebsocketClient.subs("/topic/game-request/" + response.username, function (response) {
                        $scope.newGameRequest(response);
                    });
                });
            };

            $scope.init();
        }
    })