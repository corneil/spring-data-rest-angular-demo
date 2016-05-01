(function () {
    'use strict';
    function MemberAddDialogController(selectedGroup, UserService, MemberService, NotificationService, $scope, $mdDialog, $log, $q) {
        $scope.selectedGroup = selectedGroup;
        $scope.selectedUser = null;
        $scope.selectedUsers = [];
        $scope.searchText = null;
        $scope.busy = false;
        $scope.transformChip = function (chip) {
            if (angular.isObject(chip)) {
                return chip;
            }
            NotificationService.toastError('Select an existing user');
            return null;
        };
        function createMember(user) {
            var createDeferred = $q.defer();
            assertNotNull($scope.selectedGroup);
            assertNotNull($scope.selectedGroup._group);
            var newMember = {enabled: true, memberOfgroup: $scope.selectedGroup._group._links.self.href, member: user._links.self.href};
            MemberService.createMember(newMember).then(function (member) {
                createDeferred.resolve({member: member, user: user});
            }, function (response) {
                createDeferred.reject(response);
            });
            return createDeferred.promise;
        };
        $scope.cancel = function () {
            $log.info('MemberAddDialogController.cancel');
            $mdDialog.cancel();
        };
        $scope.save = function (ev) {
            $scope.busy = true;
            var promises = [];
            for (var i in $scope.selectedUsers) {
                promises.push(createMember($scope.selectedUsers[i]));
            }
            $q.all(promises).then(function (members) {
                for (var i in members) {
                    var member = members[i];
                    MemberService.addMember($scope.selectedGroup, member.member, member.user);
                }
                $scope.busy = false;
                $mdDialog.hide(members);
            }, function (response) {
                $scope.busy = false;
                $log.error('save:all:error:' + JSON.stringify(response, null, 2));
                NotificationService.toastError(response.statusText, 'Close');
            });
        };
        $scope.queryMemberSearch = function (input) {
            $scope.busy = false;
            var deferred = $q.defer();
            $scope.users = [];
            $scope.userPromise = UserService.searchPartial(input);
            $scope.userPromise.then(function (users) {
                $log.debug('loaded ' + users.length + ' users input=' + input);
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
    }

    angular.module('springDataRestDemo').controller('MemberController', ['MemberService', 'UserService', 'NotificationService', '$scope', '$q', '$mdSidenav', '$mdMedia', '$mdDialog', '$log',
        function (MemberService, UserService, NotificationService, $scope, $q, $mdSidenav, $mdMedia, $mdDialog, $log) {
            $scope.groups = null;
            $scope.loading = false;
            $scope.selectedGroup = null;
            $scope.selectedMember = null;
            $scope.searchMember = null;
            $scope.promise = MemberService.loadAllMembers();
            $scope.promise.then(function (groups) {
                for (var g in groups) {
                    var group = groups[g];
                    group.$modified = false;
                    assertNotNull(group._group);
                }
                $scope.groups = groups;
            }, function (response) {
                $log.error('loadAllMembers:response:' + JSON.stringify(response));
                NotificationService.toastError(response.statusText, 'Close');
            });
            $scope.users = null;
            $scope.closeRight = function () {
                $mdSidenav('right').close();
            };
            $scope.selectGroup = function (group) {
                if (group) {
                    $scope.loading = true;
                    MemberService.loadGroupMembers(group).then(function () {
                        $scope.selectedGroup = group;
                        $scope.loading = false;
                        $mdSidenav('right').open();
                    }, function (response) {
                        $scope.selectedGroup = null;
                        $log.error('selectGroup:response:' + JSON.stringify(response));
                        NotificationService.toastError(response.statusText, 'Close');
                        $scope.loading = false;
                    });
                }
            };
            $scope.deleteMember = function (ev, member) {
                var index = $scope.selectedGroup.members.indexOf(member);
                assertTrue(index >= 0, 'Expected member in selectedGroup.members');
                var userMessage = 'Do you want to remove ' + member.userId + ' (' + member.fullName + ')?';
                var confirm = $mdDialog.confirm()
                    .title('Confirm removing a user?')
                    .textContent(userMessage)
                    .ariaLabel('Remove Member')
                    .targetEvent(ev)
                    .ok('Remove')
                    .cancel('Cancel');
                $mdDialog.show(confirm).then(function () {
                    MemberService.deleteMember(member._member).then(function (response) {
                        $scope.selectedGroup.members.splice(index, 1);
                        NotificationService.toastMessage('Member removed');
                    }, function (response) {
                        NotificationService.toastMessage('Member remove failed:' + response.statusText);
                    });
                });
            };
            $scope.toggleEnabledMember = function (ev, member) {
                member._member.enabled = !member._member.enabled;
                MemberService.saveEnabledMember(member._member).then(function (response) {
                    $log.debug('disableMember:response:' + JSON.stringify(response, null, 2));
                    member.enabled = member._member.enabled;
                    NotificationService.toastMessage('Member ' + member.userId + ' (' + member.fullName + ') ' + (member.enabled ? 'enabled' : 'disabled'));
                }, function (response) {
                    $log.error('disableMember:response:' + JSON.stringify(response, null, 2));
                    NotificationService.toastError('Error disabling/enabling:' + response.statusText);
                });
            }
            $scope.addMember = function (ev) {
                assertTrue($scope.selectedGroup != null);
                $mdDialog.show({
                    parent: angular.element(document.body),
                    controller: MemberAddDialogController,
                    templateUrl: 'templates/add-members.html',
                    targetEvent: ev,
                    bindToController: true,
                    locals: {
                        selectedGroup: $scope.selectedGroup
                    }
                }).then(function (members) {
                    NotificationService.toastMessage('Added ' + members.length + ' members');
                });
            };

            $scope.openSidenav = function () {
                $mdSidenav('sideNav').open();
            };
        }]);
})();