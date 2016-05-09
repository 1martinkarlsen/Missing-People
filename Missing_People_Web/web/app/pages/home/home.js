'use strict';

angular.module('missingPeople.home', ['ngRoute', 'ngFileUpload'])

.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/home', {
            templateUrl: 'app/pages/home/home.html',
            controller: 'HomeController',
            controllerAs: 'ctrl'
        });
    }])

.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}])

.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(){
        })
        .error(function(){
        });
    };
}])


.controller('HomeController', function ($scope, $http, UserService) {

    var self = this;

    if (UserService.getCurrentUser().isLogged) {

        self.missingList = getAllMissingPeople();

    }

    function getAllMissingPeople() {
        $http.get("api/missing/all").success(function (res) {

            console.log(res);
            self.missingList = res;

        }).error(function (res) {
            console.log("ERROR getting list");
            console.log(res);
        });
    };


    self.createSearch = function (credentials) {

        var file = $scope.myFile;

        var d = new Date(credentials.dateOfMissing);
        credentials.dateOfMissing = d.toISOString();
        
        var fd = new FormData();
        fd.append('file', angular.toJson(file));
        fd.append('data', angular.toJson(credentials));

        console.log(fd[0]);

        $http.post("api/missing/create", fd).success(function(res) {
            
            console.log(res);
            getAllMissingPeople();
            $('#createMissingPersonModal').modal('hide');
            
        }).error(function(res) {
            
            console.log("ERROR CREATING SEARCH!");
            console.log(res);
        });
    };
});