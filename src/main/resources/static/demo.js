angular.module('DemoApp', [ 'ngMaterial' ]);
angular.module('DemoApp').controller('DemoController', DemoController);


function DemoController($http, $mdDialog ){
	var vm = this;
	vm.firstName = '';
	vm.lastName = '';
	vm.queryParams = function(){
		console.log('firstName:' + vm.firstName);
		console.log('lastName:' + vm.lastName);
		$http.get('queryParams', {
			method: 'GET',
			params: {
				firstName: vm.firstName,
				lastName: vm.lastName
			}
		}).then(function success(response){
		     var alert = $mdDialog.alert()
		        .title('User found')
		        .content('First Name:' + response.data.firstName + ' Last Name: ' + response.data.lastName)
		        .ok('Close');
		      $mdDialog
		          .show( alert )
		          .finally(function() {
		            alert = undefined;
		          });
		}, function error(response){
		     var alert = $mdDialog.alert()
		        .title('User no found')
		        .content('Status:' + response.status + ' data: ' + response.data)
		        .ok('Close');
		      $mdDialog
		          .show( alert )
		          .finally(function() {
		            alert = undefined;
		          });
		});
	}
}