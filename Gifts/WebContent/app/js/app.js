'use strict';

var gifts = angular.module('gifts', 
['ngRoute', 'giftsControllers', 'viewControllers', 'userControllers', 'giftsDirectives', 'giftServices']);

gifts.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/login', {
		templateUrl: 'templates/login.html',
		controller: 'loginController'
	}).when('/viewGifts', {
		templateUrl: 'templates/viewGifts.html',
		controller: 'viewController'
	}).when('/makeGift', {
		templateUrl : 'templates/makeGift.html',
		controller : 'MakeGiftController'
	}).when('/register', {
		templateUrl: 'templates/createAccount.html',
		controller: 'loginController'
	}).when('/userPage', {
		templateUrl: 'templates/userPage.html',
		controller: 'userPageController'
	}).when('/viewGift/:viewGiftId', {
		templateUrl: 'templates/viewGift.html',
		controller: 'viewController'		
	}).when('/userMessage/', {
		templateUrl: 'templates/viewMessages.html',
		controller: 'viewMessageController'	
	}).when('/message/:messageGiftId', {
		templateUrl: 'templates/giftMessage.html',
		controller: 'messageController'	
	}).when('/logOut/:logOut', {
		templateUrl: 'templates/viewGift.html',
		controller: 'logOutController'	
		
	
	}).when('/myGifts', {
		templateUrl: 'templates/myGifts.html',
		controller: 'myGiftsController'
			
			
			
	}).otherwise({
		redirectTo: '/viewGifts',
		controller: 'viewController'
	});
} ]);


