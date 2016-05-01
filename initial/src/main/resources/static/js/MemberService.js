(function () {
    'use strict';
    angular.module('springDataRestDemo').service('MemberService', ['$q', '$resource', '$log', 'UserService', 'GroupService',
        function ($q, $resource, $log, UserService, GroupService) {
            var GroupMembers = $resource('/api/group-member', {}, {
                create: {method: 'POST'},
                list: {method: 'GET'}
            });

            function makeGroup(group, groupMap) {
                var groupInfo = {
                    groupName: group.groupName,
                    description: group.description,
                    href: group._links.self.href,
                    _group: group,
                    members: null,
                    _removed: []
                };
                groupMap[group.groupName] = groupInfo;
                return groupInfo;
            }

            function loadGroup(groupMap, member) {
                var deferred = $q.defer();
                GroupService.loadGroup(member._links._memberOfgroup.href).then(function (group) {
                    var groupInfo = {};
                    if (!(group.groupName in groupMap)) {
                        groupInfo = makeGroup(group, groupMap);
                    } else {
                        groupInfo = groupMap[group.groupName];
                    }
                    var deferredMember = makeDeferredMember(member);
                    groupInfo.members.push(deferredMember);
                    deferred.resolve(deferredMember);
                }, function (response) {
                    $log.error('loadGroupMembers:group:response:' + JSON.stringify(response, null, 2));
                    deferred.reject(response);
                });
                return deferred.promise;
            }

            function loadAndAddUser(groupMember) {
                var deferred = $q.defer();
                UserService.loadUser(groupMember._member._links._member.href).then(function (user) {
                    updateMember(user, groupMember);
                    deferred.resolve(groupMember);
                },
                function (response) {
                    $log.error('loadGroupMembers:user:response:' + JSON.stringify(response, null, 2));
                    deferred.reject(response);
                });
                return deferred;
            }

            function loadGroupMembers(group) {
                var memberList = $q.defer();
                if(group.members == undefined || group.members == null) {
                    var MembersForGroup = $resource('/api/group-member/search/findByMemberOfgroup_GroupName?groupName=:groupName', {groupName: '@groupName'});
                    MembersForGroup.get({groupName: group.groupName}).$promise.then(function (groupMembers) {
                        group.members = [];
                        var members = groupMembers._embedded.groupMembers;
                        for (var index in members) {
                            var member = members[index];
                            var deferredMember = makeDeferredMember(member);
                            group.members.push(deferredMember);
                        }
                        memberList.resolve(group.members);
                    }, function (response) {
                        $log.error('loadGroupMembers:failed:' + JSON.stringify(response, null, 2));
                        memberList.reject(response);
                    });
                } else {
                    memberList.resolve(group.members);
                }
                var resultDeferred = $q.defer();
                var promises = [];
                memberList.promise.then(function (members) {
                    for (var i in members) {
                        var groupMember = members[i];
                        if (groupMember.href == undefined || groupMember.href == null) {
                            promises.push(loadAndAddUser(groupMember));
                        }
                    }
                    if (promises.length > 0) {
                        $q.all(promises).then(function () {
                            resultDeferred.resolve(group);
                        }, function (response) {
                            resultDeferred.reject(response);
                        });
                    } else {
                        if (group.members.length == 0) {
                            $log.debug("loadGroupMembers:no promised. empty group");
                        } else {
                            $log.debug("loadGroupMembers:no promised. already loaded");
                        }
                        resultDeferred.resolve({});
                    }
                }, function (response) {
                    $log.error('loadGroupMembers:failed:' + JSON.stringify(response, null, 2));
                    resultDeferred.reject(response);
                });
                return resultDeferred.promise;
            }

            function updateMember(user, member) {
                member.userId = user.userId;
                member.fullName = user.fullName;
                member.emailAddress = user.emailAddress;
                member.href = user._links.self.href;
            }

            function loadAllMembers() {
                var deferred = $q.defer();
                var groupMap = {};
                GroupService.loadAllGroups().then(function (groups) {
                    var result = [];
                    for (var i in groups) {
                        result.push(makeGroup(groups[i], groupMap));
                    }
                    deferred.resolve(result);
                });
                return deferred.promise;
            }

            function makeMember(user, member) {
                return {
                    _member: member,
                    enabled: member == null || member == undefined ? false : member.enabled,
                    userId: user.userId,
                    fullName: user.fullName,
                    emailAddress: user.emailAddress,
                    href: user._links.self.href
                };
            }

            function makeDeferredMember(member) {
                return {
                    _member: member,
                    enabled: member == null || member == undefined ? false : member.enabled,
                };
            }
            function addMember(group, member, user) {
                var deferredMember = makeDeferredMember(member);
                group.members.push(deferredMember);
                if(user != undefined) {
                    updateMember(user, deferredMember);
                }
            }
            return {
                loadAllMembers: loadAllMembers,
                makeMember: makeMember,
                addMember: addMember,
                deleteMember: function (member) {
                    var deferred = $q.defer();
                    var GroupMember = $resource(member._links.self.href);
                    GroupMember.delete().$promise.then(function (response) {
                        deferred.resolve(response);
                    }, function (response) {
                        $log.error('deleteMember:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                saveEnabledMember: function (member) {
                    var deferred = $q.defer();
                    var GroupMember = $resource(member._links.self.href, {}, {update: {method: 'PATCH'}});
                    GroupMember.update({enabled:member.enabled}).$promise.then(function (response) {
                        deferred.resolve(response);
                    }, function (response) {
                        $log.error('saveMember:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                createMember: function (member) {
                    var deferred = $q.defer();
                    GroupMembers.create(member).$promise.then(function (response) {
                        deferred.resolve(response);
                    }, function (response) {
                        $log.error('createMember:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                loadGroupMembers: loadGroupMembers
            }
        }]);
})();