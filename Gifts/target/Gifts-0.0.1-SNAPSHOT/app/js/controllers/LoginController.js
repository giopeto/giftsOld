'use strict';

/* Controllers */

var giftsControllers = angular.module('giftsControllers', []);

/* Main Controller */

giftsControllers.controller('MainController', ['$scope', 'giftServices', '$http',
  function($scope, giftServices, $http) {
	
	$scope.user = {};
	$scope.user.fieldName = '';
	$scope.user.success = '';
	
	$scope.city = {};
	$scope.city = 0;
	
	$scope.gift = {};
	$scope.gift.userIdError = '';
	$scope.gift.cityError = '';
	$scope.gift.nameError = '';
	$scope.gift.giftId = '';
	
	if (!$scope.user.id) {
		$scope.user = giftServices.getUserInfo();
	}
	
	$scope.getUnreadMessagesCount = function () {
		
		$http({
			url: '../UserServlet', 
			method : 'POST',
			data: {userId: $scope.user.id, action: "getUnreadMessagesCount"},
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {

			console.log (data);
			$scope.user.unreadMessagesCount = data;
		});
	};
	
	$scope.getUnreadMessagesCount();
	
	
	
	
/*	$scope.getCities = function () {
	
		consoler.log ('hereeeee');
		
		var cities = $http({
			url: '../UserServlet', 
			method : 'POST',
			data: {'action': 'getCity'},
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			console.log(data);
			$scope.cities = data;
			
			
		});
		
		return cities;
		
	};*/
	
	
  }]);

/* Login Controller */
giftsControllers.controller('loginController', ['$scope', '$http', '$location', 'giftServices', 
  function($scope, $http, $location, giftServices) {
	
	$scope.user.success = 0;
	
	$scope.createAccount = function() {
		
		$scope.user.action = 'createAccount';
		$scope.user.city = $scope.city;
		
		$scope.user.createAccountNameError = '';
		$scope.user.createAccountPasswordError = '';
		$scope.user.createAccountNameExistError = '';
		
		if ($scope.user.name === '' || !$scope.user.name) {
			$scope.user.createAccountNameError = 'Please enter your user name';
			return;
		}
		
		if ($scope.user.password != $scope.user.retypePassword) {
			$scope.user.createAccountPasswordError = 'Password dont match';
			return;
		}
		
		if ($scope.user.password === '' || !$scope.user.password) {
			$scope.user.createAccountPasswordError = 'Please enter your password';
			return;
		}		
		
		$http({
			url: '../LoginServlet', 
			method : 'POST',
			data: $scope.user,
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {

			if (data.success != 1) {
				$scope.user.createAccountNameExistError = 'User name ' + $scope.user.name + ' already exists. Please choose another one';
				return;
			}
			
			$scope.user.success = data.success;
			$scope.user.fieldName = $scope.user.name;
			$scope.user.id = data.userId;
			giftServices.updateUserInfo($scope.user.id, $scope.user.name);
			$("#alert-success").delay(5000).fadeOut(1000);
			
			
			
		});
		
	};
	
	

	
	
	
	
	$scope.login = function() {
		$scope.user.action = 'login';
		
		$scope.user.nameError = '';
		$scope.user.passwordError = '';
		$scope.user.registerMessage = '';
		
		if ($scope.user.name === '' || !$scope.user.name) {
			$scope.user.nameError = 'Please enter your user name';
			return;
		}
		
		if ($scope.user.password === '' || !$scope.user.password) {
			$scope.user.passwordError = 'Please enter your password';
			return;
		}
		
		$http({
			url: '../LoginServlet', 
			method : 'POST',
			data: $scope.user,
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			if (data.success == 1) {
				
				$scope.user.fieldName = data.name;
				$scope.user.id = data.userId;
				$scope.user.unreadMessagesCount = data.unreadMessagesCount;
				
				giftServices.updateUserInfo($scope.user.id, $scope.user.name, $scope.user.unreadMessagesCount);
				$scope.user.success = data.success;
				
				/* REDIRECT TO VIEW */
				
				$location.path("/#viewGifts/");
				
			} else {
				$scope.user.registerMessage = 'Click here to create account';
			}
			
		});
	};

  }]);


/* Make Gift Controller */


giftsControllers.controller('MakeGiftController', ['$scope', '$http',
  function($scope, $http) {
	$scope.gift = {};
	
	$scope.saveGiftInfo = function() {
	
		$scope.gift.userId = $scope.user.id;
		
		$scope.gift.userName = $scope.user.fieldName;
		
		$scope.gift.city = $scope.city;
		
		console.log ($scope.gift);
		
		$http({
			url: '../SetGiftsInfoServlet', 
			method : 'POST',
			data: $scope.gift,
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			$scope.gift.userIdError = data.userIdError;
			$scope.gift.cityError = data.cityError;
			$scope.gift.nameError = data.nameError;
			
			
		});
		
	};
	
  }]);


/* uploadFileController */

giftsControllers.controller('uploadFileController', ['$scope', '$http',
    function($scope, $http) {
	
	$scope.uploadFile = function(){
        var file = $scope.myFile;
        
        
        if (file) {
        	
            var formData = new FormData();
            formData.append('file', file);
            
            $http({
    			url: '../SetGiftPicturesServlet',
    			method : 'POST',
    			//data: $scope.picGifts,
    			data: formData,
    			transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
    		}).success(function(data) {
    			$scope.gift.giftId = data.giftId;
    			$scope.saveGiftInfo();
    		}).error(function(data){
            });
        	
        } else {
        	$scope.saveGiftInfo();
        }
        
        

        
    };
	
	}
]);