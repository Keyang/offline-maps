(function(window, angular, undefined) {
    "use strict";

    angular.module("mgApp").controller("Login", function($scope, $routeParams, workOrderManager, attachments) {

        window.plugins.version.getVersionName(function(versionCode) {
            console.log("Got version name: " + versionCode);
            $scope.$apply(function() {
                $scope.version = versionCode;
            });
        }, function(err) {
            console.log("Failed to get version name: " + err);
        });

    });

})(window, angular);