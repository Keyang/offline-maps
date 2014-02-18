(function(window, angular, undefined) {
    "use strict";


    angular.module("mgApp").directive("workorderlistitem", function() {
        return {
            restrict: 'E',
            transclude: true,
            templateUrl: "views/WorkOrderListItem.html"
        };
    });

})(window, window.angular);
