/**
 * 文件上传组件
 * */
document.writeln('<link href="../webuploader/bootstrap.min.css" rel="stylesheet" type="text/css" />');
document.writeln('<link href="../webuploader/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />');
document.writeln('<link href="../webuploader/webuploader.css" rel="stylesheet" type="text/css" />');
document.writeln('<link href="../webuploader/style.css" rel="stylesheet" type="text/css" />');
document.writeln('<script type="text/javascript" src="../webuploader/webuploader.js"></script>');

(function (window, undefined) {
    var FileUploader = function (targetId, configurationKey, formId, allowUpload, allowDownload, allowDelete, fileServerUrl) {
        var fileUploader = new Object();
        fileUploader.options = {
            targetId: targetId,
            configurationKey: configurationKey,
            formId: formId,
            allowUpload: allowUpload === undefined ? true : allowUpload,
            allowDelete: allowDelete === undefined ? true : allowDelete,
            allowDownload: allowDownload === undefined ? true : allowDownload,
            chunkSize: '1048576',
            fileServerUrl: fileServerUrl === undefined ? 'http://192.168.16.114:2130' : fileServerUrl
        };
        fileUploader.configuration = {
            fileNumLimit: undefined,
            fileSingleSizeLimit:
            undefined,
            fileSizeLimit:
            undefined,
            enableSecretLevel:
                true,
            allowUpload:
                true,
            allowDelete:
                true,
            allowDownload:
                true,
            uploadOperationKey:
                '',
            deleteOperationKey:
                '',
            downloadOperationKey:
                '',
            enableUserSeparate:
                true,
            enableImageCompress:
                false,
            compressedImageWidth:
                1600,
            compressedImageHeight:
                1600,
            compressImageSize:
                0,
            enablePreview:
                true,
            enableReview:
                false
        };
        fileUploader.getUuid = function () {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            var uuid = s.join("");
            return uuid;
        };
        fileUploader.initWebUploader = function () {
            var component = this;
            var uploader = WebUploader.create({

                // {Selector} [可选] [默认值：undefined] 指定Drag And Drop拖拽的容器，如果不指定，则不启动。
                dnd: undefined,

                // {Selector} [可选] [默认值：false] 是否禁掉整个页面的拖拽功能，如果不禁用，图片拖进来的时候会默认被浏览器打开。
                disableGlobalDnd: false,

                // 不压缩image
                resize: false,

                // {Boolean} [可选] [默认值：false] 设置为 true 后，不需要手动调用上传，有文件选择即开始上传。
                auto: true,

                // {Boolean} [可选] [默认值：false] 是否允许在文件传输时提前把下一个文件准备好。 对于一个文件的准备工作比较耗时，比如图片压缩，md5序列化。 如果能提前在当前文件传输期处理，可以节省总体耗时。
                prepareNextFile: false,

                // swf文件路径
                swf: 'webuploader/Uploader.swf',

                // 文件接收服务端。
                server: component.options.fileServerUrl + '/fileUpload/upload?token=' + component.getToken(),

                // 是否要分片处理大文件上传
                chunked: true,

                // 分片大小
                chunkSize: component.options.chunkSize,

                // 如果某个分片由于网络问题出错，允许自动重传的次数
                chunkRetry: 3,

                // 文件上传请求的参数表，每次发送都会发送此对象中的参数
                formData: {
                    configurationKey: component.options.configurationKey
                },

                // 验证文件总数量, 超出则不允许加入队列
                fileNumLimit: undefined,

                // 验证文件总大小是否超出限制, 超出则不允许加入队列
                fileSizeLimit: undefined,

                // 验证单个文件大小是否超出限制, 超出则不允许加入队列
                fileSingleSizeLimit: undefined,

                // 选择文件的按钮。可选。
                // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                pick: {
                    id: "#" + component.pickerId
                }
            });
            this.webuploader = uploader;
            // 当有文件被加入队列之前触发
            uploader.on('beforeFileQueued', function (file) {
                var handler = true;
                file.yfId = component.getUuid();
                file.divId = component.getFileItemId(file.yfId);
                file.formId = component.options.formId;
                file.isOwnFile = true;
                file.fileName = file.name;
                file.fileSize = file.size;
                file.secretLevel = component.secretLevelSelect.value;
                component.divFiles.appendChild(component.getFileItem(file));
                $('#' + file.divId).find('span.state').text('(正在分配存储空间)');
                $.ajax({
                    contentType: 'application/json;charset=utf-8',
                    type: 'post',
                    async: false,
                    url: component.options.fileServerUrl + '/fileRepository/allocateSpace?token=' + component.getToken(),
                    data: JSON.stringify({
                        fileName: file.name,
                        formId: file.formId,
                        secretLevel: file.secretLevel,
                        md5: file.md5 === undefined ? '' : file.md5,
                        contentType: file.type,
                        fileSize: file.size,
                        chunks: file.chunks === undefined ? 1 : file.chunks,
                        yfId: file.yfId,
                        configurationKey: component.options.configurationKey
                    }),
                    success: function (data) {
                        if (data && data.code === 40000) {
                            var divId = component.getFileItemId(data.fileInfo.yfId);
                            var div = document.getElementById(divId);
                            div.file.yfId = data.fileInfo.id;
                            $('#' + divId).find('span.state').text('等待上传...');
                        } else {
                            $('#' + file.divId).find('span.state').text(data.msg);
                            handler = false;
                        }
                    },
                    error: function (e) {
                    },
                    complete: function () {
                    }
                });
                return handler;
            });

            // 当有文件添加进来的时候
            uploader.on('fileQueued', function (file) {
            });

            // 文件上传过程中创建进度条实时显示。
            uploader.on('uploadProgress', function (file, percentage) {
                var $li = $('#' + file.divId),
                    $percent = $li.find('.progress .progress-bar');

                // 避免重复创建
                if (!$percent.length) {
                    $percent = $('<div class="progress progress-striped active">' +
                        '<div class="progress-bar" role="progressbar" style="width: 0%">' +
                        '</div>' +
                        '</div>').appendTo($li).find('.progress-bar');
                }

                $li.find('span.state').text('上传中');

                $percent.css('width', percentage * 100 + '%');
            });

            uploader.on('uploadSuccess', function (file) {
                $('#' + file.divId).find('span.state').text('已上传');
            });

            uploader.on('uploadError', function (file) {
                $('#' + file.divId).find('span.state').text('上传出错');
            });

            uploader.on('uploadComplete', function (file) {
                $('#' + file.divId).find('.progress').fadeOut();
            });

            uploader.on('uploadBeforeSend', function (obj, data, header) {
                data.yfId = obj.file.yfId;
                data.configurationKey = component.options.configurationKey;
            });
        };
        fileUploader.getFileItem = function (file) {
            var div = document.createElement('div');
            div.file = file;
            div.id = file.divId;
            div.className = 'fileItem-div';
            var aFile = document.createElement('a');
            aFile.className = 'fileItem-download';
            aFile.href = '#';
            aFile.file = file;
            aFile.uploader = this;
            var h4 = document.createElement('h4');
            h4.innerHTML = file.fileName;
            h4.className = 'fileItem-info';
            aFile.appendChild(h4);
            aFile.addEventListener('click', function () {
                if (this.uploader.isAllowDownload()) {
                    window.open(this.uploader.options.fileServerUrl + '/fileDownload/download?configurationKey=' + encodeURI(this.uploader.options.configurationKey) + '&fileToken=' + this.file.yfId + '&token=' + this.uploader.getToken(), '_blank');
                }
            });
            div.appendChild(aFile);
            var aDownload = document.createElement('a');
            aDownload.href = '#';
            aDownload.file = file;
            aDownload.uploader = this;
            aDownload.className = this.isAllowDelete() ? 'btn-delete' : 'btn-delete-not-allow-delete';
            aDownload.innerHTML = '删除';
            aDownload.addEventListener('click', function () {
                if (this.uploader.isAllowDelete()) {
                    $('#' + this.file.divId).find('span.state').text('正在删除...');
                    try {
                        this.uploader.webuploader.cancelFile(this.file.id);
                    } catch (e) {

                    }
                    $.ajax({
                        contentType: 'application/json;charset=utf-8',
                        type: 'post',
                        url: this.uploader.options.fileServerUrl + '/fileInfo/deleteByFileId?token=' + this.uploader.getToken(),
                        data: JSON.stringify({
                            configurationKey: this.uploader.options.configurationKey,
                            fileId: this.file.yfId
                        }),
                        success: function (data) {
                            if (data && data.code === 40000) {
                                $('#' + file.divId).find('span.state').text('删除成功');
                                $('#' + file.divId).remove();
                            } else {
                                $('#' + file.divId).find('span.state').text('删除失败');
                            }
                        },
                        error: function (e) {
                        },
                        complete: function () {
                        }
                    });
                }
            });
            div.appendChild(aDownload);
            var spanFileSize = document.createElement('span');
            spanFileSize.className = 'fileItem-size';
            spanFileSize.innerHTML = this.getFileSizeString(file.fileSize);
            div.appendChild(spanFileSize);
            if (this.configuration.enableSecretLevel) {
                div.appendChild(this.getSecretLevelItem(file.secretLevel));
            }
            var spanState = document.createElement('span');
            spanState.className = 'state';
            div.appendChild(spanState);
            return div;
        };
        fileUploader.getSecretLevelItem = function (secretLevel) {
            var spanSecretLevel = document.createElement('span');
            spanSecretLevel.innerHTML = this.getSecretLevelText(secretLevel);
            spanSecretLevel.className = 'fileItem-secret-level';
            return spanSecretLevel;
        }
        fileUploader.getFileSizeString = function (fileSize) {
            var str = fileSize + "B";
            if (fileSize > 1073741824) {
                str = ((fileSize / 1024 / 1024 / 1024 * 100) / 100).toFixed(2) + "GB";
            } else if (fileSize > 1048576) {
                str = ((fileSize / 1024 / 1024 * 100) / 100).toFixed(2) + "MB";
            } else if (fileSize > 1024) {
                str = (((fileSize / 1024) * 100) / 100).toFixed(2) + "KB";
            }
            return str;
        };
        fileUploader.downloadZipped = function () {
            var uploader = this;
            var divItems = $('#' + uploader.divFiles.id).find('div.fileItem-div');
            var tokens = new Array();
            for (var i = 0; i < divItems.length; i++) {
                tokens[i] = divItems[i].file.yfId;
            }
            $.ajax({
                contentType: 'application/json;charset=utf-8',
                type: 'post',
                url: uploader.options.fileServerUrl + '/fileDownload/initDownloadZipped?token=' + uploader.getToken(),
                data: JSON.stringify({
                    fileTokens: tokens
                }),
                success: function (data) {
                    if (data && data.code === 40000) {
                        var downloadToken = data.downloadToken;
                        window.open(uploader.options.fileServerUrl + '/fileDownload/downloadZipped?configurationKey=' + uploader.options.configurationKey + "&downloadToken=" + downloadToken, '_blank');
                    }
                },
                error: function (e) {
                },
                complete: function () {
                }
            });
        };
        fileUploader.pickerId = fileUploader.getUuid();
        fileUploader.setAllowUpload = function (val) {
            this.options.allowUpload = val;
            if (val) {
                document.getElementById(this.pickerId).className = 'webuploader-pick-allow-upload';
                this.initWebUploader();
            } else {
                document.getElementById(this.pickerId).className = 'webuploader-pick-not-allow-upload';
                this.webuploader.destroy();
            }
        };
        fileUploader.setAllowDelete = function (val) {
            this.options.allowDelete = val;
            var targetCls = '';
            var newCls = '';
            if (val) {
                targetCls = 'a.btn-delete-not-allow-delete';
                newCls = 'btn-delete';
            } else {
                targetCls = 'a.btn-delete';
                newCls = 'btn-delete-not-allow-delete';
            }
            $('#' + this.divFiles.id).find(targetCls).attr('class', newCls);
        };
        fileUploader.setAllowDownload = function (val) {
            this.options.allowDownload = val;
            var targetCls = '';
            var newCls = '';
            if (val) {
                targetCls = 'button.btn-download-zipped-not-allow-download';
                newCls = 'btn-download-zipped';
            } else {
                targetCls = 'button.btn-download-zipped';
                newCls = 'btn-download-zipped-not-allow-download';
            }
            $('#' + this.options.targetId).find(targetCls).attr('class', newCls);
        };
        fileUploader.isAllowUpload = function () {
            return this.options.allowUpload && this.configuration.allowUpload;
        };
        fileUploader.isAllowDelete = function () {
            return this.options.allowDelete && this.configuration.allowDelete;
        };
        fileUploader.isAllowDownload = function () {
            return this.options.allowDownload && this.configuration.allowDownload;
        };
        fileUploader.load = function () {
            var div = document.getElementById(this.options.targetId);
            if (div == null) {
                alert('目标div不存在');
                return;
            }
            div.innerText = '';
            div.uploader = this;
            var component = this;
            $.ajax({
                contentType: 'application/json;charset=utf-8',
                type: "post",
                url: component.options.fileServerUrl + '/fileConfiguration/load?token=' + component.getToken(),
                data: JSON.stringify({
                    configurationKey: component.options.configurationKey,
                    formId: component.options.formId
                }),
                success: function (data, status) {
                    if (data && data.code === 40000) {
                        var allSecretLevels = new Array();
                        var str = data.allSecretLevels.substr(1, data.allSecretLevels.length - 2);
                        var arr = str.split(',');
                        for (var j = 0; j < arr.length; j++) {
                            var t = arr[j].split(':');
                            var obj = new Object();
                            obj.value = t[0];
                            obj.text = t[1].substr(1, t[1].length - 2);
                            allSecretLevels.push(obj);
                        }
                        component.allSecretLevels = allSecretLevels;
                        component.initLayout(data.fileConfiguration, data.fileInfos, data.secretLevels);
                    } else {
                        alert(data.msg);
                    }
                },
                error: function (e) {
                },
                complete: function () {
                }
            });
        };
        fileUploader.getSecretLevelText = function (secretLevel) {
            for (var i = 0; i < this.allSecretLevels.length; i++) {
                var t = this.allSecretLevels[i];
                if (t.value == secretLevel) {
                    return t.text;
                }
            }
            return '未知';
        };
        fileUploader.initLayout = function (config, fileInfos, secretLevels) {
            var div = document.getElementById(this.options.targetId);
            this.configuration.fileNumLimit = config.fileNumLimit;
            this.configuration.fileSingleSizeLimit = config.fileSingleSizeLimit;
            this.configuration.fileSizeLimit = config.fileSizeLimit;
            this.configuration.enableSecretLevel = config.enableSecretLevel === 1;
            this.configuration.allowUpload = config.allowUpload === 1;
            this.configuration.allowDelete = config.allowDelete === 1;
            this.configuration.allowDownload = config.allowDownload === 1;
            this.configuration.uploadOperationKey = config.uploadOperationKey;
            this.configuration.deleteOperationKey = config.deleteOperationKey;
            this.configuration.downloadOperationKey = config.downloadOperationKey;
            this.configuration.enableUserSeparate = config.enableUserSeparate === 1;
            this.configuration.enableImageCompress = config.enableImageCompress === 1;
            this.configuration.compressedImageWidth = config.compressedImageWidth;
            this.configuration.compressedImageHeight = config.compressedImageHeight;
            this.configuration.compressImageSize = config.compressImageSize;
            this.configuration.enablePreview = config.enablePreview === 1;
            this.configuration.enableReview = config.enableReview === 1;
            div.className = 'uploader';
            var divToolbar = document.createElement('div');
            // 密级选择
            var secretLevelSelect = document.createElement('select');
            secretLevelSelect.className = this.isAllowUpload() ? 'select-secret-level' : 'select-secret-level-not-allow-upload';
            var str = secretLevels.substr(1, secretLevels.length - 2);
            var arr = str.split(',');
            for (var j = 0; j < arr.length; j++) {
                var t = arr[j].split(':');
                var opt = document.createElement('option');
                opt.value = t[0];
                opt.text = t[1].substr(1, t[1].length - 2);
                secretLevelSelect.add(opt);
            }
            this.secretLevelSelect = secretLevelSelect;
            divToolbar.appendChild(secretLevelSelect);
            // 选文件按钮
            var btnPicker = document.createElement('div');
            this.picker = btnPicker;
            btnPicker.id = this.pickerId;
            btnPicker.innerHTML = '选择文件';
            divToolbar.appendChild(btnPicker);
            // 打包下载按钮
            var btnDownloadZipped = document.createElement('button');
            btnDownloadZipped.uploader = this;
            btnDownloadZipped.className = this.isAllowDownload() ? 'btn-download-zipped' : 'btn-download-zipped-not-allow-download';
            btnDownloadZipped.innerHTML = '打包下载';
            btnDownloadZipped.addEventListener('click', function () {
                this.uploader.downloadZipped();
            });
            divToolbar.appendChild(btnDownloadZipped);
            div.appendChild(divToolbar);

            var divFiles = document.createElement('div');
            divFiles.className = 'uploader-files';
            divFiles.id = 'files_' + this.pickerId;

            for (var i = 0; i < fileInfos.length; i++) {
                var fileInfo = fileInfos[i];
                fileInfo.divId = this.getFileItemId(fileInfo.id);
                fileInfo.yfId = fileInfo.id;
                divFiles.appendChild(this.getFileItem(fileInfo));
            }

            div.appendChild(divFiles);
            this.divFiles = divFiles;
            if (this.isAllowUpload()) {
                this.initWebUploader();
            } else {
                btnPicker.className = 'webuploader-pick-not-allow-upload';
            }
        };
        fileUploader.getFileItemId = function (fileId) {
            return 'div_' + this.pickerId + '_' + fileId
        };
        fileUploader.getToken = function () {
            var strcookie = document.cookie;//获取cookie字符串
            var arrcookie = strcookie.split("; ");//分割
            //遍历匹配
            for (var i = 0; i < arrcookie.length; i++) {
                var arr = arrcookie[i].split("=");
                if (arr[0] == "token") {
                    return arr[1];
                }
            }
            return "";
        };
        fileUploader.load();
        return fileUploader;
    };
    window.ace = $.extend(window.ace || {}, {uploader: {FileUploader: FileUploader}});
})(window, undefined);