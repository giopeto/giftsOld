'use strict';
angular.module('giftServices', []).factory('giftServices', function($rootScope){
    var service = {};
    service._id = 0;
    
    service.updateUserInfo = function(userId, userName, unreadMessagesCount){
        this._id = userId;
        
        if (typeof(Storage) != "undefined") {
            
            localStorage.setItem("_id", this._id);
            localStorage.setItem("name", userName);
            localStorage.setItem("unreadMessagesCount", unreadMessagesCount);
           
        } else {
            console.log("Sorry, your browser does not support Web Storage...");
        }

    };

    service.getUserInfo = function(){
       
        if (typeof(Storage) != "undefined") {
            
        	var user = {};
        	user.id = localStorage.getItem("_id");
        	user.fieldName = localStorage.getItem("name");
        	user.unreadMessagesCount = localStorage.getItem("unreadMessagesCount");
        	return user;
           
        } else {
            console.log("Sorry, your browser does not support Web Storage...");
        }
          
    };

    service.logOut = function(x){
    	
        if (typeof(Storage) != "undefined") {
        	localStorage.removeItem("_id");
        	localStorage.removeItem("name");   
        } else {
            console.log("Sorry, your browser does not support Web Storage...");
        }
        
    };
    
   /* service.checkImage = function(viewGifts){
    	 
    	angular.forEach(viewGifts, function (value, key) {
    		
    		
    		if (!value.image) {
    			value.imageTemplate = "";
    		}
    		
    		
    	});
    };*/
    
    return service;
});
