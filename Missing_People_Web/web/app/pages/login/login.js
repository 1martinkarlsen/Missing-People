'use strict';

angular.module('missingPeople.login', ['ngRoute'])

.config(['$routeProvider', function ($routeProvider) {        
    $routeProvider.when('/login', {
        templateUrl: 'app/pages/login/login.html',
        controller: 'LoginController',
        controllerAs: 'ctrl'
    });
}])

.controller('LoginController', function($timeout, $location, AuthService) {
    var self = this;
    
    self.login = function(credentials) {
        // Resetting the login_alert box
        $("#login_alert").removeClass("alert-success");
        $("#login_alert").removeClass("alert-warning");
        $("#login_alert").removeClass("alert-danger");
        $("#login_alert").html("");
        $("#login_alert").addClass("hidden");
        
        AuthService.login(credentials).then(function(res) {
            
            $("#login_alert").removeClass("hidden");
            $("#login_alert").addClass("alert-success");
            $("#login_alert").html("Log ind lykkedes");
            
            //Will set a short delay before automatically redirecting
            $timeout(function () {
                //This did not work i dunno why??                    
                $location.url("/landingpage");
            }, 1500);
            
        }, function(data) {
            
            $("#login_alert").removeClass("hidden");
            $('#login_alert').addClass("alert-danger");
            $('#login_alert').html("<b>"+ data.data.httpError +"</b><br />"+ data.data.message);
        });
    };
});