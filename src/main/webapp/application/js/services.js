angular.module("appServices",[]).
factory("JobService",["$http",function($http) {
	return {
		getAllJobs : function() {
			return $http({method:"GET", url:"app/alljobs/all", cache:false}).
				then(function(response) {
					return response.data;
				},function(response,status) {
					console.log("Error fetching all amber jobs !!");
					console.log(response);
					console.log(status);
				});
		},
        getAllProjects : function() {
            return $http({method:"GET", url:"app/alljobs/projects", cache:false}).
                then(function(response) {
                    return response.data;
                },function(response,status) {
                    console.log("Error fetching all amber jobs !!");
                    console.log(response);
                    console.log(status);
                });
        },
		fetchJob : function(jobId) {
			return $http({method:"GET", url:"app/job/"+jobId, cache:false}).
				then(function(response) {
					return response.data;
				}, function(response, status) {
					console.log("Error fetching job detail for Job Id "+jobId);
					console.log(response);
					console.log(status);
				});
		},
        cancelExperiment : function(expID) {
            return $http({method:"GET", url:"app/cancel/"+expID, cache:false}).
                then(function(response) {
                    return response.data;
                }, function(response, status) {
                    console.log("Error canceling the job:  "+jobId);
                    console.log(response);
                    console.log(status);
                });
        },
        newJob : function(step,expName,files) {
            return $http({method:"GET", url:"application/newExperiment/", cache:false}).
                then(function(response) {
                    return response.data;
                }, function(response, status) {
                    console.log("Error fetching job detail for Job Id "+jobId);
                    console.log(response);
                    console.log(status);
                });
        },
        getAllApplications : function() {
            return $http({method:"GET", url:"app/applications/list/", cache:false}).
                then(function(response) {
                    return response.data;
                }, function(response, status) {
                    console.log("Error fetching applications ");
                    console.log(response);
                    console.log(status);
                });
        },
        getAllApplicationInputs : function(application) {
            return $http({method:"GET", url:"app/applications/"+application+"/inputs/", cache:false}).
                then(function(response) {
                    return response.data;
                }, function(response, status) {
                    console.log("Error fetching application inputs for  "+application);
                    console.log(response);
                    console.log(status);
                });
        },
        uploadFile: function (file,jobID, callback) {
            $http.uploadFile({
                url: "application/uploadPDB/"+jobID,
                file: file
            }).progress(function(event) {
                    console.log('percent: ' + parseInt(100.0 * event.loaded / event.total));
                }).error(function (data, status, headers, config) {
                    console.error('Error uploading file')
                    callback(status);
                }).then(function(data, status, headers, config) {
                    callback(null);
                });
        }
	};
}]);
