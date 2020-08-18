angular.module("lobby")
    .component("onlinePlayers", {
        templateUrl: "/app/template/lobby/onlinePlayers.html",
        controller: function ($scope, $window, GameApi, LoginApi, $location) {

            var stompClient = null;
            var socket = null;

            $scope.createSocket = function () {
                socket = new SockJS('/word-socket');
            };

            $scope.createClient = function (callback) {
                stompClient = Stomp.over(socket);
                stompClient.debug = null
                stompClient.connect({}, function (frame) {
                    callback();
                    console.log('Connected: ' + frame);
                });
            };

            $scope.registerOnlinePlayers = function () {
                //Bir kullanici login oldugunda diger login olmus olan kullanicilara
                //login olan kullanicinin bilgileri "msg" ile gonderiliyor
                stompClient.subscribe('/topic/online-players', function (msg) {
                    var response = JSON.parse(msg.body);
                    $scope.users = response;
                    $scope.$apply();
                });
            };

            $scope.subscribeGameRequest = function (username) {
                stompClient.subscribe("/topic/game-request/" + username, function (msg) {
                    var response = JSON.parse(msg.body);
                    switch (response.code) {
                        case 0:
                            Swal.fire({
                                title: 'New game request',
                                text: response.username + " wants to play.",
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

                });
            };

            $scope.gameRequest = function (user) {
                GameApi.gameRequest({username: user.username}, {}, function (response) {
                    Swal.fire('Please wait for response');
                });
            };

            /*

            $scope.gameRequest = function (index) {
                GameApi.gameRequest($scope.users[index], function (response) {
                    if(response.code == 0){
                        alert("İstek göderildi cevap bekleniyor");
                    }
                })
                console.log("Cift tiklanince girdi")
                console.log(index);
            }

            $scope.gameRequestControl = function(){
                var socket = new SockJS('/word-socket');
                stompClient = Stomp.over(socket);
                stompClient.debug = null
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame);

                    //Kullaniciya gelen oyun isteklerinin oldugu web socket
                    stompClient.subscribe('/topic/game-request', function (msg) {
                        var response = JSON.parse(msg.body);
                        console.log("game response sender " + response.sender);
                        console.log("game response receiver " + response.receiver);
                        console.log("session: " + window.sessionStorage("LOGGEDIN_USER"));
                        $scope.$apply();
                    });
                });
            }


             */

            $scope.init = function () {
                $scope.createSocket();
                $scope.createClient(function () {
                    $scope.registerOnlinePlayers();
                    LoginApi.me(function (response) {
                        $scope.subscribeGameRequest(response.username);
                    });
                });


                // $scope.gameRequestControl();
                $scope.users = [];
            };

            $scope.init();
        }
    })