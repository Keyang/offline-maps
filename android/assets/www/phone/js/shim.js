document.addEventListener("deviceready", function() {

    if(!window.FileTransfer) {
        console.log("------------------------------------");
        console.log("using custom FileTransfer");
        console.log("------------------------------------");
        window.FileTransfer = function() {
            this.onprogress = null;
        };

        window.FileUploadOptions = function() {};

        /**
         * For use in the browser when cordova does not work. Download to
         * html5 file system.
         * @param  {String} src     Remote url.
         * @param  {String} dst     Destination on file system.
         * @param  {Function} success Success callback
         * @param  {Function} fail    Error callback.
         */
        FileTransfer.prototype.download = function(src, dst, success, fail) {
            var xhr = new XMLHttpRequest();
            xhr.open('GET', decodeURI(src), true);

            // We need to use the reponse as a blob, because
            // it is most likely binary data.
            xhr.responseType = 'blob';

            xhr.onreadystatechange = function(e) {
                if (this.readyState == 4 && this.status == 200) {

                    var res = this.response;

                    fileSystem.root.getFile(dst, {create: true}, function(fileEntry) {

                        fileEntry.createWriter(function(fileWriter) {

                            fileWriter.onwriteend = function(e) {
                                console.log("Write completed.", fileEntry.toURL());
                                success(fileEntry);
                            };

                            fileWriter.onerror = fail;

                            fileWriter.write(res);

                        }, fail);

                    }, fail);
                }
            };

            xhr.onprogress = this.onprogress;

            xhr.send();

        };


        FileTransfer.prototype.upload = function(src, dst, success, fail, options) {
            var self = this;

            options = options || {};

            function upload(blob) {
                var data = new FormData();


                data.append(options.fileKey, blob, options.fileName);

                for(var key in options.params) {
                    data.append(key, options.params[key]);
                }

                var xhr = new XMLHttpRequest();

                xhr.open("POST", dst);

                xhr.onreadystatechange = function() {
                    if (this.readyState == 4 && this.status == 200) {
                        success();
                    }
                };

                xhr.onprogress = self.onprogress;

                xhr.send(data);

            }

            fileSystem.root.getFile(src, {create: false}, function(entry) {

                entry.file(function(file) {
                    upload(file);
                }, fail);
            }, fail);
        };
    }

}, false);












