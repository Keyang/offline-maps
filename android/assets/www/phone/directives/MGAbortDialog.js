(function(window, angular, undefined) {
    "use strict";

    var abortReasons = [
        {
            val: 0,
            text: "unable to make safe"
        },
        {
            val: 1,
            text: "out of time"
        },
        {
            val: 2,
            text: "not found"
        },
        {
            val: 3,
            text: "materials required"
        },
        {
            val: 4,
            text: "already completed"
        },
        {
            val: 5,
            text: "made safe - 3rd party"
        },
        {
            val: 6,
            text: "obstructed"
        },
        {
            val: 7,
            text: "diverted to new task"
        },
        {
            val: 8,
            text: "other issues"
        }
    ],
    tms = [
        {
            val: 9,
            text: "Give and Take"
        },
        {
            val: 10,
            text: "Priority Signs"
        },
        {
            val: 11,
            text: "Stop/Go Boards"
        },
        {
            val: 12,
            text: "2-Way Signals"
        },
        {
            val: 13,
            text: "Multi Way Signals"
        },
        {
            val: 14,
            text: "Stop Works sign"
        },
        {
            val: 15,
            text: "Lane Closure"
        },
        {
            val: 16,
            text: "Road Closure"
        }
    ];

    angular.module("mgApp").directive("mgabortdialog", function() {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,
            scope: {
                result: "=",
                title: "@"
            },
            templateUrl: "views/MGAbortDialog.html",
            controller: function($scope, dataManager) {
                $scope.result.trafficManagement = null;
                $scope.result.value = null;

                $scope.tms = tms;
                $scope.abortReasons = abortReasons;
            }
        };
    });

})(window, window.angular);
