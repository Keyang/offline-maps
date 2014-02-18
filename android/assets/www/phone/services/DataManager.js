(function(window, angular, undefined) {

    var datasetID = "WorkOrders";

    function errorHandler() {
        console.log(arguments);
    }

    angular.module('mgApp').factory('workOrderManager', function($rootScope, attachments) {

        var workOrders = [],
            workOrderMap = {};

        var sync = $fh.sync;

        sync.init({
            "sync_frequency": 5,
            "do_console_log" : true,
            // Notify of changes to the local data set
            "notify_delta_received": true,
            "notify_local_update_applied": true,

            // notify when pushing data failed
            "notify_remote_update_failed": true,
        });

        // Provide handler function for receiving notifications from sync service - e.g. data changed
        sync.notify(function(e) {
            switch(e.code) {
                case "local_update_applied":
                case "delta_received":
                    sync.doList(datasetID, refreshData);
                    break;
                case "remote_update_failed":
                    console.log("remote update failed");
                    break;
            }
        });

        window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;

        /**
         * Initialise the filesystem with the size specified
         * @param  {Integer} amount Amount in bytes to initialise.
         */
        function requestFileSystem(amount) {
            window.requestFileSystem(LocalFileSystem.PERSISTENT, amount, function(fs) {
                console.log("Alocated file system");
                sync.manage(datasetID, {});
                window.fileSystem = fs;
            }, function() {
                console.log("Failed to allocate file system");
            });
        }

        // When in the browser we need to use the html5 file system rather than
        // the one cordova supplies, but it needs to request a quota first.
        if(typeof navigator.webkitPersistentStorage !== "undefined") {
            navigator.webkitPersistentStorage.requestQuota(20 * 1024 * 1024, requestFileSystem, function() {
                console.warn("User declined file storage");
            });
        }
        else {
            // Amount is 0 because we pretty much have free reign over the
            // amount of storage we use on an android device.
            requestFileSystem(0);
        }


        function refreshData(data) {

            console.log(data);
            workOrders = [];
            window.workOrderMap = workOrderMap = {};
            for(var key in data) {
                workOrders.push(workOrderMap[key] = new WorkOrder(data[key].data, key));
            }

            // Notify everyone that the work orders have been updated.
            $rootScope.$broadcast("WorkOrders.updated", workOrders, workOrderMap);
        }

        function WorkOrder(data, id) {
            function getRefs(attachment) {
                return {
                    description: attachment.description,
                    identifier: attachment.identifier,
                    mimeType: attachment.mimeType,
                    ref: attachments.get(attachment.identifier, attachment.mimeType)
                };
            }

            this.data = data;

            this.id = id;

            this.attachments = !data.attachments ? [] : data.attachments.map(getRefs);

            this.targetStartDate = new Date(data.targetStartDate);

            this.statuses = data.statuses;
            this.photos = !data.photos ? [] : data.photos.map(getRefs);

            if(!this.statuses) {
                this.statuses = [
                    {
                        name: "DOWNLOADED"
                    }
                ];
                this.save();
            }
        }

        WorkOrder.prototype.ABORT = -1;
        // When we first recieve the work order this should be the status.
        WorkOrder.prototype.ASSIGNED = 1;
        // Once we've got the work order, we change to this status.
        WorkOrder.prototype.DOWNLOADED = 2;
        WorkOrder.prototype.ONWAY = 3;
        WorkOrder.prototype.ONSITE = 4;
        WorkOrder.prototype.RISK_ASSESS = 5;
        WorkOrder.prototype.TAKE_PHOTOS = 6;
        WorkOrder.prototype.COMPLETE = 7;

        /**
         * The app status is different to the actual status in that we have
         * to display different things in the app. This might not be the best
         * place to have this(it probably should be in the controller)
         * @return {String} The status name.
         */
        WorkOrder.prototype.appStatus = function() {
            var status = this.status();

            if(status) {
                var statusName = status.name;
                if(statusName === "RISK_ASSESS" && this.photos.length) {
                    return "TAKE_PHOTOS";
                }
                else return statusName;
            }
            else {
                return "ASSIGNED";
            }
        };

        /**
         * Get a number value that represents the order of the current status.
         * @return {[type]} [description]
         */
        WorkOrder.prototype.statusValue = function() {
            return this[this.appStatus()];
        };

        /**
         * Save the work order to the sync framework which will handle
         * persisting it locally and remotely.
         */
        WorkOrder.prototype.save = function() {
            sync.doUpdate(datasetID, this.id, this.toJSON(), function() {
                console.log("updated sync");
            }, function() {
                console.log("failed sync");
            });
        };

        /**
         * Get the latest status in that was updated.
         * @return {Object} The status object.
         */
        WorkOrder.prototype.status = function() {
            var statuses = this.statuses;
            return statuses[statuses.length - 1];
        };

        /**
         * Update the work order to a new status.
         * @param  {String} status The status name
         * @param  {cb} data   Any data associated with the status.
         * @return {Boolean}       Whether the status was updated successfully.
         */
        WorkOrder.prototype.addStatus = function(status, data) {
            var statusValue = this[status],
                self = this;

            // Can either go to abort or one status ahead of the current one.
            if(status === "ABORT" || statusValue === this.statusValue() + 1) {

                self.statuses.push({
                    name: status,
                    timestamp: +new Date(),
                    data: data
                });

                this.save();
                return true;
            }
            else {
                return false;
            }
        };


        WorkOrder.prototype.isValidNextStatus = function(status) {
            var statusValue = this[status];
            return status === "ABORT" || statusValue === this.statusValue() + 1;
        };

        /**
         * Add a photo to the work order.
         * @param  {Attachment} attachment  The reference to the attachment object.
         * @param  {String} description The description of the photo.
         * @param  {Object} data        Data association with the photo.
         */
        WorkOrder.prototype.addPhoto = function(attachment, description, data) {
            this.photos.push({
                description: description,
                identifier: attachment.identifier,
                mimeType: attachment.mimeType,
                ref: attachment
            });

            this.save();
        };

        WorkOrder.prototype.toJSON = function() {

            function getAttachment(attachment) {
                return {
                    description: attachment.description,
                    identifier: attachment.identifier,
                    mimeType: attachment.mimeType,
                    // contains gps info etc...
                    data: attachment.data
                };
            }

            return {
                wonum: this.data.wonum,
                sideID: this.data.siteID,
                orgID: this.data.orgID,
                streetAddress: this.data.streetAddress,
                postcode: this.data.postcode,
                description: this.data.description,
                summary: this.data.summary,
                priority: this.data.priority,
                targetStartDate: this.data.targetStartDate,

                attachments:this.attachments.map(getAttachment),
                photos: this.photos.map(getAttachment),
                statuses: this.statuses
            };
        };

        return {

            getWorkOrders: function() {
                return workOrders;
            },
            getWorkOrder: function(wonum) {
                return workOrderMap[wonum];
            }

        };
      });


})(window, window.angular);