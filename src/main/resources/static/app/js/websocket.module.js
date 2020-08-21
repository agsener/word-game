var app = angular.module("websocket",
    []);

app.factory('WebsocketClient', ["$q", "$timeout", function ($q, $timeout) {

    var user;
    var socket = new SockJS('/word-socket');
    var stompClient = Stomp.over(socket);
    stompClient.debug = null;

    var deferred = $q.defer();


    stompClient.connect({}, function (frame) {
        deferred.resolve();
        startBeating();
    });

    function startBeating() {
        if(user) {
            stompClient.send("/app/beat", {}, JSON.stringify({'id': user.id}));
        }
        $timeout(function () {
            startBeating();
        }, 5000);
    }

    function subscribe(topic, callback) {
        deferred.promise.then(function () {
            stompClient.subscribe(topic, function (msg) {
                var response = JSON.parse(msg.body);
                callback(response);
            });
        });
    }

    function setUser(u) {
        user = u;
    }

    function getUser(u) {
        return user;
    }


    return {
        subs: subscribe,
        setUser: setUser,
        getUser: getUser
    }
}]);