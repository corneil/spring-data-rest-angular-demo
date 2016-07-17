(function () {
    'use strict';
    angular.module('springDataRestDemo').service('UserService', ['$q', '$resource', '$log', 'userCache', '$http',
        function ($q, $resource, $log, userCache, $http) {
            var Users = $resource('/rest/users', {}, {
                create: {method: 'POST'},
                list: {method: 'GET'}
            });

            return {
                searchPartial: function (input) {
                    var deferred = $q.defer();
                    $log.debug('searching for :' + input);
                    var UserQuery = $resource('/rest/users/search/find?input=:input', {input: '@input'});
                    UserQuery.get({input: input}).$promise.then(function (users) {
                            if(users._embedded != undefined && users._embedded.users != undefined) {
                                deferred.resolve(users._embedded.users);
                            } else {
                                deferred.resolve([]);
                            }
                        },
                        function (response) {
                            $log.error('searchPartial:failed:' + JSON.stringify(response, null, 2));
                            deferred.reject(response);
                        });
                    return deferred.promise;
                },
                loadAllUsers: function () {
                    var deferred = $q.defer();
                    $log.debug('calling /rest/users');
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
                    $log.debug('deleting :' + user._links.self.href);
                    var User = $resource(user._links.self.href);
                    User.delete().$promise.then(function (response) {
                        userCache.remove(user._links.self.href);
                        deferred.resolve(response);
                    }, function (response) {
                        $log.error('deleteUser:failed:' + JSON.stringify(response, null, 2));
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                createUser: function (user) {
                    var deferred = $q.defer();
                    $log.debug('calling /rest/users');
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
                    User.save(user).$promise.then(function (data) {
                        userCache.put(data._links.self.href, data);
                        deferred.resolve(data);
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