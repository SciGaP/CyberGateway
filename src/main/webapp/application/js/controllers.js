angular.module("appControllers", ["appServices", "angularFileUpload"]).
    controller("JobListCtrl", ["JobService", "$scope", "$location","$routeParams", function (JobService, $scope, $location,$routeParams) {
        console.log("In JobListCtrl");

        $scope.project = $routeParams.project;
        console.log($scope.project);

        //filtering
        $scope.filterText = {};

        $scope.projectSelect = function (project) {
            $scope.selectedProject = project;
            $scope.project = (project == "AllExperiments") ? "" : project;
            $scope.filterText.project = (project == "AllExperiments") ? "" : project;

            console.log("project selected: " + project);
            console.log("Project filter: " + $scope.project);
        };

        $scope.projectSelect($scope.project);

        //making the project active
        $scope.navClass = function (page) {

            var currentRoute = $location.path().substring(1) || '/';
            console.log("Current Route  "+currentRoute + " Page :"+page);
            return page === currentRoute ? 'active' : '';
        };

        var loadJobs = function () {
            console.log("inside the load jobs");
            JobService.getAllJobs().then(function (jobs) {
                $scope.jobs = jobs;
                console.log(jobs);
            });
            console.log("exiting load jobs....");
        };

        loadJobs();
        $scope.refresh = function () {
            loadJobs();
        };
        $scope.fetchDetail = function (jobIndex, jobId) {
            JobService.fetchJob(jobId).then(function (job) {
                $scope.jobs[jobIndex].detail = job;
            });
        };
        //for project showing and filtering
        $scope.selectedProject = "AllExperiments";
        $scope.projects = [
            {name: "AllExperiments"},
            {name: "vanillagateway"}

        ];
        $scope.project = "";

        $scope.status = "AllJobs";
        $scope.statuses = [
            {name: "AllJobs", value: "AllJobs"},
            {name: "Finished", value: "Finished"},
            {name: "Launched", value: "Launched"}
        ];


        $scope.onProjectSelect = function (project) {
            $scope.selectedProject = project;
            $scope.project = (project == "AllExperiments") ? "" : project;

            console.log("project selected: " + project);
            console.log("Project filter: " + $scope.project);
        };


        $scope.onStatusSelect = function (status) {
            $scope.status = status;
            console.log($scope.status);
        };


        //job list filter
        $scope.statusFilter = function (job) {
            console.log(job.status + " status: " + $scope.status + "   result: " + (job.status === $scope.status));
            if ($scope.status == "AllJobs") {
                return true;
            }
            if (job.status != $scope.status) {
                return false;
            }
            return true;
        };
        $scope.showDetails = function (item) {
            $scope.item = item;
        };

        //for single job details

        $scope.fetchSingleJob = function (jobID) {
            JobService.fetchJob(jobID).then(function (job) {
                $scope.item = job;
            });
            console.log("Job fetched : " + $scope.item);
        };


        $scope.go = function (path) {
            $location.path(path);
            console.log("path cloning going to: " + path);
        };


        //cancel the experiment
        $scope.cancelExperiment = function (expID) {
            JobService.cancelExperiment(expID).then(function (response) {
                console.log("cancel experiment result: " + response);
            });
            $location.path('/');

        };


    }]).
    controller("JobController", ["$scope", "$routeParams", "JobService", function ($scope, $routeParams, JobService) {

        $scope.jobID = $routeParams.jobID;
        console.log("In Job Controller, Job ID: " + $scope.jobID);

        $scope.fetchSingleJob = function (jobID) {
            JobService.fetchJob(jobID).then(function (job) {
                $scope.job_details = job;
            });
        };
        console.log($scope.job_details);
        $scope.fetchSingleJob(jobID);
        console.log("Job details : " + $scope.job_details);

    }]).
    controller("NewJobCtrl", ["$scope", "$routeParams", "$http", "$location","JobService", function ($scope, $routeParams, $http, $location,JobService) {

        $scope.experiment = {};

        //save the advanced options
        $scope.saveAdvanceOptions = function () {
            $scope.savedCPUCount = $scope.advCPUcount;
            $scope.savedScheduling = $scope.advScheduling;
        }

        $scope.createJob = function (isValid) {
            // check to make sure the form is completely valid
            if (isValid) {

                console.log("posting data....");
                var data = $.param($scope.experiment);
                var file = $scope.files[0];
                console.log(data);

                var exp = $scope.experiment;
                var fd = new FormData();
                fd.append('file', file);
                fd.append('name', exp.name);
                fd.append('description', exp.description);
                fd.append('application', exp.application);

                $http.post('app/newjob', fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                })
                    .success(function (data) {
                        console.log("Form posted");
                        console.log(data);
                    });
                alert('Job created Successfully!');
                $location.path('/');
            }
        };


        //an array of files selected
        $scope.files = [];

        //listen for the file selected event
        $scope.$on("fileSelected", function (event, args) {
            $scope.$apply(function () {
                //add the file object to the scope's files collection
                $scope.files.push(args.file);
            });
        });

        $scope.deleteFile = function (index) {
            $scope.files.splice(index, 1);
            console.log(index);
        };

        console.log($scope.selected);
        console.log("In New Job Controller ...");


        $scope.experiment.application = "SimpleEcho3";


        $scope.getAllApplications = function () {
            JobService.getAllApplications().then(function (response) {
                console.log("applications experiment result: " + response);
                $scope.applications = response;
            });

        };

        $scope.getAllApplications();

        $scope.onApplicationSelect = function (appname) {
            $scope.experiment.application = appname;
            console.log($scope.experiment.application);
        };


    }]).directive('fileUpload',function () {
        console.log("uploading file ...");
        return {
            scope: true,        //create a new scope
            link: function (scope, el, attrs) {
                el.bind('change', function (event) {
                    var files = event.target.files;
                    //iterate files since 'multiple' may be specified on the element
                    for (var i = 0; i < files.length; i++) {
                        //emit event upward
                        scope.$emit("fileSelected", { file: files[i] });
                    }
                });
            }
        };
    }).controller("FileUploadController", [ '$scope', '$upload', function ($scope, $upload) {
        $scope.onFileSelect = function ($files) {
            //$files: an array of files selected, each file has name, size, and type.
            for (var i = 0; i < $files.length; i++) {
                var file = $files[i];
                $scope.upload = $upload.upload({
                    url: 'amberCtrl/uploadPDB/test', //upload.php script, node.js route, or servlet url
                    method: 'POST',
                    transformRequest: angular.identity,
                    headers: {'Content-Type': 'multipart/form-data'}, withCredential: true,
                    data: {file: file},
                    file: file,

                    // file: $files, //upload multiple files, this feature only works in HTML5 FromData browsers
                    /* set file formData name for 'Content-Desposition' header. Default: 'file' */
                    fileFormDataName: 'myFile' //OR for HTML5 multiple upload only a list: ['name1', 'name2', ...]
                    /* customize how data is added to formData. See #40#issuecomment-28612000 for example */
                    //formDataAppender: function(formData, key, val){}
                }).progress(function (evt) {
                        console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
                    }).success(function (data, status, headers, config) {
                        // file is uploaded successfully
                        console.log("File uploaded successfully")
                        console.log(data);

                    });
                //.error(...)
                //.then(success, error, progress);
            }
        };
    }]);