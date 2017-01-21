angular.module('DemoApp', [ 'ngMaterial' , 'ngFileUpload']);
angular.module('DemoApp').controller('DemoController', DemoController);


function DemoController($http, $mdDialog, Upload ){
	var vm = this;
	vm.firstName = '';
	vm.lastName = '';
	vm.id = '';
	vm.filesToUpload = [];
	vm.showFound = function(response){
     var alert = $mdDialog.alert()
        .title('User found')
        .content('Id:' + response.data.id +' First Name:' + response.data.firstName + ' Last Name: ' + response.data.lastName)
        .ok('Close');
      $mdDialog
          .show( alert )
          .finally(function() {
            alert = undefined;
          });
	}
	vm.showSaved = function(response){
		var alert = $mdDialog.alert()
		.title('User saved')
		.content('Id:' + response.data.id +' First Name:' + response.data.firstName + ' Last Name: ' + response.data.lastName)
		.ok('Close');
		$mdDialog
		.show( alert )
		.finally(function() {
			alert = undefined;
		});
	}
	vm.showFoundError = function(response){
	     var alert = $mdDialog.alert()
	        .title('User not found')
	        .content('Status:' + response.status + ' data: ' + response.data)
	        .ok('Close');
	      $mdDialog
	          .show( alert )
	          .finally(function() {
	            alert = undefined;
	          });
	}
	vm.showSavedError = function(response){
		var alert = $mdDialog.alert()
		.title('User not saved')
		.content('Status:' + response.status + ' data: ' + response.data)
		.ok('Close');
		$mdDialog
		.show( alert )
		.finally(function() {
			alert = undefined;
		});
	}
	
	vm.queryParams = function(){
		console.log('firstName:' + vm.firstName);
		console.log('lastName:' + vm.lastName);
		$http.get('queryParams', {
			params: {
				firstName: vm.firstName,
				lastName: vm.lastName
			}
		}).then(vm.showFound, vm.showFoundError);
	}
	vm.pathParams = function(){
		console.log('id:' + vm.id);
		$http.get('pathParams/' + vm.id).then(vm.showFound, vm.showFoundError);
	}
	vm.postParamsAngular = function(){
		console.log('id:' + vm.id);
		console.log('firstName:' + vm.firstName);
		console.log('lastName:' + vm.lastName);
		$http.post('postParamsAngular', {
			id: vm.id,
			firstName: vm.firstName,
			lastName: vm.lastName
		}).then(vm.showSaved, vm.showSavedError);
	}
	vm.postParamsClassic = function(){
		console.log('id:' + vm.id);
		console.log('firstName:' + vm.firstName);
		console.log('lastName:' + vm.lastName);
		var jqxhr = $.post( "postParamsClassic",{
			id: vm.id,
			firstName: vm.firstName,
			lastName: vm.lastName
		}, function(response){ 
			vm.showSaved({
				data:response
				}
			)}
		).fail(function(response){
			vm.showSavedError({
				status: response.status,
				data: response.responseText
			});
		});
	}
	vm.uploadFileAngular = function(){
		console.log('id:' + vm.id);
		console.log('firstName:' + vm.firstName);
		console.log('lastName:' + vm.lastName);
		console.log('filesToUpload:' + vm.filesToUpload);
		Upload.upload({
			url: 'uploadFileAngular', 
			data: {
				id: vm.id,
				firstName: vm.firstName,
				lastName: vm.lastName,
				filesToUpload: vm.filesToUpload
			}	
		}).then(vm.showSaved, vm.showSavedError);
	}
}