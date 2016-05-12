'use strict';
        
angular.module('missingPeople.home', ['ngRoute', 'ngFileUpload'])

.config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/home', {
    templateUrl: 'app/pages/home/home.html',
            controller: 'HomeController',
            controllerAs: 'ctrl'
    });
}])

.directive("fileread", [function () {
    return {
        scope: {
            fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                var reader = new FileReader();
                reader.onload = function (loadEvent) {
                    scope.$apply(function () {
                        scope.fileread = loadEvent.target.result;
                    });
                };
                reader.readAsDataURL(changeEvent.target.files[0]);
            });
        }
    };
}])

.controller('HomeController', function ($scope, $http, UserService) {

    var self = this;
    
    $scope.uploadme = {};
    
    if (UserService.getCurrentUser().isLogged) {
        self.missingList = getAllMissingPeople();
    }

    function getAllMissingPeople() {
        $http.get("api/missing/all").success(function (res) {

            console.log(res);
            //res.Photo = res.Photo.substr(8);
            self.missingList = res;
        }).error(function (res) {
            console.log("ERROR getting list");
            console.log(res);
        });
    };
        
    self.createSearch = function (credentials) {

        //fileUpload.uploadFileToUrl(file);
        
        var file = $scope.uploadme;
        var d = new Date(credentials.dateOfMissing);
        credentials.dateOfMissing = d.toISOString();
        
        //var fd = new FormData();
        //fd.append('file', file);
        //fd.append('data', credentials);
        
        credentials.file = file;
        
        console.log(credentials.file);
        
        $http.post("api/missing/create", credentials).success(function(res) {
            
            console.log(res);
            getAllMissingPeople();
            $('#createMissingPersonModal').modal('hide');
            
        }).error(function(res) {
            
            console.log("ERROR CREATING SEARCH!");
            console.log(res);
        });
    };
    
    function uploadFile(file) {
        file.upload =  $upload.upload({
            url: 'api/missing/create',
            method: 'POST',
            headers: {
                'Content-Type': 'multipart/form-data'
            },
            file: file
        });
    }
});