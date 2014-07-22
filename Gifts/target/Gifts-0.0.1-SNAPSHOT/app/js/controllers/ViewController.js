'use strict';

/* Controllers */

var viewControllers = angular.module('viewControllers', []);

viewControllers.controller('viewController', ['$scope', '$http', '$routeParams',
  function($scope, $http, $routeParams) {
	
	$scope.viewGifts = {};
	$scope.viewGiftId = $routeParams.viewGiftId;
	
	if (!$scope.viewGiftId) {
		$http({
			url: '../ViewGiftsController', 
			method : 'POST',
			data: {"action": "getAllGifts"},
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			$scope.viewGifts = data;
			
			console.log ($scope.viewGifts);
			
			
		});
	} else {
		$scope.viewGifts.id = $scope.viewGiftId;
		$scope.viewGifts.action = 'getSingleGifts';
		$http({
			url: '../ViewGiftsController', 
			method : 'POST',
			data: $scope.viewGifts,
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			
			if ($scope.user.id != data.gifts.userId) {
				data.gifts.showMessageButton = 1;
			}
			$scope.viewGifts = data;
		});
	}

  }]);

viewControllers.controller('myGiftsController', ['$scope', '$http', function($scope, $http) {
	
	$http({
		url: '../ViewGiftsController', 
		method : 'POST',
		data: {"userId": $scope.user.id, "action": "getGiftsByUserId"},
		headers: {
            'Content-Type': 'application/json'
        }
	}).success(function(data) {
		$scope.viewGifts = data;
	});
	
}]);