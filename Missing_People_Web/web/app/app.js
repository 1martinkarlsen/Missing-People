'use strict';

angular.module('missingPeople', [
    'ngRoute',
    'ngStorage',
    'ngFileUpload',
    'ui.bootstrap',
    'mdr.datepicker',
    
    'missingPeople.controllers',
    'missingPeople.services',
    
    'missingPeople.login',
    'missingPeople.home'
])

.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $routeProvider.otherwise({redirectTo: '/home'});
    
    // Html5Mode sættes for at fjerne unødvendige "#" i URL, for at gøre URL pænere.
    //$locationProvider.html5Mode(true);
}]);