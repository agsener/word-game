var app = angular.module("websocket",
    []);

app.factory('WebsocketClient', ["$q", function ($q) {

        var socket = new SockJS('/word-socket');
        var stompClient = Stomp.over(socket);
        stompClient.debug = null;

        var deferred = $q.defer();

        stompClient.connect({}, function (frame) {
            deferred.resolve();
            console.log('Connected: ' + frame);
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