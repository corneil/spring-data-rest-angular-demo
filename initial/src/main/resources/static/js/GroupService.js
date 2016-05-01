(function () {
    'use strict';
    angular.module('springDataRestDemo')
        .service('GroupService', ['$q', '$resource', '$log', 'UserService', 'groupCache', function ($q, $resource, $log, UserService, groupCache) {
            var Groups = $resource('/api/groups', {}, {
                list: {method: 'GET'},
                create: {method: 'POST'}
            });

            function loadGroupOwner(group) {
                var deferred = $q.defer();
                var promise = UserService.loadUser(group._links._groupOwner.href);
                promise.then(function (user) {
                    group._groupOwner = user;
                    group.groupOwner = user._links.self.href;
                    deferred.resolve(group);
                }, function (response) {
                    $log.error('loadGroupOwner:failure:response:' + JSON.stringify(response, null, 2));
                    group._groupOwner = undefined;
                    deferred.reject(response);
                });
                return deferred.promise;
            }

            function deleteGroupMember(groupMemberRef) {
                $log.info('deleteGroupMember:' + groupMemberRef);
                return $resource(groupMemberRef).delete().$promise;
            }

            function deleteMembersForGroup(group) {
                var deferred = $q.defer();
                var promises = [];
                var GroupMembers = $resource('/api/group-member/search/findByMemberOfgroup_GroupName?groupName=:groupName', {groupName: '@groupName'});
                GroupMembers.get({groupName: group.groupName}).$promise.then(
                    function (groups) {
                        for (var i in groups._embedded.groupMembers) {
                            var groupMember = groups._embedded.groupMembers[i];
                            promises.push(deleteGroupMember(groups._embedded.groupMembers[i]._links.self.href));
                        }
                        $q.all(promises).then(function (data) {
                            deferred.resolve(data);
                        }, function (response) {
                            $log.error('deleteMembersForGroup:failure:response:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    }, function (response) {
                        $log.error('deleteMembersForGroup:failure:response:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    }
                );
                return deferred.promise;
            }

            return {
                loadAllGroups: function (loadOwners) {
                    var deferred = $q.defer();
                    Groups.list().$promise.then(function (groups) {
                            var groups = groups._embedded.groups;
                            if (loadOwners != undefined && loadOwners) {
                                var promises = [];
                                for (var g in groups) {
                                    promises.push(loadGroupOwner(groups[g]));
                                }
                                $q.all(promises).then(function () {
                                    deferred.resolve(groups);
                                }, function (response) {
                                    $log.error('loadAllGroups:failure:response:' + JSON.stringify(response, null, 2));
                                    deferred.reject(response);
                                });
                            } else {
                                deferred.resolve(groups);
                            }
                        },
                        function (response) {
                            $log.error('loadAllGroups:failure:response:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                loadGroup: function (groupRef) {
                    var deferred = $q.defer();
                    var Group = $resource(groupRef, {}, {get: {method: 'GET', cache: groupCache}});
                    Group.get().$promise.then(function (group) {
                        loadGroupOwner(group).then(function (g) {
                            deferred.resolve(g);
                        }, function (response) {
                            deferred.reject(response);
                        });
                    }, function (response) {
                        $log.error('loadGroup:failure:response:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                createGroup: function (group) {
                    var deferred = $q.defer();
                    var savedOwner = group._groupOwner;
                    group.groupOwner = savedOwner._links.self.href;
                    Groups.create(group).$promise.then(
                        function (group) {
                            loadGroupOwner(group).then(function (g) {
                                g._groupOwner = savedOwner;
                                g.groupOwner = savedOwner._links.self.href;
                                deferred.resolve(g);
                            }, function (response) {
                                deferred.reject(response);
                            });
                        },
                        function (response) {
                            $log.error('createGroup:failure:response:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                saveGroup: function (group) {
                    var deferred = $q.defer();
                    var savedOwner = group._groupOwner;
                    group.groupOwner = savedOwner._links.self.href;
                    var Group = $resource(group._links.self.href, {}, {save: {method: 'PUT'}});
                    Group.save(group).$promise.then(
                        function (saved) {
                            groupCache.remove(saved._links.self.href);
                            saved._groupOwner = savedOwner;
                            saved.groupOwner = savedOwner._links.self.href;
                            deferred.resolve(saved);
                        },
                        function (response) {
                            $log.error('saveGroup:failure:response:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                deleteGroup: function (group) {
                    var deferred = $q.defer();
                    deleteMembersForGroup(group).then(function () {
                        var Group = $resource(group._links.self.href);
                        Group.delete().$promise.then(
                            function (response) {
                                groupCache.remove(group._links.self.href);
                                deferred.resolve(response);
                            }, function (response) {
                                $log.error('deleteGroup:failure:response:' + JSON.stringify(response, null, 2));
                                deferred.reject(response);
                            });
                    }, function (response) {
                        $log.error('deleteGroup:failure:response:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                }
            };
        }]);
})();