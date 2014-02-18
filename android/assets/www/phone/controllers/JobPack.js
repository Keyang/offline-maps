(function(window, angular, undefined) {
    "use strict";

    function safeApply(scope, fn) {
        var phase = scope.$root.$$phase;
        if(phase == '$apply' || phase == '$digest') {
            scope.$eval(fn);
        }
        else {
            scope.$apply(fn);
        }
    }

    angular.module("mgApp").controller("JobPack", function($scope, $routeParams, workOrderManager, attachments) {
        var id = $routeParams.WONUM;

        $scope.workOrder = workOrderManager.getWorkOrder(id);

        $scope.$on("WorkOrders.updated", function(e, workOrders, workOrderMap) {

            $scope.$apply(function() {
                $scope.workOrder = workOrderMap[id];
            });

        });

    });

})(window, angular);