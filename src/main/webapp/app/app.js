var TG_SCOPES = 'https://www.googleapis.com/auth/userinfo.email';
var TG_CLIENT_ID = '{YOUR_CLIENT_ID}.apps.googleusercontent.com';

$(document).foundation();

function initApi() {
    console.log('Init API');
    var apisToLoad;
    var loadCallback = function() {
      if (--apisToLoad == 0) {
        signin(true, userAuthed);
      }
    };
    apisToLoad = 2; // must match number of calls to gapi.client.load()
    apiRoot = '//' + window.location.host + '/_ah/api';
    gapi.client.load('poker', 'v1', loadCallback, apiRoot);
    gapi.client.load('oauth2', 'v2', loadCallback);
}

function signin(mode, authorizeCallback) {
    gapi.auth.authorize({client_id: TG_CLIENT_ID,
      scope: TG_SCOPES, immediate: mode},
      authorizeCallback);
}

function userAuthed() {
    var request =
        gapi.client.oauth2.userinfo.get().execute(function(resp) {
      if (!resp.code) {
        // User is signed in, call my Endpoint
        updateAngularModel();
      }
    });
}

function updateAngularModel() {/*$scope.$apply()*/}

angular.module('poker-app', [])
    .controller('PokerController', ['$scope', '$window', '$sce', function($scope, $window, $sce) {
        $scope.hello = 'Hello Angular';
        $scope.showTable = false;
        $scope.model = {
            hand : [
               {rank : '10', suit: '&hearts;', clazz: 'card rank-10 hearts', held: false},
               {rank : 'J',  suit: '&hearts;', clazz: 'card rank-j hearts', held: false},
               {rank : 'Q',  suit: '&hearts;', clazz: 'card rank-q hearts', held: false},
               {rank : 'K',  suit: '&hearts;', clazz: 'card rank-k hearts', held: false},
               {rank : 'A',  suit: '&hearts;', clazz: 'card rank-a hearts', held: false},
            ]
        }
        
        $scope.toggleShowTable = function() {
            $scope.showTable = !$scope.showTable;
        }
        
        $scope.toggleHold = function(cardNumber) {
            if ($scope.model.canHold) {
                gapi.client.poker.hold({'card' : cardNumber}).then(
                    function(resp) {
                        console.log(resp);
                        $scope.model.hand[cardNumber].held = !$scope.model.hand[cardNumber].held;
                        $scope.$apply();
                    },
                    function(reason) {
                        console.log(reason);
                    }
                );
            }
        }
        
        $scope.betOne = function() {
            if ($scope.model.canBet) {
                if ($scope.model.bet >= 5) {
                    $scope.model.bet = 1;
                } else if ($scope.model.credit - $scope.model.bet == 0) {
                    $scope.model.bet = 1;
                } else {
                    ++$scope.model.bet;
                }
                
                if ($scope.model.bet > 0) {
                    $scope.model.stepText = 'Deal';
                }
                gapi.client.poker.bet({'bet' : $scope.model.bet}).then(
                    function(resp) {
                        console.log(resp);
                        $scope.$apply();
                    },
                    function(reason) {
                        console.log(reason);
                    }
                );
            }
        }

        $scope.betMax = function() {
            if ($scope.model.canBet) {
                $scope.model.bet = ($scope.model.credit - 5) >= 0 ? 5 : $scope.model.credit;
                $scope.model.stepText = 'Deal';
                gapi.client.poker.bet({'bet' : $scope.model.bet}).execute(
                    function(resp) {
                        console.log(resp);
                    }
                );
            }
        }
        
        $scope.getHand = function() {
            if (($scope.model.step == 1 && $scope.model.bet > 0) || ($scope.model.step == 2) || ($scope.model.step == 3)) {
                gapi.client.poker.deal().then(
                    function(resp) {
                        $scope.model = resp.result;
                        $scope.model.canBet = ($scope.model.step == 1);
                        $scope.model.canHold = ($scope.model.step == 2);
                        $scope.resultClass = $scope.model.wonCombination >=0 ? 'alert' : 'regular';
                        $scope.uiReady = true;
                        $scope.$apply();
                        console.log(resp);
                    },
                    function(reason) {
                        console.log(resp);
                    }
                );
            }
        };
        
        $scope.resetCredit = function() {
            if ($scope.model.credit <= 0) {
                gapi.client.poker.reset().then(
                        function(resp) {
                            $scope.model.credit = 100;
                            $scope.$apply();
                            console.log(resp);
                        },
                        function(reason) {
                            console.log(resp);
                        });
            }
        }
        
        $scope.auth = function() {
            $scope.uiReady = false;
            gapi.auth.authorize({client_id: TG_CLIENT_ID, scope: TG_SCOPES, immediate: false},
                  function() {
                      var request = gapi.client.oauth2.userinfo.get().then(
                          function(resp) {
                              $scope.isLoggedIn = true;
                              $scope.currentUser = "Hi, " + resp.result.name;
                              $scope.$apply();
                              console.log(resp);
                          },
                          function(reson) {
                              console.log(resp);
                          }
                      );
                      gapi.client.poker.load().then(
                          function(resp) {
                            $scope.model = resp.result;
                            $scope.model.canBet = ($scope.model.step == 1);
                            $scope.model.canHold = ($scope.model.step == 2);
                            $scope.resultClass = $scope.model.wonCombination >=0 ? 'success' : 'alert';
                            $scope.uiReady = true;
                            $scope.$apply();
                            console.log(resp);
                          },
                          function(reason) {
                            console.log(resp);
                          }
                    );
    
                  })};
    }])
    .filter('toTrustedHtml', ['$sce', function($sce){
        return function(text) {
            return $sce.trustAsHtml(text);
        };
    }]);

