(function(window, angular, undefined) {


    angular.module("mgApp").factory("network", function($rootScope) {

        var states = {};
        states[Connection.UNKNOWN]  = 'Unknown connection';
        states[Connection.ETHERNET] = 'Ethernet connection';
        states[Connection.WIFI]     = 'WiFi connection';
        states[Connection.CELL_2G]  = 'Cell 2G connection';
        states[Connection.CELL_3G]  = 'Cell 3G connection';
        states[Connection.CELL_4G]  = 'Cell 4G connection';
        states[Connection.CELL]     = 'Cell generic connection';
        states[Connection.NONE]     = 'No network connection';


        function handleEvent(e) {
            console.log("network changed");
            console.log(navigator.connection.type);
            $rootScope.$broadcast("Network.changed", navigator.connection.type);
        }

        window.addEventListener("online", handleEvent, false);
        window.addEventListener("offline", handleEvent, false);

        handleEvent();

        return {
            getStatus: function() {

            }
        };

    });

})(window, window.angular);