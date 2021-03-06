'use strict';

angular.module('regCartApp').factory('loginInterceptor', function ($q, $injector, $window, $rootScope) {
    return {
        'response' : function(response) {
            /*
            Currently, when the session expires any http request (including REST calls) is being redirected
            to the login page which responds with an HTTP status of 200 (making it unrecognizable as an
            error. To account for this, we will look for the login page to come back in the response...
            if it does we will broadcast the sessionExpired event for the controller to handle.
             */
            var responseData=response.data;
            if (typeof responseData !== 'object' && responseData.indexOf('Kuali Student Login') > -1) {
                console.log('Informing user that session has expired...');
                $rootScope.$broadcast('sessionExpired');
                return $q.reject(response);
            } else {
                return response;
            }
        },
        'responseError': function (rejection) {
            if (rejection.status === 0) {
                //For failed connections, try to log in again and refresh the page
                console.log('Failed to execute request - trying to login');
                var LoginService = $injector.get('LoginService');
                //This should be removed for production. In the future, we should handle logouts in a user-friendly way.
                LoginService.logOnAsAdmin().query({userId: 'student1', password: 'student1'}, function () {
                    //After logging in, reload the page.
                    console.log('Logged in, reloading page.');
                    $window.location.reload();
                }, function () {
                    //After logging in, reload the page.
                    console.log('Not Logged in, reloading page.');
                    $window.location.reload();
                });
            }
            return $q.reject(rejection);
        }
    };
});
