(function () {
    'use strict';
    angular.module('springDataRestDemo')
        .service('NotificationService', ['$mdToast', function ($mdToast) {
            return {
                toastMessage: function (msg) {
                    $mdToast.show($mdToast.simple()
                        .parent(angular.element(document.body))
                        .position('top right')
                        .textContent(msg)
                        .hideDelay(3000));
                }, toastError: function (msg, action) {
                    if (action == undefined) {
                        action = 'Close';
                    }
                    var simple = $mdToast.simple()
                        .parent(angular.element(document.body))
                        .position('top right')
                        .theme('error')
                        .textContent(msg)
                        .action(action).highlightAction(true);
                    $mdToast.show(simple);
                }
            };
        }]);
})();