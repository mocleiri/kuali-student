'use strict';

angular.module('regCartApp')
    .service('CartService', ['$resource', 'APP_URL', function CartService($resource, APP_URL) {
    this.getCart = function () {
//            return $resource(APP_URL + 'CourseRegistrationCartClientService/submitCart', {}, {
        return $resource('json/static-cart.json', {}, {
            query:{method:'GET', cache:false, isArray:false}
        });
    };
    this.getGradingOptions = function() {
        return $resource('json/new-static-cart-item.json', {}, {
            query:{method:'GET', cache:false, isArray:false}
        });
    };
    this.addCourseToCart = function () {
        return $resource(APP_URL + 'CourseRegistrationCartClientService/addCourseToCart', {}, {
            query:{method:'GET', cache:false, isArray:false}
        });
    };
    this.updateCartItem = function () {
//            return $resource(APP_URL + 'CourseRegistrationCartClientService/updateCartItem', {}, {
        return $resource('json/static-cart-update.json', {}, {
            query:{method:'GET', cache:false, isArray:false}
        });
    };
}]);
