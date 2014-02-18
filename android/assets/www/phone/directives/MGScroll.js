(function(window, angular, undefined) {
    "use strict";

    angular.module("mgApp").directive("mgscroll", function() {
        return {
            replace: true,
            restrict: 'E',
            transclude: true,
            templateUrl: "views/MGScroll.html",
            link: function($scope, element, attr) {
                var scroller = null;


                setTimeout(function() {
                    scroller = new IScroll(element[0], {
                        mouseWheel: true,
                        hScroll: false
                        // useTransform: false
                    });
                    element[0].addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
                }, 100);



                $scope.$watch(function() {
                    if(scroller) {
                        scroller.refresh();
                    }
                });

            }
        };
    });

})(window, window.angular);
