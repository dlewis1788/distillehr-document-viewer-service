var distillehrApp = angular.module('distillehrApp', ['ngRoute']);

function distillehrRouteConfig($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'documents.html',
            controller: 'DocumentController'
        })
        .when('/retrieveDocumentList/cernerMillennium/person/:id', {
            templateUrl: 'documents.html',
            controller: 'DocumentController'
        });
}

distillehrApp.config(distillehrRouteConfig);

distillehrApp.controller('DocumentController', function($scope, $routeParams, $http) {
    $scope.id = $routeParams.id;
    $scope.username = $routeParams.username;
    $http.get('/documentViewer/retrieveDocumentList/cernerMillennium/person/' + $scope.id).success(function (response) {
        $scope.documentList = response;
    });
});
