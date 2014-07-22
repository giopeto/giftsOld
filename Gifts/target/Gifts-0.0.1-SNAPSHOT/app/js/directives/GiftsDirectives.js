'use strict';

var giftsDirectives = angular.module('giftsDirectives', []);

/* File Model Directives */

giftsDirectives.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);

giftsDirectives.directive('cityAutocomplete', function() {
	
	
	var getCitiesTemplate = function () {
		/*var cities2 = {};
		$http({
			url: '../UserServlet', 
			method : 'POST',
			data: {'action': 'getCity'},
			headers: {
	            'Content-Type': 'application/json'
	        }
		}).success(function(data) {
			
			cities = data;
			
			
		});
		*/
		var cities = [{"_id": "0", "city": "Всички Градове"},{"_id": "1", "city": "София"},{"_id": "2", "city": "Пловдив"},{"_id": "3", "city": "Варна"},{"_id": "4", "city": "Бургас"},{"_id": "21", "city": "Стара Загора"},{"_id": "19", "city": "Русе"},{"_id": "45", "city": "Асеновград"},{"_id": "47", "city": "Банско"},{"_id": "26", "city": "Берковица"},{"_id": "6", "city": "Благоевград"},{"_id": "27", "city": "Ботевград"},{"_id": "7", "city": "Велико Търново"},{"_id": "58", "city": "Велинград"},{"_id": "8", "city": "Видин"},{"_id": "9", "city": "Враца"},{"_id": "10", "city": "Габрово"},{"_id": "41", "city": "Горна Оряховица"},{"_id": "36", "city": "Димитровград"},{"_id": "11", "city": "Добрич"},{"_id": "56", "city": "Дряново"},{"_id": "35", "city": "Дупница"},{"_id": "51", "city": "Елин Пелин"},{"_id": "55", "city": "Казанлък"},{"_id": "29", "city": "Козлодуй"},{"_id": "12", "city": "Кърджали"},{"_id": "13", "city": "Кюстендил"},{"_id": "14", "city": "Ловеч"},{"_id": "30", "city": "Лом"},{"_id": "28", "city": "Мездра"}
		,{"_id": "49", "city": "Мелник"},{"_id": "15", "city": "Монтана"},{"_id": "32", "city": "Несебър"},{"_id": "59", "city": "Омуртаг"},{"_id": "16", "city": "Пазарджик"},{"_id": "17", "city": "Перник"},{"_id": "44", "city": "Петрич"},{"_id": "54", "city": "Пирдоп"},{"_id": "18", "city": "Плевен"},{"_id": "53", "city": "Правец"},{"_id": "34", "city": "Разград"},{"_id": "48", "city": "Разлог"},{"_id": "43", "city": "Сандански"},{"_id": "37", "city": "Свиленград"},{"_id": "42", "city": "Свищов"},{"_id": "52", "city": "Своге"},{"_id": "33", "city": "Севлиево"},{"_id": "57", "city": "Септември"},{"_id": "39", "city": "Силистра"},{"_id": "20", "city": "Сливен"},{"_id": "46", "city": "Смолян"},{"_id": "31", "city": "Созопол"},{"_id": "50", "city": "Троян"},{"_id": "40", "city": "Търговище"},{"_id": "38", "city": "Харманли"},{"_id": "22", "city": "Хасково"},{"_id": "23", "city": "Шумен"},{"_id": "24", "city": "Ямбол"},{"_id": "5", "city": "- друго -"}];
		
		var citiesTemplate = '<select class="form-control" id="e1" name = "city" ng-model = "city" >';
		angular.forEach(cities, function(value, key) {
			citiesTemplate += '<option value = "' + value._id + '">' + value.city + '</option>';
		});
		
		citiesTemplate += '</select>';
		return citiesTemplate;
	
	};
	

	var template = getCitiesTemplate();
	 
	return {
		restrict : 'A',
		template: template,
		scope : {
			city : '='
		},
		link : function(scope, elem, attrs) {
			
		}
	};
});




