(function () {
    'use strict';
    function GroupDialogController(group, newGroup, GroupService, UserService, $scope, $mdDialog, NotificationService, $q, $log) {
        $log.info('GroupDialogController:newGroup=' + newGroup + ':group=' + JSON.stringify(group));
        $scope.newGroup = newGroup;
        $scope.group = group;
        $scope.searchText = null;
        $scope.busy = false;
        $scope.dialogTitle = $scope.newGroup ? 'Create Group' : 'Edit Group';
        $log.info('scope values init:' + $scope.newGroup + ':' + $scope.dialogTitle + ':' + ':' + $scope.group);
        $scope.cancel = function () {
            $log.info('GroupDialogController.cancel');
            $mdDialog.cancel();
        };
        $scope.queryMemberSearch = function (input) {
            $scope.busy = true;
            var deferred = $q.defer();
            $scope.users = [];
            $scope.userPromise = UserService.searchPartial(input);
            $scope.userPromise.then(function (users) {
                $scope.users = users;
                deferred.resolve(users);
                $scope.busy = false;
            }, function (response) {
                $log.error('Error response:' + JSON.stringify(response, null, 2));
                NotificationService.toastError(response.statusText);
                deferred.reject(response);
                $scope.busy = false;
            });
            return deferred.promise;
        };
        $scope.save = function () {
            var promise = $scope.newGroup ? GroupService.createGroup($scope.group) : GroupService.saveGroup($scope.group);
            promise.then(function (group) {
                NotificationService.toastMessage($scope.newGroup ? 'Group Created' : 'Group Saved');
                $mdDialog.hide(group);
            }, function (response) {
                $log.error('Error response:' + response.status + ':' + response.statusText + ':' + JSON.stringify(response.data));
                if (response.status == 409 && response.statusText == 'Conflict') {
                    $scope.groupForm.groupName.$setValidity('unique', false);
                } else {
                    var errorData = response.data;
                    $scope.errorMessages.push(errorData.message);
                }
            });
        };
    };

    angular.module('springDataRestDemo').controller('GroupController', ['GroupService', 'UserService', 'NotificationService', '$scope', '$q', '$mdMedia', '$mdSidenav', '$mdBottomSheet', '$mdDialog', '$log',
        function (GroupService, UserService, NotificationService, $scope, $q, $mdMedia, $mdSidenav, $mdBottomSheet, $mdDialog, $log) {
            $scope.selected = [];
            $scope.groupSelected = 0;
            $scope.groups = [];
            $scope.selectedGroup = null;
            $scope.promise = GroupService.loadAllGroups(true);
            $scope.promise.then(function (groups) {
                $scope.groups = groups;
                for (var i in groups) {
                    groups[i].selected = false;
                }
            }, function (response) {
                $log.error('Error response:' + response.status + ':' + response.statusText);
            });
            $scope.openSidenav = function () {
                $mdSidenav('sideNav').open();
            };
            $scope.editGroup = function (ev) {
                $scope.selectedGroup = $scope.selected[0];
                $log.info('edit group :' + $scope.selectedGroup.groupName);
                $mdDialog.show({
                    parent: angular.element(document.body),
                    controller: GroupDialogController,
                    templateUrl: 'templates/group-dialog.html',
                    targetEvent: ev,
                    bindToController: true,
                    locals: {
                        group: $scope.selectedGroup,
                        users: $scope.users,
                        newGroup: false
                    }
                }).then(function (group) {
                    $log.info('updating group in array:' + JSON.stringify(group));
                    $scope.selectedGroup = group;
                    for (var i in $scope.groups) {
                        var item = $scope.groups[i];
                        if (item.groupId == group) {
                            $scope.groups[i] = group;
                        }
                    }
                });
            };
            $scope.addGroup = function (ev) {
                $scope.selectedGroup = {groupName: '', _groupOwner: undefined};
                $log.info('adding group');
                $mdDialog.show({
                    parent: angular.element(document.body),
                    controller: GroupDialogController,
                    templateUrl: 'templates/group-dialog.html',
                    targetEvent: ev,
                    bindToController: true,
                    locals: {
                        group: $scope.selectedGroup,
                        users: $scope.users,
                        newGroup: true
                    }
                }).then(function (group) {
                    $log.info('adding group to array:' + JSON.stringify(group));
                    $scope.selectedGroup = group;
                    $scope.groups.push(group);
                });
            };
            $scope.deleteGroups = function (ev) {
                var groupMessage = 'You have selected to delete:';
                var u;
                for (u in $scope.selected) {
                    var user = $scope.selected[u];
                    groupMessage = groupMessage + ' ' + user.groupName;
                }
                var confirm = $mdDialog.confirm()
                    .title('Do you want to delete groups?')
                    .textContent(groupMessage)
                    .ariaLabel('Delete Groups')
                    .targetEvent(ev)
                    .ok('Delete')
                    .cancel('Cancel');
                $mdDialog.show(confirm).then(function () {
                    var promises = []
                    for (u in $scope.selected) {
                        var group = $scope.selected[u];
                        var promise = GroupService.deleteGroup(group);
                        promises.push(promise);
                    }
                    $q.all(promises).then(function (data) {
                        $log.info('Deletion completed:' + data);
                        var deleteCount = $scope.selected.length;
                        for (u in $scope.selected) {
                            var group = $scope.selected[u];
                            var rg = $scope.groups.indexOf(group);
                            assertTrue(rg >= 0, 'Expected group index in groups');
                            $scope.groups.splice(rg, 1);
                        }
                        $scope.selected = [];
                        NotificationService.toastMessage('Deleted ' + deleteCount + ' Groups');
                    }, function (data) {
                        $log.error('Deletion incompleted:' + JSON.stringify(data));
                        NotificationService.toastError('Deletion incompleted: ' + data.statusLine, 'Close');
                    });
                }, function () {
                    $log.debug('Cancelled deletion');
                });
            }
        }]);

})();