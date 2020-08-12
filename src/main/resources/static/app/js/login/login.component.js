angular.module("login")
    .component("loginPage",{
        templateUrl: "/app/template/login/login.html",
        controller: function($scope, LoginApi, $location){

            $scope.loginFunction = function(){
                $scope.user = {
                    username: $scope.username,
                    password: $scope.password
                };
                LoginApi.login($scope.user,function(response){
                    if (response.code === 0){
                        $location.path("/lobby");
                    }else{
                        //TODO: Kullanici login olamadiginda, ne olacaginin yazilmasi gerekiyor
                        switch (response.code) {
                            default:
                            case 50:
                                return;
                        }
                    }

                })
            }


        }
    })