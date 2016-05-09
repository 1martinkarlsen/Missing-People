'use strict';

/* Place your Global Services in this File */

// Demonstrate how to register services
angular.module('missingPeople.controllers', [])
    
.controller('AppController', function(AuthService, UserService) {
    var self = this;
    
    self.getCurrentUser = function() {
        return UserService.getCurrentUser();
    };
    
    self.logout = function() {
        AuthService.logout();
    };
});