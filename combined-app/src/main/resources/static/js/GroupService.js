(function () {
    'use strict';
    angular.module('springDataRestDemo')
        .service('GroupService', ['$q', '$resource', '$log', 'UserService', 'groupCache', function ($q, $resource, $log, UserService, groupCache) {
            var Groups = $resource('/rest/groups', {}, {
                list: {method: 'GET'},
                create: {method: 'POST'}
            });

            function loadGroupOwner(group) {
                var deferred = $q.defer();
                var promise = UserService.loadUser(group._links.groupOwner.href);
                promise.then(function (user) {
                    group._groupOwner = user;
                    group.groupOwner = user._links.self.href;
                    deferred.resolve(group);
                }, function (response) {
                    $log.error('loadGroupOwner:failure:response:' + JSON.stringify(response, null, 2));
                    group.groupOwner = undefined;
                    group._groupOwner = undefined;
                    deferred.reject(response);
                });
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
                    Groups.create({groupName: group.groupName,
                        description: group.description,
                        groupOwner: group.groupOwner}
                    ).$promise.then(
                        function (saved) {
                            loadGroupOwner(saved).then(function (loaded) {
                                loaded._groupOwner = savedOwner;
                                loaded.groupOwner = savedOwner._links.self.href;
                                deferred.resolve(loaded);
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
                    var Group = $resource(group._links.self.href, {}, {save: {method: 'PATCH'}});
                    Group.save({
                        groupName: group.groupName,
                        description: group.description,
                        groupOwner: group.groupOwner
                    }).$promise.then(
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
                    var Group = $resource(group._links.self.href);
                    Group.delete().$promise.then(
                        function (response) {
                            groupCache.remove(group._links.self.href);
                            deferred.resolve(response);
                        }, function (response) {
                            $log.error('deleteGroup:failure:response:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                }
            };
        }]);
})();