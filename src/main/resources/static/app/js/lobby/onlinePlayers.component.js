angular.module("lobby")
    .component("onlinePlayers", {
        templateUrl: "/app/template/lobby/onlinePlayers.html",
        controller: function ($scope, $window, GameApi) {

            var stompClient = null;

            $scope.registerTopic = function () {
                var socket = new SockJS('/word-socket');
                stompClient = Stomp.over(socket);
                stompClient.debug = null
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame);

                    //Bir kullanici login oldugunda diger login olmus olan kullanicilara
                    //login olan kullanicinin bilgileri "msg" ile gonderiliyor
                    stompClient.subscribe('/topic/online-players', function (msg) {
                        var response = JSON.parse(msg.body);
                        $scope.users = response;
                        $scope.$apply();
                        console.log(response);
                    });
                });
            };

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

            $scope.init = function () {
                $scope.registerTopic();
                $scope.gameRequestControl();
                $scope.users = [];
            };

            $scope.init();
        }
    })