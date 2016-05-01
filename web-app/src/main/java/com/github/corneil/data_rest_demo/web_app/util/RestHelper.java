package com.github.corneil.data_rest_demo.web_app.util;

import org.springframework.hateoas.Link;
import org.springframework.util.Assert;

/**
 * Created by Corneil on 2016-05-01.
 */
public class RestHelper {
    public static String resourceId(String instanceUrl, String resourceUrl) {
        Assert.isTrue(instanceUrl.startsWith(resourceUrl));
        String idPart = instanceUrl.substring(resourceUrl.length());
        if(idPart.startsWith("/")) {
            return idPart.substring(1);
        }
        return idPart;
    }
}
