'use strict';

/* Controllers */

var viewControllers = angular.module('viewControllers', []);

viewControllers.controller('viewController', ['$scope', '$http', '$routeParams',
  function($scope, $http, $routeParams) {
	
	$scope.showPreloader = true;
	
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
			$scope.showPreloader = false;
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
	
	$scope.getAllUserGifts = function () {   
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
	};
	
	
	
	
	$scope.deleteGift = function (args) {
		
		if (confirm ("Сигурни ли сте?")){
			$http({
				
				url: '../ViewGiftsController', 
				method : 'POST',
				data: {"giftId": args.giftId, "action": "deleteGift"},
				headers: {
		            'Content-Type': 'application/json'
		        }
			}).success(function(data) {
				$scope.getAllUserGifts();
			});
		}
		
	};
	
	$scope.getAllUserGifts();
	
	
	
}]);