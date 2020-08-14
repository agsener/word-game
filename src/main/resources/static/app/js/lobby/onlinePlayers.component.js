angular.module("lobby")
    .component("onlinePlayers", {
        templateUrl: "/app/template/lobby/onlinePlayers.html",
        controller: function ($scope, GameApi) {

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
                        $scope.users = response
                        ;
                        $scope.$apply();
                        console.log(response);
                    });
                });
            };

            $scope.init = function () {
                $scope.registerTopic();
                $scope.users = [];
            };

            $scope.init();
        }
    })