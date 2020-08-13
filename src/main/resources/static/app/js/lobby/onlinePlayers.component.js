angular.module("lobby")
    .component("onlinePlayers", {
        templateUrl: "/app/template/lobby/onlinePlayers.html",
        controller: function($scope, GameApi){

            var stompClient = null;

            $scope.registerTopic = function(){
                var socket = new SockJS('/word-socket');
                stompClient = Stomp.over(socket);
                stompClient.debug = null
                stompClient.connect({}, function (frame) {
                    console.log('Connected: ' + frame);
                    stompClient.subscribe('/topic/online-players', function (msg) {
                        console.log(msg);
                    });
                });
            };

            $scope.init = function(){
                $scope.registerTopic();
            };

            $scope.init();
        }
    })