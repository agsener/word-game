var app = angular.module("websocket",
    []);

app.factory('WebsocketClient', ["$q", "$timeout", function ($q, $timeout) {

        var socket = new SockJS('/word-socket');
        var stompClient = Stomp.over(socket);
        stompClient.debug = null;

        var deferred = $q.defer();

        stompClient.connect({}, function (frame) {
            deferred.resolve();
            // $timeout 5sn bir send heart beat.
        });

        function subscribe(topic, callback){
           deferred.promise.then(function(){
                stompClient.subscribe(topic, function (msg) {
                    var response = JSON.parse(msg.body);
                    callback(response);
                });
            });
        }

        return {
            subs: subscribe
        }
}]);