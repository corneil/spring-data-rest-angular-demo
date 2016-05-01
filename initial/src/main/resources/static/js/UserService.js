(function () {
    'use strict';
    angular.module('springDataRestDemo').service('UserService', ['$q', '$resource', '$log', 'userCache', '$http',
        function ($q, $resource, $log, userCache, $http) {
            var Users = $resource('/api/users', {}, {
                create: {method: 'POST'},
                list: {method: 'GET'}
            });
            function countGroupsOwned(user) {
                var deferred = $q.defer();
                $http.get('/api/groups/search/countByGroupOwner_UserId?userId=' + user.userId).then(function (response) {
                    $log.debug("countGroupsOwned:response:" + JSON.stringify(response, null,2));
                    deferred.resolve(response.data - 0);
                },function (response) {
                    $log.error('deleteGroupMembersForUser:failed:' + JSON.stringify(response, null, 2));
                    deferred.reject(response);
                });
                return deferred.promise;
            }
            function deleteGroupMembersForUser(user) {
                var deleteDeferred = $q.defer();
                var MemberSearch = $resource('/api/group-member/search/findByMember_UserId?userId=:userId', {userId: '@userId'});
                MemberSearch.get({userId: user.userId}).$promise.then(function (groupMembers) {
                        var deletePromises = [];
                        for (var i in groupMembers._embedded.groupMembers) {
                            var groupMember = groupMembers._embedded.groupMembers[i];
                            $log.debug('Deleting:' + groupMember._links.self.href);
                            var GroupMember = $resource(groupMember._links.self.href);
                            deletePromises.push(GroupMember.delete().$promise);
                        }
                        if (deletePromises.length != 0) {
                            $q.all(deletePromises).then(function (data) {
                                deleteDeferred.resolve(data);
                            }, function (response) {
                                $log.error('deleteGroupMembersForUser:failed:' + JSON.stringify(response, null, 2));
                                deleteDeferred.reject(response);
                            });
                        } else {
                            deleteDeferred.resolve(groupMembers);
                        }
                    },
                    function (response) {
                        $log.error('deleteGroupMembersForUser:failed:' + JSON.stringify(response, null, 2));
                        deleteDeferred.reject(response);
                    });
                return deleteDeferred.promise;
            }

            return {
                searchPartial: function (input) {
                    var deferred = $q.defer();
                    $log.debug('searching for :' + input);
                    var UserQuery = $resource('/api/users/search/findLikeUserIdOrFullName?input=:input', {input: '@input'});
                    UserQuery.get({input: input}).$promise.then(function (users) {
                            deferred.resolve(users._embedded.users);
                        },
                        function (response) {
                            $log.error('searchPartial:failed:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                loadAllUsers: function () {
                    var deferred = $q.defer();
                    $log.debug('calling /api/users');
                    Users.list().$promise.then(function (users) {
                            deferred.resolve(users._embedded.users);
                        },
                        function (response) {
                            $log.error('loadAllUsers:failed:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                loadUser: function (userRef) {
                    var User = $resource(userRef, {}, {get: {method: 'GET', cache: userCache}});
                    return User.get().$promise;
                },
                deleteUser: function (user) {
                    var deferred = $q.defer();
                    var countGroupsPromise = countGroupsOwned(user);
                    countGroupsPromise.then(function(result) {
                        if(result > 0) {
                            deferred.reject({statusLine:'Cannot delete ' + user.userId + ' owns ' + result + ' groups'});
                        } else {
                            var deletePromise = deleteGroupMembersForUser(user);
                            deletePromise.then(function () {
                                $log.debug('deleting :' + user._links.self.href);
                                var User = $resource(user._links.self.href);
                                User.delete().$promise.then(function (response) {
                                    userCache.remove(user._links.self.href);
                                    deferred.resolve(response);
                                }, function (response) {
                                    $log.error('deleteUser:failed:' + JSON.stringify(response, null, 2));
                                    deferred.reject(response);
                                });
                            }, function (response) {
                                deferred.reject(response);
                            });
                        }
                    }, function (response) {
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                createUser: function (user) {
                    var deferred = $q.defer();
                    $log.debug('calling /api/users');
                    Users.create(user).$promise.then(function (user) {
                        userCache.put(user._links.self.href, user);
                        deferred.resolve(user);
                    },
                    function (response) {
                        $log.error('createUser:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                saveUser: function (user) {
                    var deferred = $q.defer();
                    $log.debug('PUT -> ' + ':' + user._links.self.href);
                    var User = $resource(user._links.self.href, {}, {save: {method: 'PUT'}});
                    User.save(user).$promise.then(function (response) {
                        userCache.put(response.data._links.self.href, response.data);
                        deferred.resolve(response.data);
                    },
                    function (response) {
                        $log.error('saveUser:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                }
            };
        }]);
})();