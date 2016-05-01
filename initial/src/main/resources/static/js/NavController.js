var mainMenu = [
    {
        name: 'users', title: 'Users',
        icon: 'social:ic_person',
        href: '/users',
        templateName: 'templates/users.html',
        controller: 'UserController'
    },
    {
        name: 'groups',
        title: 'Groups',
        icon: 'social:ic_people',
        href: '/groups',
        templateName: 'templates/groups.html',
        controller: 'GroupController'
    },
    {
        name: 'members',
        title: 'Members',
        icon: 'social:ic_person_add',
        href: '/members',
        templateName: 'templates/members.html',
        controller: 'MemberController'
    },
    {
        name: 'themes',
        title: 'Themes',
        icon: 'image:ic_color_lens',
        href: '/themes',
        templateName: 'templates/themes.html',
        controller: 'ThemeController'
    }
];
var adminMenu = [
    {
        name: 'halBrower',
        title: 'HAL Browser',
        icon: 'social:ic_domain',
        href: '/admin/hal-browser',
        templateName: 'templates/hal-browser.html',
        controller: 'NavController'
    },
    {
        name: 'h2Console',
        title: 'H2 Console',
        icon: 'action:ic_dashboard',
        href: '/admin/h2-console',
        templateName: 'templates/h2-console.html',
        controller: 'NavController'
    }
];
function configureRoutes($routeProvider) {
    $routeProvider.when('/home', {templateUrl: 'templates/home.html', controller: 'NavController'});
    for (var i in mainMenu) {
        var item = mainMenu[i];
        $routeProvider.when(item.href, {templateUrl: item.templateName, controller: item.controller});
    }
    for (var i in adminMenu) {
        var item = adminMenu[i];
        $routeProvider.when(item.href, {templateUrl: item.templateName, controller: item.controller});
    }
    $routeProvider.otherwise({redirectTo: '/home'});
}

(function () {
    'use strict';
    angular.module('springDataRestDemo').controller('NavController',
        ['$scope', '$q', '$mdMedia', '$mdSidenav', '$log', '$location',
            function ($scope, $q, $mdMedia, $mdSidenav, $log, $location) {
                $scope.openSidenav = function () {
                    $mdSidenav('sideNav').open();
                };
                $scope.closeSidenav = function () {
                    $mdSidenav('sideNav').close();
                };
                $scope.iconStyle = {'medium-icons': $mdMedia('xs'), 'large-icons': $mdMedia('sm'), 'x-large-icons': $mdMedia('gt-sm')};
                $scope.activeContent = 'templates/users.html';
                $scope.mainMenu = mainMenu;
                $scope.adminMenu = adminMenu;
                $scope.showMenu = function (item) {
                    $log.debug('menu-selection:' + item.name + ':' + item.href);
                    $scope.activeContent = item.templateName;
                    $location.url(item.href);
                    $scope.closeSidenav();
                }
            }]);
})();