(function(window, angular, undefined) {
    "use strict";

    angular.module("mgApp").directive("mgheader", function(network) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                title: "@title"
            },
            templateUrl: "views/MGHeader.html",
            controller: function($scope, $rootScope, attachments) {

                $scope.syncStatus = "none";

                function updateSyncStatus(status) {
                    switch(status) {
                        case attachments.NONE:
                            $scope.syncStatus = "none";
                            break;
                        case attachments.WAITING:
                            $scope.syncStatus = "waiting";
                            break;
                        case attachments.SYNCING:
                            $scope.syncStatus = "syncing";
                            break;
                    }
                }

                function updateNetworkStatus() {
                    var status = navigator.connection.type;
                    if(status === Connection.UNKNOWN || status === Connection.NONE) {
                        console.log("no network");
                        $scope.syncStatus = "failed";
                    }
                    else {
                        updateSyncStatus(attachments.getStatus());
                    }
                }
                updateNetworkStatus();

                $rootScope.$on("Network.changed", function(e, status) {
                    console.log("network changed event");
                    $scope.$apply(updateNetworkStatus);
                });

                $rootScope.$on("Attachments.statusChange", function(e, status) {

                    $scope.$apply(function() {
                        updateNetworkStatus();
                    });

                });

            }
        };
    });

})(window, window.angular);
