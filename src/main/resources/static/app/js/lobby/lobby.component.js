angular.module("lobby")
    .component("lobbyPage", {
        templateUrl: "/app/template/lobby/lobby.html",
        controller: function($scope, GameApi){

            $scope.gameRequest = function () {
                console.log();
            }
        }
    })