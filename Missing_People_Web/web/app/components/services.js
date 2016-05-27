'use strict';

/* Place your Global Services in this File */

// Demonstrate how to register services
angular.module('missingPeople.services', ['ngStorage'])
    
.service('UserService', function($localStorage) {
    var self = this;
    
    self.currentUser = {
        id: '',
        isLogged: false,
        email: '',
        roles: [],
        firstname: '',
        lastname: '',
        token: ''
    };
    
    self.create = function(id, email, firstname, lastname, roles, token) {
        self.currentUser.id = id;
        self.currentUser.isLogged = true;
        self.currentUser.email = email;
        self.currentUser.roles = roles;
        self.currentUser.firstname = firstname;
        self.currentUser.lastname = lastname;
        self.currentUser.token = token;
        
        $localStorage.user = self.currentUser;
    };
    self.destroy = function() {
        self.currentUser.id = '';
        self.currentUser.isLogged = false;
        self.currentUser.email = '';
        self.currentUser.roles = '';
        self.currentUser.firstname = '';
        self.currentUser.lastname = '';
        self.currentUser.token = '';
        
        delete $localStorage.user;
    };
    
    self.setCurrentUser = function(user) {
        self.currentUser = user;
    };
    
    self.getCurrentUser = function() {
        if(self.currentUser.isLogged == false) {
            if($localStorage.user != null) {
                self.currentUser = $localStorage.user;
            }
        }
        
        return self.currentUser;
    };
})

.factory('AuthService', function($http, UserService) {
    var authService = {};
    
    function url_base64_decode(str) {
        var output = str.replace('-', '+').replace('_', '/');
        switch (output.length % 4) {
            case 0:
                break;
            case 2:
                output += '==';
                break;
            case 3:
                output += '=';
                break;
            default:
                throw 'Illegal base64url string!';
        }
        return window.atob(output); //polifyll https://github.com/davidchambers/Base64.js    
    };
    
    authService.login = function(credentials) {
        return $http.post('api/user/login', credentials)
            .success(function (data) {
                
                var encodedToken = data.token.split('.')[1];
                var profile = JSON.parse(url_base64_decode(encodedToken));
                
                UserService.create(data.id, data.email, data.firstname, data.lastname, profile.roles.split(','), data.token);

                //Will set a short delay before automatically redirecting
//                $timeout(function () {
//                    //This did not work i dunno why??                    
//                    $location.url("/landingpage");
//                }, 1500);
                return data;
            })
            .error(function (data) {
            });
    };
    
    authService.logout = function() {
        UserService.destroy();
    };
    
    authService.isAuthenticated = function() {
        return !!UserService.email;
    };
    
    authService.isAuthorized = function(authorizedRoles) {
        if(!angular.isArray(authorizedRoles)) {
            authorizedRoles = [authorizedRoles];
        }
        return (authService.isAuthenticated() && authorizedRoles.indexOf(UserService.roles) !== -1);
    };
    
    return authService;
});