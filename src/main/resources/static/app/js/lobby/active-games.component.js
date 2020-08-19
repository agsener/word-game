angular.module("lobby")
    .component("activeGames", {
        templateUrl: "/app/template/lobby/activeGames.html",
        controller: function($scope, GameApi, WebsocketClient){

            $scope.init = function(){
                WebsocketClient.subs('/topic/active-games', function(response){
                        $scope.activeGames = response;
                        $scope.$apply();
                });
            }

            $scope.init();
        }

    })