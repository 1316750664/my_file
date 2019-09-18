var queuedFlag = false;//是否有成功加入队列待上传的文件
var urlSuccessArray = [];
var nameSuccessArray = [];

function swfLoadError() {
    alert("请安装Adobe Flash Player 9.0以上");
}

function fileDialogStart() {
    /* I don't need to do anything here */
}

//只要成功加入上传队列的文件均会自动执行此事件一次
function fileUploadQueued(file) {
    try {
        var preImg = this.customSettings.preImage_target;
        var liImg = document.createElement("li");
        liImg.innerHTML = '<img src="/images/blank128.png" id="swf_img_' + file.index + '"/><p>' + file.name + '</p>';
        document.getElementById(preImg).appendChild(liImg);
    } catch (ex) {
        this.debug(ex);
    }
}

function fileDialogComplete(numFilesSelected, numFilesQueued) {
    try {
        if (numFilesQueued > 0) {
            queuedFlag = true;
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function fileUploadStart(file) {
    try {
        var per = parseInt(this.customSettings.preImage_per_row);
        var fileIndex = parseInt(file.index);
        var col = fileIndex % per;
        var row = parseInt(fileIndex / per);
        var progressId = this.customSettings.progress_target;
        document.getElementById(progressId).style.left = parseInt(col * 174 + 10) + "px";
        document.getElementById(progressId).style.top = parseInt(row * 184 + 120 + 40) + "px";
    } catch (ex) {
        this.debug(ex);
    }
}

function fileUploadQueueError(file, errorCode, message) {
    try {
        if (errorCode === SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED) {
            alert("You have attempted to queue too many files.\n" + (message === 0 ? "You have reached the upload limit." : "You may select " + (message > 1 ? "up to " + message + " files." : "one file.")));
            return;
        }
        var progress = new FileProgress(file, this.customSettings.progress_target);
        progress.setError();
        progress.toggleCancel(false);
        switch (errorCode) {
            case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
                progress.setStatus("File is too big.");
                this.debug("Error Code: File too big, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
                progress.setStatus("Cannot upload Zero Byte files.");
                this.debug("Error Code: Zero byte file, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
                progress.setStatus("Invalid File Type.");
                this.debug("Error Code: Invalid File Type, File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
            case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
                alert("You have selected too many files.  " + (message > 1 ? "You may only add " + message + " more files" : "You cannot add any more files."));
                break;
            default:
                if (file !== null) {
                    progress.setStatus("Unhandled Error");
                }
                this.debug("Error Code: " + errorCode + ", File name: " + file.name + ", File size: " + file.size + ", Message: " + message);
                break;
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function fileUploadProgress(file, bytesLoaded) {
    try {
        var percent = Math.ceil((bytesLoaded / file.size) * 100);
        var progress = new FileProgress(file, this.customSettings.progress_target);
        progress.setProgress(percent);
        progress.setStatus("已上传" + percent + "%......");
    } catch (ex) {
        this.debug(ex);
    }
}

function fileUploadSuccess(file, serverData) {
    try {
//      alert(serverData);
        var serverJson = eval('(' + serverData + ')');
        if (serverJson.error == 0) {
            urlSuccessArray.push(serverJson.url);
            nameSuccessArray.push(file.name);
            document.getElementById("swf_img_" + file.index).src = serverJson.url;
        } else {
            alert(serverJson.message);
        }
    } catch (ex) {
        this.debug(ex);
    }
}

function fileUploadComplete(file) {
    try {
        /*I want the next upload to continue automatically so I'll call startUpload here */
        if (this.getStats().files_queued > 0) {
            this.startUpload();
        } else {
            insertUpFile(this);//调用插入到主页面
            queuedFlag = false;
            var progress = new FileProgress(file, this.customSettings.progress_target);
            progress.setComplete();
            progress.setStatus("");
            progress.toggleCancel(false);
        }
    } catch (ex) {
        this.debug(ex);
    }
}

//调用插入到主页面，fileUploadComplete函数中调用
function insertUpFile(obj) {
    if (urlSuccessArray.length == 0) {
        //alert("请先上传文件");
        return;
    }
    else {
        var pImgViewId = obj.customSettings.p_image_view_id;//obj不能使用this
        for (var i = 0; i < urlSuccessArray.length; i++) {
            var liImg = window.parent.document.createElement("li");
            liImg.innerHTML = '<img src="' + urlSuccessArray[i] + '"/><p>' + nameSuccessArray[i] + '</p>';
            window.parent.document.getElementById(pImgViewId).appendChild(liImg);
        }

        var pAllImgId = obj.customSettings.p_all_image_id;
        var allImgHidden = window.parent.document.getElementById(pAllImgId);
        if (allImgHidden.value == "") {
            allImgHidden.value = urlSuccessArray.join(",");
        }
        else {
            allImgHidden.value = allImgHidden.value + "," + urlSuccessArray.join(",");
        }
        window.parent.dragAndDrop();
        urlSuccessArray = [];
        nameSuccessArray = [];
    }
}

function fileUploadError(file, errorCode, message) {
    var progress;
    try {
        switch (errorCode) {
            case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
                try {
                    progress = new FileProgress(file, this.customSettings.progress_target);
                    progress.setCancelled();
                    progress.setStatus("Cancelled");
                    progress.toggleCancel(false);
                }
                catch (ex1) {
                    this.debug(ex1);
                }
                break;
            case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
                try {
                    progress = new FileProgress(file, this.customSettings.progress_target);
                    progress.setCancelled();
                    progress.setStatus("Stopped");
                    progress.toggleCancel(true);
                }
                catch (ex2) {
                    this.debug(ex2);
                }
            case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
                progress.setCancelled();
                progress.setStatus("SizeLimited");
                progress.toggleCancel(true);
                break;
            default:
                alert(message);
                break;
        }
    } catch (ex3) {
        this.debug(ex3);
    }
}

/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file, targetID) {
    this.fileProgressID = "divFileProgress";

    this.fileProgressWrapper = document.getElementById(this.fileProgressID);
    if (!this.fileProgressWrapper) {
        this.fileProgressWrapper = document.createElement("div");
        this.fileProgressWrapper.className = "progressWrapper";
        this.fileProgressWrapper.id = this.fileProgressID;

        this.fileProgressElement = document.createElement("div");
        this.fileProgressElement.className = "progressContainer";

        var progressCancel = document.createElement("a");
        progressCancel.className = "progressCancel";
        progressCancel.href = "#";
        progressCancel.style.visibility = "hidden";
        progressCancel.appendChild(document.createTextNode(" "));

        var progressText = document.createElement("div");
        progressText.className = "progressName";
        progressText.appendChild(document.createTextNode(file.name));

        var progressBar = document.createElement("div");
        progressBar.className = "progressBarInProgress";

        var progressStatus = document.createElement("div");
        progressStatus.className = "progressBarStatus";
        progressStatus.innerHTML = "&nbsp;";

        this.fileProgressElement.appendChild(progressCancel);
        this.fileProgressElement.appendChild(progressText);
        this.fileProgressElement.appendChild(progressStatus);
        this.fileProgressElement.appendChild(progressBar);

        this.fileProgressWrapper.appendChild(this.fileProgressElement);

        document.getElementById(targetID).appendChild(this.fileProgressWrapper);
        fadeIn(this.fileProgressWrapper, 0);

    } else {
        this.fileProgressElement = this.fileProgressWrapper.firstChild;
        this.fileProgressElement.childNodes[1].firstChild.nodeValue = file.name;
    }
    this.height = this.fileProgressWrapper.offsetHeight;
}

FileProgress.prototype.setProgress = function (percentage) {
    this.fileProgressElement.className = "progressContainer green";
    this.fileProgressElement.childNodes[3].className = "progressBarInProgress";
    this.fileProgressElement.childNodes[3].style.width = percentage + "%";
};

FileProgress.prototype.setComplete = function () {
    this.fileProgressElement.className = "progressContainer blue";
    this.fileProgressElement.childNodes[3].className = "progressBarComplete";
    this.fileProgressElement.childNodes[3].style.width = "";

};

FileProgress.prototype.setError = function () {
    this.fileProgressElement.className = "progressContainer red";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};

FileProgress.prototype.setCancelled = function () {
    this.fileProgressElement.className = "progressContainer";
    this.fileProgressElement.childNodes[3].className = "progressBarError";
    this.fileProgressElement.childNodes[3].style.width = "";

};

FileProgress.prototype.setStatus = function (status) {
    this.fileProgressElement.childNodes[2].innerHTML = status;
    if (status == "") {//上传完成
        this.fileProgressElement.childNodes[1].innerHTML = status;
    }
};

FileProgress.prototype.toggleCancel = function (show, swfuploadInstance) {
    this.fileProgressElement.childNodes[0].style.visibility = show ? "visible" : "hidden";
    if (swfuploadInstance) {
        var fileID = this.fileProgressID;
        this.fileProgressElement.childNodes[0].onclick = function () {
            swfuploadInstance.cancelUpload(fileID);
            return false;
        };
    }
};