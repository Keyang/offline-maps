(function(window, angular, undefined) {
    'use strict';

    var DIALOGS = {
        NONE: null,
        ABORT: "abort",
        COMPLETE: "complete"
    };

    function safeApply(scope, fn) {
        var phase = scope.$root.$$phase;
        if(phase == '$apply' || phase == '$digest') {
            scope.$eval(fn);
        }
        else {
            scope.$apply(fn);
        }
    }

    // TODO: these need to be got from the cloud.
    var abortReasons = [
        "unable to make safe",
        "out of time",
        "not found",
        "materials required",
        "already completed",
        "made safe - 3rd party",
        "obstructed",
        "diverted to new task",
        "other issues"
    ],
    tms = [
        "No parking cones required",
        "Give and Take",
        "Priority Signs",
        "Stop/Go Boards",
        "2-Way Signals",
        "Multi Way Signals",
        "Stop Works sign",
        "Lane Closure",
        "Road Closure"
    ],
    main = [
        "Permanently Repaired"
    ],
    sub = [
        "out of time",
        "materials required",
        "made safe - third party",
        "made safe as instructed",
        "other issues"
    ];


    angular.module('mgApp')
        .controller('WorkOrder', function ($scope, $routeParams, $timeout, workOrderManager, GPS) {
            function Dialog(data) {
                this.data = null;
                this.callback = null;
            }

            Dialog.prototype.open = function(cb) {
                this.opened = true;
                this.data = null;
                this.callback = cb;
            };
            Dialog.prototype.close = function() {
                if(this.opened) {
                    this.callback = null;
                    this.opened = false;
                }
            };

            Dialog.prototype.done = function() {
                var self = this;

                if(this.opened) {
                    var callback = this.callback,
                        data = this.data;

                    if(callback) {
                        $timeout(function() {
                            if(self.callback(self.data) !== false) {
                                self.close();
                            }
                        }, 0);
                    }
                }
            };

            var id = $routeParams.WONUM,
                workOrder = null;

            $scope.DIALOGS = DIALOGS;

            $scope.dialog = DIALOGS.NONE;
            $scope.content = "description";
            $scope.tms = tms;
            $scope.abortReasons = abortReasons;
            $scope.main = main;
            $scope.sub = sub;

            var gpsDialog = $scope.gpsDialog = new Dialog(),
                abortDialog = $scope.abortDialog = new Dialog(),
                completeDialog = $scope.completeDialog = new Dialog();

            workOrder = $scope.workOrder = workOrderManager.getWorkOrder(id);

            $scope.$on("WorkOrders.updated", function(e, workOrders, workOrderMap) {

                $scope.$apply(function() {
                    $scope.workOrder = workOrder = workOrderMap[id];
                });

            });

            $scope.$on("Attachments.updated", function() {
                $scope.$digest();
            });

            $scope.complete = function() {

                if(workOrder.statusValue() === workOrder.COMPLETE - 1) {

                    completeDialog.open(function(result) {
                        if(!result) return false;

                        takePhoto("Work End Photo", function(err) {
                            if(!err) {
                                $scope.updateStatus("COMPLETE", {
                                    result: result
                                });
                                safeApply($scope);
                            }
                        });
                    });
                }
            };

            $scope.abort = function() {

                abortDialog.open(function(reason) {
                    if(!reason) return false;

                    takePhoto("Work Abort photo", function(err) {
                        console.log("-----------------------------------------");
                        console.log(err);
                        console.log("-----------------------------------------");
                        if(!err) {
                            $scope.updateStatus("ABORT", {
                                reason: reason
                            }, function() {
                                document.location.href = "#/WorkOrderList";
                            });
                        }
                    });

                    return true;
                });
            };

            $scope.updateStatus = function(status, data, cb) {
                if(workOrder.isValidNextStatus(status)) {
                    if(!data) {
                        data = {};
                    }

                    if(status !== "RISK_ASSESS") {
                        console.log("getting location information");
                        getLocation(function(geo) {
                            data.geo = geo;

                            $scope.$apply(function() {
                                workOrder.addStatus(status, data);
                                if(cb) cb();
                            });
                        });
                    }
                    else {
                        workOrder.addStatus(status);
                        if(cb) cb();
                    }
                }
            };


            function getLocation(cb) {
                var waitTimeout = null,
                    gpsRequest = new GPS.Request();

                function wait() {
                    // We need gps coordinates to continue.
                    $scope.needsGPS = true;
                    waitTimeout = $timeout(function() {
                        $scope.needsGPS = false;

                        gpsDialog.open(function(data) {
                            if(data === "continue") {
                                finish();
                            }
                            else if(data === "retry") {
                                $scope.$apply(wait);
                            }
                        });

                    }, 20 * 1000);
                }

                function finish() {
                    $scope.$apply(function() {
                        $scope.needsGPS = false;
                    });
                    $timeout.cancel(waitTimeout);
                    gpsRequest.cancel();
                    return cb(gpsRequest.coords);
                }
                wait();

                gpsRequest.waitFor({
                    minAccuracy: 20,
                    maxAge: 60 * 1000
                }, finish);
            }


            $scope.checkRiskAssess = function() {
                if(workOrder.statusValue() === workOrder.RISK_ASSESS - 1) {
                    $scope.content = "risk-assess";
                    safeApply($scope);
                }
            };

            $scope.riskAssessCancelled = function() {
                $scope.content = "description";
                safeApply($scope);
            };

            $scope.riskAssessComplete = function() {
                $scope.content = "description";
                $scope.updateStatus("RISK_ASSESS");
                safeApply($scope);
            };

            $scope.takePhoto = function() {

                if(workOrder.statusValue() === workOrder.TAKE_PHOTOS ||
                   workOrder.statusValue() === workOrder.TAKE_PHOTOS - 1) {

                    var numPhotos = workOrder.photos.length,
                        title = numPhotos ? "Photo " + numPhotos : "Work Start Photo";

                    takePhoto(title, function(err) {
                        if(err) {
                            return;
                        }
                        safeApply($scope);
                    });
                }
            };

            function takePhoto(title, cb) {

                navigator.camera.getPicture(function(path) {
                    if(path) {
                        attachments.create(path, function(err, attachment) {
                            if(err) {
                                console.log("Failed to create attachment", err);
                                return;
                            }

                            workOrder.addPhoto(attachment, title);

                            if(cb) setTimeout(cb, 0);
                        });
                    }
                    else {
                        setTimeout(function() {
                            cb(new Error("No uri was found"));
                        }, 0);
                    }
                }, function() {
                    if(cb) {
                        setTimeout(function() {
                            cb(new Error("User didn't take photo"));
                        }, 0);
                    }
                }, {
                    quality: 75,
                    targetWidth: 720,
                    targetHeight: 1280,
                    encodingType: Camera.EncodingType.PNG,
                    sourceType: Camera.PictureSourceType.CAMERA,
                    destinationType: Camera.DestinationType.FILE_URI,
                    correctOrientation: true
                });
            }



        });

})(window, window.angular);
