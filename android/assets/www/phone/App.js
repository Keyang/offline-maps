(function(window, angular, undefined) {
  'use strict';

  angular.module('mgApp', [])
    .config(function ($routeProvider, $compileProvider) {

      document.addEventListener("deviceready", function() {
        console.log("deviceready");

        document.addEventListener("backbutton", function(e) {
          console.log("backbutton");
          e.preventDefault();
        }, false);

      }, false);

      $compileProvider.urlSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|tel):/);

      $routeProvider
        .when('/Login', {
          templateUrl: 'views/Login.html',
          controller: 'Login'
        })

        .when('/VehicleCheck', {
          templateUrl: 'views/VehicleCheck.html',
          controller: 'Plain'
        })
        .when('/VehicleCheckFail', {
          templateUrl: 'views/VehicleCheckFail.html',
          controller: 'Plain'
        })

        .when('/WorkOrderList', {
          templateUrl: 'views/WorkOrderList.html',
          controller: 'WorkOrderList'
        })

        .when('/WorkGang', {
          templateUrl: 'views/WorkGang.html',
          controller: 'Plain'
        })

        .when('/JobPack/:WONUM', {
          templateUrl: 'views/JobPack.html',
          controller: 'JobPack'
        })

        .when('/WorkOrder/:WONUM', {
          templateUrl: 'views/WorkOrder.html',
          controller: 'WorkOrder'
        })

        .when('/Aborted/:WONUM', {
          templateUrl: 'views/Aborted.html',
          controller: 'Plain'
        })

        .otherwise({
          redirectTo: '/Login'
        });

    }).run(function(navigation) {
      navigation.restore();
    });
})(window, window.angular);


