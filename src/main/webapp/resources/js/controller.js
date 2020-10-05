var cartApp = angular.module('cartApp', []);

//cartApp이라는 모듈안에 cartCtrl이라는 컨트롤러가 있다.
//controller의 두번째 매개변수인 function은 컨트롤러의 생성자
//scope(model)이랑 http(httpRequest받기위해서)모듈을 의존성 주입을 받고 있음
cartApp.controller("cartCtrl", function($scope, $http) {
	
	//scope의 메소드 등록
	$scope.initCartId = function(cartId) {
		$scope.cartId = cartId;
		$scope.refreshCart();
	};
	
	//카트 내용 조회
	$scope.refreshCart = function() {
		//CartRestController의 getCartById 불림
		//short포맷
		$http.get('/eStore/api/cart/'+$scope.cartId).then (
				function successCallback(response) {
					//성공하면 cart에 저장
					$scope.cart = response.data;
				});
	};
	
	//카트 제거
	$scope.clearCart = function() {
		$scope.setCsrfToken();
		//clearCart()부름
		//long포맷
		$http({
			method : 'DELETE',
			url : '/eStore/api/cart/' + $scope.cartId
		}).then(function successCallback() {
			$scope.refreshCart();
		}, function errorCallback(response) {
			console.log(response.data);
		});
	};
	
	$scope.addToCart = function(productId) {
		$scope.setCsrfToken();
		$http.put('/eStore/api/cart/add/'+productId).then(
				function successCallback() {
					alert("Product successfully added to the cart!");
				}, function errorCallback() {
					alert("Adding to the cart failed!");
				});
	};
	
	$scope.removeFromCart = function(productId) {
		$scope.setCsrfToken();
		$http({
			method : 'DELETE',
			url : '/eStore/api/cart/cartitem/' + productId
		}).then(function successCallback() {
			$scope.refreshCart();
		}, function errorCallback(response) {
			console.log(response.data);
		});
	};
	
	$scope.updateQuantity = function(productId, value) {
		$scope.setCsrfToken();
		$http({
			method : 'PUT',
			url : '/eStore/api/cart/cartitem/quantity/' + productId + '/' + value
		}).then(function successCallback() {
			$scope.refreshCart();
		}, function errorCallback(response) {
			console.log(response.data);
		});
		
	};

	$scope.calGrandTotal = function() {
		var grandTotal = 0;
		
		for(var i=0; i<$scope.cart.cartItems.length; i++) {
			grandTotal += $scope.cart.cartItems[i].totalPrice;
		}
		
		return grandTotal;
	};
	
	$scope.setCsrfToken = function() {
		var csrfToken = $("meta[name='_csrf']").attr("content");
		var csrfHeader = $("meta[name='_csrf_header']").attr("content");
		
		$http.defaults.headers.common[csrfHeader] = csrfToken;
	}
});