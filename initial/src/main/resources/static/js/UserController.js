(function () {
    'use strict';
    function UserDialogController(user, newUser, UserService, NotificationService, $scope, $mdDialog, $log) {
        $log.info('UserDialogController:newUser=' + newUser + ':user=' + JSON.stringify(user));

        $scope.newUser = newUser;
        $scope.user = user;
        $scope.errorMessages = [];
        $scope.dateOfBirth = !newUser && user.dateOfBirth ? moment(user.dateOfBirth, 'YYYY-MM-DD').toDate() : null;
        $scope.dialogTitle = $scope.newUser ? 'Create User' : 'Edit User';
        $log.info('scope values init:' + $scope.newUuser + ':' + $scope.dialogTitle + ':' + $scope.dateOfBirth + ':' + $scope.user);
        $scope.cancel = function () {
            $scope.errorMessages = [];
            $log.info('UserDialogController.cancel');
            $mdDialog.cancel();
        };
        $scope.save = function () {
            $scope.errorMessages = [];
            $scope.user.dateOfBirth = moment($scope.dateOfBirth).format('YYYY-MM-DD');
            $log.info('UserDialogController.save');
            var promise = $scope.newUser ? UserService.createUser($scope.user) : UserService.saveUser($scope.user);
            promise.then(function (user) {
                $log.info('user=' + JSON.stringify(user));
                $mdDialog.hide(user);
                NotificationService.toastMessage($scope.newUser ? 'User Created' : 'User Saved');
            }, function (response) {
                $log.error('Error response:' + response.status + ':' + response.statusText + ':' + JSON.stringify(response.data));
                if (response.status == 409 && response.statusText == 'Conflict') {
                    $scope.userForm.userId.$setValidity('unique', false);
                } else {
                    var errorData = response.data;
                    $scope.errorMessages.push(errorData.message);
                }
            });
        };
    };

    angular.module('springDataRestDemo').controller('UserController',
        ['UserService', 'NotificationService', '$scope', '$q', '$mdMedia', '$mdSidenav', '$mdBottomSheet', '$mdDialog', '$log',
            function (UserService, NotificationService, $scope, $q, $mdMedia, $mdSidenav, $mdBottomSheet, $mdDialog, $log) {
                $scope.openSidenav = function () {
                    $mdSidenav('sideNav').open();
                };
                $scope.selected = [];
                $scope.userSelected = 0;
                $scope.users = null;
                $scope.selectedUser = null;
                $scope.iconStyle = {'medium-icons':$mdMedia('sm'), 'large-icons': $mdMedia('md'), 'x-large-icons': $mdMedia('gt-md')};
                $scope.promise = UserService.loadAllUsers();
                $scope.promise.then(function (users) {
                    $log.debug('loaded ' + users.length + ' users');
                    for (var i in users) {
                        users[i].selected = false;
                    }
                    $scope.users = users;
                }, function (response) {
                    $log.error('Error response:' + response.status + ':' + response.statusText);
                });

                $scope.deleteUsers = function (ev) {
                    var userMessage = 'You have selected to delete:';
                    var u;
                    for (u in $scope.selected) {
                        var user = $scope.selected[u];
                        userMessage = userMessage + ' ' + user.userId + ' (' + user.fullName + ')';
                    }
                    // TODO refactor so that dialog shows progress and remains open until completion.
                    var confirm = $mdDialog.confirm()
                        .title('Do you want to delete users?')
                        .textContent(userMessage)
                        .ariaLabel('Delete Users')
                        .targetEvent(ev)
                        .ok('Delete')
                        .cancel('Cancel');
                    $mdDialog.show(confirm).then(function () {
                        var promises = [];
                        for (u in $scope.selected) {
                            var user = $scope.selected[u];
                            $log.debug('Deleting:' + user.userId);
                            promises.push(UserService.deleteUser(user));
                        }
                        $q.all(promises).then(function () {
                            var deleteCount = $scope.selected.length;
                            for (u in $scope.selected) {
                                var user = $scope.selected[u];
                                var index = $scope.users.indexOf(user);
                                assertTrue(index >= 0, 'Could not find:' + user.userId + ' in ' + JSON.stringify($scope.users, null, 2));
                                $scope.users.splice(index, 1);
                            }
                            $scope.selected = [];
                            NotificationService.toastMessage('Deleted ' + deleteCount + ' Users');
                        }, function (response) {
                            $log.error('Deletion error:' + JSON.stringify(data, null, 2));
                            NotificationService.toastError('Deletion incompleted: ' + data.statusLine, 'Close');
                        });
                    }, function () {
                        $log.debug('Cancelled dialog');
                    });
                };
                $scope.checkSelected = function (user) {
                    var index = $scope.selected.indexOf(user);
                    if (index == -1 && user.selected) {
                        $scope.selected.push(user);
                    } else if (index >= 0 && !user.selected) {
                        $scope.selected.splice(index, 1);
                    }
                };
                $scope.selectUser = function (user) {
                    user.selected = !user.selected;
                    $scope.checkSelected(user);
                };

                $scope.deleteUser = function (ev, user) {
                    var userMessage = 'Do you want to delete ' + user.userId + ' (' + user.fullName + ')?';
                    var confirm = $mdDialog.confirm()
                        .title('Confirm deleting a user?')
                        .textContent(userMessage)
                        .ariaLabel('Delete User')
                        .targetEvent(ev)
                        .ok('Delete')
                        .cancel('Cancel');
                    $mdDialog.show(confirm).then(function () {
                        UserService.deleteUser(user).then(function (data) {
                            var index = $scope.users.indexOf(user);
                            assertTrue(index >= 0, 'Could not find:' + user.userId + ' in ' + JSON.stringify($scope.users, null, 2));
                            $scope.users.splice(index, 1);
                            NotificationService.toastMessage('Deleted User:' + user.userId);
                        }, function (data) {
                            $log.error('Deletion error:' + JSON.stringify(data, null, 2));
                            NotificationService.toastError('Deletion failed: ' + data.statusLine, 'Close');
                        });
                    }, function () {
                        $log.debug('Cancelled deletion');
                    });
                };
                $scope.editUser = function (ev, user) {
                    if (user == undefined) {
                        $scope.selectedUser = $scope.selected[0];
                    } else {
                        $scope.selectedUser = user;
                    }
                    $mdDialog.show({
                        parent: angular.element(document.body),
                        controller: UserDialogController,
                        templateUrl: 'templates/user-dialog.html',
                        targetEvent: ev,
                        bindToController: true,
                        UserService: UserService,
                        locals: {
                            user: $scope.selectedUser,
                            newUser: false
                        }
                    }).then(function (user) {
                        $scope.selectedUser = user;
                        for (var i in $scope.users) {
                            var item = $scope.users[i];
                            if (item.userId == user.userId) {
                                $scope.users[i] = user;
                            }
                        }
                    });
                };
                $scope.addUser = function (ev) {
                    $scope.selectedUser = {fullName: '', userId: '', emailAddress: '', dateOfBirth: null};
                    $log.info('adding user');
                    $mdDialog.show({
                        parent: angular.element(document.body),
                        controller: UserDialogController,
                        templateUrl: 'templates/user-dialog.html',
                        NotificationService: NotificationService,
                        targetEvent: ev,
                        bindToController: true,
                        UserService: UserService,
                        locals: {
                            user: $scope.selectedUser,
                            newUser: true
                        }
                    }).then(function (user) {
                        $scope.selectedUser = user;
                        $scope.users.push(user);
                    });
                };
            }]);

})();