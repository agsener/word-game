angular.module("game")
    .component("game", {
        templateUrl: "/app/template/game/game.html",
        controller: function ($scope, GameApi, LoginApi, $routeParams) {

            /*
            $scope.isMyTurn = false;



            $scope.letters = makeLetters("abcdefghijklmnopqrstuvwxyz");
            console.log($scope.letters);

            GameApi.word(function (responseWord) {
                $scope.secretWord = responseWord.letters;

                LoginApi.me(function (responseMe) {
                    console.log("username: " + responseMe.username);
                    console.log("turn: " + responseWord.turn);
                    if (responseWord.turn === 0 && responseMe.username === responseWord.sender.username) {
                        $scope.isMyTurn = true;
                    } else if (responseWord.turn === 1 && responseMe.username === responseWord.receiver.username) {
                        $scope.isMyTurn = true;
                    }
                    console.log("Kimin sirasi: " + $scope.isMyTurn);
                });


                console.log("turn: " + $scope.turn);
                console.log("secret word");
                console.log($scope.secretWord);
            })

            //Kullanici harflere tikladiginda, dogru tahmin yaparsa if()'e giriyor. Yanlis tahminde ise else'e giriyor ve sunucuya
            //GameDto post atiliyor. Sira diger kullaniciya geciyor.

*/

            $scope.selectWord = function (guess) {
                GameApi.select({id: $routeParams.id, letter: guess.name}, {}, function (response) {
                    $scope.secretWord = response.letters;
                });
            };

            var makeLetters = function (word) {
                return _.map(word.split(''), function (character) {
                    return {name: character, chosen: false};
                });
            };

            $scope.init = function () {
                $scope.letters = makeLetters("abcdefghijklmnopqrstuvwxyz");
                GameApi.game({id: $routeParams.id}, function (response) {
                    $scope.secretWord = response.letters;
                });
            };

            $scope.init();
        }
    });