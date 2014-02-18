(function(window, angular, undefined) {
    "use strict";

    angular.module("mgApp").directive("mgdialog", function() {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,
            scope: {
                result: "=",
                title: "@"
            },
            templateUrl: "views/MGCompleteDialog.html",
            controller: function($scope, dataManager) {
                $scope.result.value = null;

                $scope.main = [
                    {
                        val: 0,
                        text: "Permanently Repaired"
                    }
                ];

                $scope.sub = [
                    {
                        val: 1,
                        text: "out of time"
                    },
                    {
                        val: 2,
                        text: "materials required"
                    },
                    {
                        val: 3,
                        text: "made safe - third party"
                    },
                    {
                        val: 4,
                        text: "made safe as instructed"
                    },
                    {
                        val: 5,
                        text: "other issues"
                    }
                ];
            }
        };
    });

})(window, window.angular);
