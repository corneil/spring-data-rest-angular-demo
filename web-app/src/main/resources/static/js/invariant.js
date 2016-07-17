/**
 * Copyright 2013-2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

'use strict';

/**
 * Use assertTrue() to assert state which your program assumes to be true.
 *
 * Provide sprintf-style format (only %s is supported) and arguments
 * to provide information about what broke and what you were
 * expecting.
 *
 * The assertTrue message will be stripped in production, but the assertTrue
 * will remain to ensure logic does not differ in production.
 */
function assertTrue(condition, format, a, b, c, d, e, f) {
    if (!condition) {
        var error;
        if (format === undefined) {
            error = new Error("Expected format");
        } else {
            var args = [a, b, c, d, e, f];
            var argIndex = 0;
            error = new Error(
                format.replace(/%s/g, function () {
                    return args[argIndex++];
                })
            );
            error.name = 'Invariant Violation';
        }

        error.framesToPop = 1; // we don't care about assertTrue's own frame
        throw error;
    }
}


function assertNotNull(expression, format, a, b, c, d, e, f) {
    if(expression == undefined || expression == null) {
        var error;
        if (format === undefined) {
            error = new Error('Expression not null');
        } else {
            var args = [a, b, c, d, e, f];
            var argIndex = 0;
            error = new Error(
                format.replace(/%s/g, function () {
                    return args[argIndex++];
                })
            );
            error.name = 'Invariant Violation';
        }
        error.framesToPop = 1;
        throw error;
    }
}