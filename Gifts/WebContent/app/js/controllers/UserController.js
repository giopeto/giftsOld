'use strict';

/* Controllers */

var userControllers = angular.module('userControllers', []);

userControllers.controller('userPageController', [ '$scope', '$http', function($scope, $http) {

			$http({
				url : '../UserServlet',
				method : 'POST',
				data: {'id': $scope.user.id, 'action': 'getUserInfo'},
				headers : {
					'Content-Type' : 'application/json'
				}
			}).success(function(data) {
				$scope.editUserInfo  = data.user;
				$scope.user  = data.user;
				$scope.user.retypePassword = data.user.password;
			});

			$scope.editAccount = function() {
				
				if ($scope.user.password === '' || !$scope.user.password) {
					$scope.user.createAccountPasswordError = 'Please enter your password';
					return;
				}		
				
				if ($scope.user.password != $scope.user.retypePassword) {
					$scope.user.createAccountPasswordError = 'Password dont match';
					return;
				}
				
				$scope.user.action = 'editAccount';
				
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
						giftServices.updateUserInfo($scope.user.id, $scope.user.name);
						$scope.user.success = data.success;
						
						/* REDIRECT TO VIEW */
						
					}
					$("#alert-success").delay(5000).fadeOut(1000);
				});
			};
			
			
			
			
			
			
}]);

userControllers.controller('messageController', [ '$scope', '$http', '$routeParams', '$location', function($scope, $http, $routeParams, $location) {
	$scope.messages = {};
	$scope.viewMessageGiftsInfo = {};
	$scope.viewMessageGiftsInfo.id = $routeParams.messageGiftId;
	$scope.viewMessageGiftsInfo.action = 'getSingleGifts';
	
	$http({
		url: '../ViewGiftsController', 
		method : 'POST',
		data: $scope.viewMessageGiftsInfo,
		headers: {
            'Content-Type': 'application/json'
        }
	}).success(function(data) {
		$scope.viewMessageGiftsInfo = data.gifts;
	});
	
	$scope.saveMessage = function(args) {
		
		if ($scope.messages.message) {
			$scope.messages.fromId = $scope.user.id;
			$scope.messages.action = "saveMessage";
			$scope.messages.giftId = args.giftId;
			$scope.messages.toId = args.toId;
			if (args.messageId !== "") {
				$scope.messages.messageId = args.messageId;
			}
			
			$http({
				url: '../UserServlet', 
				method : 'POST',
				data: $scope.messages,
				headers: {
		            'Content-Type': 'application/json'
		        }
			}).success(function(data) {
				$location.path("/userMessage");
				
			});
		} else {
			alert ("No message");
		}
		
		
		
	};
	
	
}]);

userControllers.controller('viewMessageController', [ '$scope', '$http', '$location', function($scope, $http, $location) {

	$scope.viewMessages = {};
	$scope.messages = {};
	$scope.messages.newMessage = {};
	
	
	$scope.getMessages = function (){
		
		$scope.viewMessages.action = 'viewMessage';
		$scope.viewMessages.userId = $scope.user.id;
		
		$http({
			url: '../UserServlet', 
			method : 'POST',
			data: $scope.viewMessages,
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			$scope.viewMessages = data;
			//$scope.viewMessages.newMessage = {};
		});
	};
	$scope.getMessages();
	
	/* DUPLICATE METHOd*/
	$scope.saveMessage = function(args) {
		
		if ($scope.messages.newMessage[args.messageId]) {
			$scope.messages.fromId = $scope.user.id;
			$scope.messages.message = $scope.messages.newMessage[args.messageId];
			$scope.messages.action = "saveMessage";
			$scope.messages.giftId = args.giftId;
			$scope.messages.toId = args.toId;
			if (args.messageId !== "") {
				$scope.messages.messageId = args.messageId;
			}
			
			$http({
				url: '../UserServlet', 
				method : 'POST',
				data: $scope.messages,
				headers: {
		            'Content-Type': 'application/json'
		        }
			}).success(function(data) {
				$scope.getMessages();
				//$location.path("/#userMessage/");
				$scope.messages.newMessage[args.messageId] = "";
			});
		} else {
			alert ("No message");
		}
		
		
	};
	
	/* END DUPLICATE METHOd*/

}]);


userControllers.controller('logOutController', [ '$scope', '$http', 'giftServices',function($scope, $http, giftServices) {
	
	giftServices.logOut();
	$scope.$parent.user = {};
	
}]);

